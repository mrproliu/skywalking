package org.apache.skywalking.apm.commons.datacarrier;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.InsufficientCapacityException;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.apache.skywalking.apm.commons.datacarrier.buffer.BufferStrategy;
import org.apache.skywalking.apm.commons.datacarrier.consumer.IConsumer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@State(Scope.Thread)
public class DataCarrierBenchmark {

    private RingBuffer<StringWrapper> ringBuffer;
    private Disruptor<StringWrapper> disruptor;
    private DataCarrier<String> dataCarrier = null;

    @Param({"BLOCKING", "IF_POSSIBLE"})
    private BufferStrategy strategy;

    @Param({"512", "1024", "2048"})
    private int bufferSize;

    @Param({"1", "10", "20"})
    private int consumerCount;

    @Setup
    public void setup(Blackhole blackhole) {
        // Init Disruptor
        disruptor = new Disruptor<>(StringWrapper::new, bufferSize, DaemonThreadFactory.INSTANCE,
            ProducerType.SINGLE, new SleepingWaitStrategy(0, 20));
        ringBuffer = disruptor.getRingBuffer();
        disruptor.handleEventsWith(IntStream.range(0, consumerCount).mapToObj(i -> (EventHandler<StringWrapper>) (event, sequence, endOfBatch) -> {
            blackhole.consume(event);
        }).collect(Collectors.toList()).toArray(new EventHandler[0]));
        disruptor.start();

        // Init DataCarrier
        dataCarrier = new DataCarrier<>(10, bufferSize);
        dataCarrier.setBufferStrategy(strategy);
        dataCarrier.consume(new DataCarrierConsumer(blackhole), consumerCount);
    }

    @Benchmark
    @Group("DisruptorAndDataCarrier")
    public void disruptor(Blackhole blackhole) {
        try {
            long inx = strategy == BufferStrategy.IF_POSSIBLE ? ringBuffer.tryNext() : ringBuffer.next();
            ringBuffer.get(inx).setValue("");
            ringBuffer.publish(inx);
            blackhole.consume(inx);
        } catch (InsufficientCapacityException e) {
        }
    }

    @Benchmark
    @Group("DisruptorAndDataCarrier")
    public void dataCarrier(Blackhole blackhole) {
        if (dataCarrier.produce("")) {
            blackhole.consume(true);
        }
    }

    @TearDown
    public void tearDown() {
        disruptor.shutdown();
        dataCarrier.shutdownConsumers();
    }

    private static class StringWrapper {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(DataCarrierBenchmark.class.getSimpleName())
            .jvmArgsAppend("-Xmx512m", "-Xms512m")
            .forks(1)
            .build();

        new Runner(opt).run();
    }

    private static class DataCarrierConsumer implements IConsumer<String> {
        private final Blackhole blackhole;

        public DataCarrierConsumer(Blackhole blackhole) {
            this.blackhole = blackhole;
        }

        @Override
        public void init() {
        }

        @Override
        public void consume(List<String> data) {
            for (String s : data) {
                blackhole.consume(s);
            }
        }

        @Override
        public void onError(List<String> data, Throwable t) {
        }

        @Override
        public void onExit() {
        }
    }
}
