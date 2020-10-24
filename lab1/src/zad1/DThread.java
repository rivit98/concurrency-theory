package zad1;

import java.util.stream.IntStream;

class DThread extends Thread {
    public final Counter counter;

    DThread(Counter c) {
        this.counter = c;
    }

    public void run() {
        IntStream.range(0, 100000).forEach(i -> {
            counter.dec();
        });
    }
}
