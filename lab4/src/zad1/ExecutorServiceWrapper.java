package zad1;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceWrapper implements IExecutor {
    private final ExecutorService executorService;
    private final List<Thread> workers = new LinkedList<>();
    private final CountDownLatch countDownLatch;

    public ExecutorServiceWrapper(int threadNum) {
        executorService = Executors.newFixedThreadPool(threadNum);
        countDownLatch = new CountDownLatch(threadNum);
    }



    @Override
    public void submit(Thread t) {
        workers.add(t);
    }

    @Override
    public void shutdownNow() {
//        System.out.println("shutdownNow");
        executorService.shutdownNow();
    }

    @Override
    public void awaitTermination() {
        workers.forEach(executorService::submit);

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public CountDownLatch getLatch() {
        return countDownLatch;
    }
}
