import proxy.BufferProxy;

import java.util.LinkedList;
import java.util.stream.IntStream;

public class Main {
    private static final int BUFFER_CAPACITY = 10;
    private static final int CONSUMER_THREADS = 5;
    private static final int PRODUCER_THREADS = 5;
    private static final int ELEMENTS_PER_THREAD = 20;


    public static void main(String[] args) {
        var threadList = new LinkedList<Thread>();
        var bufferProxy = new BufferProxy(BUFFER_CAPACITY);

        IntStream.range(0, CONSUMER_THREADS).forEach(i -> {
            threadList.add(new Consumer(bufferProxy, ELEMENTS_PER_THREAD));
        });

        IntStream.range(0, PRODUCER_THREADS).forEach(i -> {
            threadList.add(new Producer(bufferProxy, ELEMENTS_PER_THREAD));
        });

        threadList.forEach(Thread::start);

        threadList.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
