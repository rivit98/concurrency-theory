package zad1;

import java.util.Random;
import java.util.stream.IntStream;

class Producer extends Thread {
    private final Buffer buffer;
    private final Random random = new Random();
    private final int produceLimit;
    private final int iterations;


    public Producer(Buffer buf, int iters) {
        iterations = iters;
        buffer = buf;
        produceLimit = buffer.maxSize() / 2;
        buffer.registerProducer();
    }

    public void run() {
        IntStream.range(0, iterations).forEach(i -> {
            var howMany = Math.max(1, random.nextInt(produceLimit));
            int[] data = new int[howMany];
            IntStream.range(0, howMany).forEach(j -> {
                data[j] = random.nextInt(20);
            });

            buffer.put(data);
        });

        buffer.unregisterProducer();
    }
}
