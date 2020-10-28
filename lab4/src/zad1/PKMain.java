package zad1;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


public class PKMain {
    public static void main(String[] args) {
        final int m = 10; // producers
        final int n = 10; // consumers
        final int M = 100; // max buffer size

        IntStream.range(0, 100).forEach(i ->{
            testCase(m, n, M);
        });
    }

    public static void testCase(int m, int n, int M){
        final var random = new Random();
        final var buffer = new Buffer(M);
        final var pool = Executors.newFixedThreadPool(n + m);

        IntStream.range(0, m).forEach(i ->
                pool.submit(
                        new Producer(buffer, Math.max(3, random.nextInt(20)))
                ));
        IntStream.range(0, n).forEach(i ->
                pool.submit(
                        new Consumer(buffer,  Math.max(3, random.nextInt(20)))
                ));

        pool.shutdown();
        try{
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
