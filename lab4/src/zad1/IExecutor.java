package zad1;

public interface IExecutor {
    void submit(Thread t);

    void shutdownNow();

    void awaitTermination();
}
