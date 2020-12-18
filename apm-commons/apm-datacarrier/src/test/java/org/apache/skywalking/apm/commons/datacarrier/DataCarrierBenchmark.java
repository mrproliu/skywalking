/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@BenchmarkMode({Mode.Throughput})
@State(Scope.Benchmark)
public class DataCarrierBenchmark {

    private RingBuffer<StringWrapper> ringBuffer;
    private Disruptor<StringWrapper> disruptor;
    private DataCarrier<String> dataCarrier = null;

    @Param({"BLOCKING", "IF_POSSIBLE"})
    private BufferStrategy strategy;

    @Param({"512", "1024", "8192"})
    private int bufferSize;

    @Param({"1", "5", "10"})
    private int consumerCount;

    @Setup
    public void setup(Blackhole blackhole) {
        // Init Disruptor
        disruptor = new Disruptor<>(StringWrapper::new, bufferSize, DaemonThreadFactory.INSTANCE,
            ProducerType.MULTI, new SleepingWaitStrategy(0, 20));
        ringBuffer = disruptor.getRingBuffer();
        disruptor.handleEventsWith(IntStream.range(0, consumerCount).mapToObj(i -> (EventHandler<StringWrapper>) (event, sequence, endOfBatch) -> {
            blackhole.consume(event);
        }).collect(Collectors.toList()).toArray(new EventHandler[0]));
        disruptor.start();

        // Init DataCarrier
        dataCarrier = new DataCarrier<>(5, bufferSize);
        dataCarrier.setBufferStrategy(strategy);
        dataCarrier.consume(new DataCarrierConsumer(blackhole), consumerCount);
    }

    @Benchmark
    @Threads(5)
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
    @Threads(5)
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
            .result("result.json")
            .resultFormat(ResultFormatType.JSON)
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
