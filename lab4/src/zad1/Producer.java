package zad1;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;


class Producer extends Thread {
    private final IBuffer buffer;
    private final Random random = new Random();
    private final int produceLimit;
    private final int iterations;
    private final IExecutor executor;


    public Producer(IExecutor ex, IBuffer buf, int iters) {
        iterations = iters;
        buffer = buf;
        produceLimit = buffer.maxSize() / 2;
        executor = ex;
    }

    public void run() {
//        System.out.println(Thread.currentThread().getId() + "p");
//        try {
//            executor.getLatch().countDown();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        for(int i = 0; i < iterations; i++) {
            var howMany = Math.max(1, random.nextInt(produceLimit)-1);
            int[] data = new int[howMany];
            IntStream.range(0, howMany).forEach(j -> {
                data[j] = j;
            });

            try {
                buffer.put(data);
            } catch (InterruptedException exception) {
                break;
            }
        }
        buffer.unregisterProducer();
        if(!buffer.isAnySideInterested()){
            executor.shutdownNow();
        }
    }
}
