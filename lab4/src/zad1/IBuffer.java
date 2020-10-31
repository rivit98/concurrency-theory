package zad1;

import java.util.List;

public interface IBuffer {
    void unregisterProducer();
    void unregisterConsumer();

    int maxSize();

    void put(int[] products) throws InterruptedException;

    void get(List<Integer> results, int howMany) throws InterruptedException;

    boolean isAnySideInterested();
}
