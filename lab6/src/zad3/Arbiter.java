package zad3;

import java.util.concurrent.Semaphore;

public class Arbiter {
    private final Semaphore semaphore = new Semaphore(Main.PHILOSOPHERS_NUM - 1);

    public void acquireForks() throws InterruptedException {
        semaphore.acquire();
    }

    public void releaseForks() {
        semaphore.release();
    }
}
