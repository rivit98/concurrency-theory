package zad1;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public interface IExecutor {
    void submit(Thread t);

    void shutdownNow();

    void awaitTermination();

    CountDownLatch getLatch() throws Exception;
}
