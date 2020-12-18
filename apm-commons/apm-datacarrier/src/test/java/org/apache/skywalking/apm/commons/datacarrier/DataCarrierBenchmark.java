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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author MrPro
 * @description: TODO
 * @date 2020-12-16 12:47:00
 */
@RunWith(Parameterized.class)
public class DataCarrierBenchmark {

    @Parameterized.Parameter
    public String name;

    @Parameterized.Parameter(1)
    public int channelSize;

    @Parameterized.Parameter(2)
    public int bufferSize;

    @Parameterized.Parameter(3)
    public int producerCount;

    @Parameterized.Parameter(4)
    public int consumerCount;

    @Parameterized.Parameter(5)
    public BufferStrategy bufferStrategy;

    @Parameterized.Parameter(6)
    public boolean isDataCarrier;

    private AtomicInteger produceCount = new AtomicInteger();
    private AtomicInteger produceSuccessCount = new AtomicInteger();
    private AtomicInteger consumeCount = new AtomicInteger();

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        int[] producerArray = {1, 5, 10};
        int[] consumerArray = {1, 5, 10};
        int[] channelArray = {5, 10};
        int[] bufferArray = {512, 1024, 2048};
        BufferStrategy[] strategies = {BufferStrategy.BLOCKING, BufferStrategy.IF_POSSIBLE};
        boolean[] isDataCarrierArray = {true, false};

        List<Object[]> result = new ArrayList<Object[]>();
        for (int producerCount : producerArray) {
            for (int consumerCount : consumerArray) {
                for (int channelSize : channelArray) {
                    for (int bufferSize : bufferArray) {
                        for (BufferStrategy strategy : strategies) {
                            for (boolean isDataCarrier : isDataCarrierArray) {
                                result.add(new Object[] {
                                    "Producer:" + producerCount + ",Consumer:" + consumerCount + ",Channel:" + channelSize + ",Buffer:" + bufferSize + ",Strategy:" + strategy + ",dataCarrier:" + isDataCarrier,
                                    channelSize, bufferSize, producerCount, consumerCount, strategy, isDataCarrier
                                });
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Test
    public void testWithBlocking() throws InterruptedException, IOException {
        DataCarrier<String> data = null;
        RingBuffer<StringWrapper> ringBuffer = null;
        if (isDataCarrier) {
            data = new DataCarrier<>(channelSize, bufferSize);
            data.setBufferStrategy(bufferStrategy);
            data.consume(new DataCarrierConsumer(consumeCount), consumerCount);
        } else {
            Disruptor<StringWrapper> disruptor = new Disruptor<>(StringWrapper::new, bufferSize, DaemonThreadFactory.INSTANCE,
                producerCount == 1 ? ProducerType.SINGLE : ProducerType.MULTI, new SleepingWaitStrategy(0, 20));
            ringBuffer = disruptor.getRingBuffer();

            disruptor.handleEventsWith(IntStream.range(0, consumerCount).mapToObj(i -> (EventHandler<StringWrapper>) (event, sequence, endOfBatch) -> {
                consumeCount.addAndGet(1);
            }).collect(Collectors.toList()).toArray(new EventHandler[0]));
            disruptor.start();
        }

        // create monitor
        ScheduledExecutorService monitorService = Executors.newSingleThreadScheduledExecutor();
        AvgCalc producerAvg = new AvgCalc();
        AvgCalc producerSuccessAcg = new AvgCalc();
        AvgCalc consumerAvg = new AvgCalc();
        monitorService.scheduleAtFixedRate(() -> {
            int produceCount = this.produceCount.getAndSet(0);
            int consumerCount = this.consumeCount.getAndSet(0);
            int produceSuccessCount = this.produceSuccessCount.getAndSet(0);
            System.out.println("rate: produce:" + produceCount + ", produce success: " + produceSuccessCount + ", consume:" + consumerCount);
            producerAvg.add(produceCount);
            consumerAvg.add(consumerCount);
            producerSuccessAcg.add(produceSuccessCount);
        }, 1, 1, TimeUnit.SECONDS);

        // create producer
        ExecutorService producerPool = Executors.newFixedThreadPool(producerCount);
        if (isDataCarrier) {
            DataCarrier<String> finalData = data;
            for (int i = 0; i < producerCount; i++) {
                producerPool.submit(() -> {
                    for (long j = 0; j < Long.MAX_VALUE; j++) {
                        produceCount.incrementAndGet();
                        if (finalData.produce("")) {
                            produceSuccessCount.incrementAndGet();
                        }
                    }
                });
            }
        } else {
            RingBuffer<StringWrapper> finalRingBuffer = ringBuffer;
            for (int i = 0; i < producerCount; i++) {
                producerPool.submit(() -> {
                    for (long j = 0; j < Long.MAX_VALUE; j++) {
                        produceCount.incrementAndGet();
                        try {
                            long inx = bufferStrategy == BufferStrategy.BLOCKING ? finalRingBuffer.next() : finalRingBuffer.tryNext();
                            finalRingBuffer.get(inx).setValue("");
                            finalRingBuffer.publish(inx);
                            produceSuccessCount.incrementAndGet();
                        } catch (InsufficientCapacityException e) {
                        }
                    }
                });
            }
        }


        // sleep and shutdown
        TimeUnit.SECONDS.sleep(10);
        producerPool.shutdownNow();
        monitorService.shutdownNow();

        // print result
        String result = name + ":Producer Avg: " + producerAvg.avg() + ", Producer Success Avg: " + producerSuccessAcg.avg() + ", Consumer Avg: " + consumerAvg.avg();
        System.out.println(result);
        Files.write(new File("/Users/liuhan/Desktop/dataCarrier.txt").toPath(), (result + "\n").getBytes(),
            StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }

    private class AvgCalc {
        public int count;
        public int size;

        public void add(int size) {
            this.size += size;
            this.count += 1;
        }
        public int avg() {
            return size / count;
        }
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

    private static class DataCarrierConsumer implements IConsumer<String> {
        private final AtomicInteger consumeCount;

        public DataCarrierConsumer(AtomicInteger consumeCount) {
            this.consumeCount = consumeCount;
        }

        @Override
        public void init() {
        }

        @Override
        public void consume(List<String> data) {
            consumeCount.addAndGet(data.size());
        }

        @Override
        public void onError(List<String> data, Throwable t) {
        }

        @Override
        public void onExit() {
        }
    }

}
