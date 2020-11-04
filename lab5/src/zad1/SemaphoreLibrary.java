package zad1;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

class SemaphoreLibrary implements ILibrary {
    private final Semaphore readerCountSemaphore = new Semaphore(1);
    private final Semaphore writerSemaphore = new Semaphore(1);
    private final AtomicInteger readerCount = new AtomicInteger(0);

    @Override
    public void read() throws InterruptedException {
        readerCountSemaphore.acquire();
        var value = readerCount.incrementAndGet();
        if (value == 1) {
            writerSemaphore.acquire();
        }
        readerCountSemaphore.release();

        // read something

        readerCountSemaphore.acquire();
        value = readerCount.decrementAndGet();
        if (value == 0) {
            writerSemaphore.release();
        }
        readerCountSemaphore.release();
    }

    @Override
    public void write() throws InterruptedException {
        writerSemaphore.acquire();

        // write something

        writerSemaphore.release();
    }
}
