package zad2;

import java.util.stream.IntStream;

class IThread extends MyThread {
    IThread(Executor e, Counter c) {
        super(e, c);
    }

    public void run() {
        IntStream.range(0, 100000).forEach(i -> {
            if (this.sleepingTime > 0) {
                try {
                    Thread.sleep(this.sleepingTime);
                } catch (InterruptedException e) {
                    this.wakeup();
                }
            }

            counter.inc();
        });
        this.executor.notifyAboutFinished(this);
    }
}
