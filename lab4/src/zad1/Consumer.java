package zad1;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


class Consumer extends Thread {
    private final IBuffer buffer;
    private final int consumeLimit;
    private final int iterations;
    private final Random random = new Random();
    private final IExecutor executor;

    public Consumer(IExecutor ex, IBuffer buf, int iters) {
        iterations = iters;
        buffer = buf;
        consumeLimit = buffer.maxSize() / 2;
        executor = ex;
    }

    public void run() {
        for (int i = 0; i < iterations; i++) {
            var howMany = Math.max(1, random.nextInt(consumeLimit)-1);
            List<Integer> results = new LinkedList<>();
            try {
                buffer.get(results, howMany);
            } catch (InterruptedException exception) {
                break;
            }
        }

        buffer.unregisterConsumer();
        if (!buffer.isAnySideInterested()) {
            executor.shutdownNow();
        }
    }
}
