package zad1;

import java.util.concurrent.Semaphore;

class Fork {
    private final Semaphore semaphore = new Semaphore(1);

    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }
}
