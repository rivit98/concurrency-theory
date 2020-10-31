package zad1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class Buffer implements IBuffer {
    private final LinkedList<Integer> buffer = new LinkedList<>();
    private final int maxBufferSize;
    private final Object lock = new Object();
    private Integer producersRunning = 0;
    private Integer consumersRunning = 0;

    public Buffer(int size, int m, int n) {
        maxBufferSize = size * 2;
        producersRunning = m;
        consumersRunning = n;
    }

    @Override
    public void unregisterProducer() {
        synchronized (lock) {
            producersRunning--;
        }
    }

    @Override
    public void unregisterConsumer() {
        synchronized (lock) {
            consumersRunning--;
        }
    }

    @Override
    public boolean isAnySideInterested() {
        return producersRunning > 0 && consumersRunning > 0;
    }

    @Override
    public int maxSize() {
        return maxBufferSize;
    }

    @Override
    public synchronized void put(int[] products) throws InterruptedException {
        while (isAnySideInterested() && (buffer.size() + products.length >= maxBufferSize)) {
            wait();
        }

        if (!isAnySideInterested()) {
            return;
        }

        buffer.addAll(Arrays.stream(products).boxed().collect(Collectors.toList()));
        notifyAll();
    }

    @Override
    public synchronized void get(List<Integer> results, int howMany) throws InterruptedException {
        while (isAnySideInterested() && buffer.size() < howMany) {
            wait();
        }

        if (!isAnySideInterested()) {
            return;
        }

        var sublist = buffer.subList(0, howMany);
        for(int i = 0; i < howMany; i++){
            var v = sublist.get(i);
            results.add(v);
        }
        sublist.clear();
        notifyAll();
    }
}
