package zad1;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

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
        int n = producersRunning.decrementAndGet();
//        System.out.println("Producer finished " + n + " left");
    }

    @Override
    public void unregisterConsumer() {
        int n = consumersRunning.decrementAndGet();
//        System.out.println("Consumer finished " + n + " left");
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
//        System.out.println("attempt to put " + products.length);
        for (var v : products) {
            if(!isAnySideInterested()){
                return;
            }
            buffer.put(v);
        }
    }

    @Override
    public void get(List<Integer> results, int howMany) throws InterruptedException {
//        System.out.println("attempt to get " + howMany + " by " + Thread.currentThread().getId());

        while (results.size() < howMany) {
            if(!isAnySideInterested()){
                return;
            }
//            System.out.println("pre take bsize " + buffer.size());
            Integer v = buffer.take();
            results.add(v);
//            System.out.println(
//                    "taking: " + results.size() + "/" + howMany +
//                            " | " + Thread.currentThread().getId() +
//                            " bsize " + buffer.size()

//            );
        }
    }
}
