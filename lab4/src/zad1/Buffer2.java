package zad1;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

class Buffer2 implements IBuffer {
    private final int maxBufferSize;
    private final AtomicInteger producersRunning;
    private final AtomicInteger consumersRunning;
    private final BlockingQueue<Integer> buffer;

    public Buffer2(int size, int m, int n) {
        maxBufferSize = size * 2;
        producersRunning = new AtomicInteger(m);
        consumersRunning = new AtomicInteger(n);
        buffer = new ArrayBlockingQueue<>(maxBufferSize);
    }

    @Override
    public void unregisterProducer() {
        producersRunning.decrementAndGet();
    }

    @Override
    public void unregisterConsumer() {
        consumersRunning.decrementAndGet();
    }

    @Override
    public boolean isAnySideInterested() {
        return producersRunning.get() > 0 && consumersRunning.get() > 0;
    }

    @Override
    public int maxSize() {
        return maxBufferSize;
    }

    @Override
    public void put(int[] products) throws InterruptedException {
        for (var v : products) {
            if(!isAnySideInterested()){
                return;
            }
            buffer.put(v);
        }
    }

    @Override
    public void get(List<Integer> results, int howMany) throws InterruptedException {

        while (results.size() < howMany) {
            if(!isAnySideInterested()){
                return;
            }
            Integer v = buffer.take();
            results.add(v);
        }
    }
}
