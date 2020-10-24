package zad1;

import java.util.stream.IntStream;

public class IThread extends Thread {
    public final Counter counter;

    public IThread(Counter c) {
        this.counter = c;
    }

    public void run() {
        IntStream.range(0, 100000).forEach(i -> counter.inc());
    }
}