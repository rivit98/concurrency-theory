package zad1;

import java.util.LinkedList;
import java.util.List;

public class MyExecutor implements IExecutor {
    private final List<Thread> workers = new LinkedList<>();

    @Override
    public void submit(Thread t){
        workers.add(t);
    }

    @Override
    public void shutdownNow(){
        workers.forEach(Thread::interrupt);
    }

    @Override
    public void awaitTermination() {
        workers.forEach(Thread::start);

        workers.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException ignored) {
            }
        });
    }
}
