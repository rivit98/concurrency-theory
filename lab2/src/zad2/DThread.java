package zad2;

import java.util.stream.IntStream;

public class DThread extends Thread {
    public final Counter counter;

    public DThread(Counter c) {
        this.counter = c;
    }

    public void run() {
        IntStream.range(0, 100000).forEach(i -> counter.dec());
    }
}