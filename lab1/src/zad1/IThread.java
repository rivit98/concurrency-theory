package zad1;

import java.util.stream.IntStream;

class IThread extends Thread {
    public final Counter counter;

    IThread(Counter c) {
        this.counter = c;
    }

    public void run() {
        IntStream.range(0, 100000).forEach(i -> {
            counter.inc();
        });
    }
}
