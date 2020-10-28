package zad1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

class Buffer {
    private final LinkedList<Integer> buffer = new LinkedList<>();
    private final int maxBufferSize;
    private final Object lock = new Object();
    private Integer producersRunning = 0;
    private Integer consumersRunning = 0;

    public Buffer(int size) {
        maxBufferSize = size * 2;
    }

    public void registerProducer() {
        synchronized (lock) {
            producersRunning++;
        }
    }

    public void unregisterProducer() {
        synchronized (lock) {
            producersRunning--;
            checkEnd();
        }
    }

    public void registerConsumer() {
        synchronized (lock) {
            consumersRunning++;
        }
    }

    public void unregisterConsumer() {
        synchronized (lock) {
            consumersRunning--;
            checkEnd();
        }
    }

    public void checkEnd() {
        if (isJobFinished()) {
            notifyAll();
        }
    }

    public boolean isJobFinished() {
        return producersRunning <= 0 || consumersRunning <= 0;
    }

    public int maxSize() {
        return maxBufferSize;
    }

    public synchronized void put(int[] products) {
        while (!isJobFinished() && buffer.size() + products.length >= maxBufferSize) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }

        if (isJobFinished()) {
            notifyAll();
            return;
        }

        buffer.addAll(Arrays.stream(products).boxed().collect(Collectors.toList()));
//        System.out.println("producing " + Arrays.toString(products));
//        System.out.println("Buffer: " + buffer.toString());
        notifyAll();
    }

    public synchronized int[] get(int howMany) {
        while (!isJobFinished() && buffer.size() < howMany) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }

        if (isJobFinished()) {
            notifyAll();
            return new int[]{};
        }

        var sublist = buffer.subList(0, howMany);
        var products = sublist.stream().mapToInt(i -> i).toArray();
        sublist.clear();
//        System.out.println("Consumed: " + Arrays.toString(products));
//        System.out.println("Buffer: " + buffer.toString());
        notifyAll();
        return products;
    }
}
