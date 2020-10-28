package zad1;

import java.util.Random;
import java.util.stream.IntStream;

class Consumer extends Thread {
    private final Buffer buffer;
    private final int consumeLimit;
    private final int iterations;
    private final Random random = new Random();

    public Consumer(Buffer buf, int iters) {
        iterations = iters;
        buffer = buf;
        consumeLimit = buffer.maxSize() / 2;
        buffer.registerConsumer();
    }

    public void run() {
        IntStream.range(0, iterations).forEach(i -> {
            var howMany = Math.max(1, random.nextInt(consumeLimit));
//            System.out.println("Want to consume " + howMany);
            var res = buffer.get(howMany);
        });

        buffer.unregisterConsumer();
    }
}
