package zad3;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

class Main {
    public static Integer PHILOSOPHERS_NUM = 5;

    public static void main(String[] args) {
        var times = testCaseWrapper();
        var stringBuilder = new StringBuilder();
        for (var t : times) {
            stringBuilder.append(t);
            stringBuilder.append(",");
        }

        System.out.println(stringBuilder.toString());

        Path out = Paths.get("philosophers_arbiter.csv");
        try {
            Files.writeString(out, stringBuilder.toString(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long[] testCaseWrapper() {
        var times = new LinkedList<long[]>();

        IntStream.range(0, 50).forEach(i -> {
            var results = testCase();
            times.add(results);
        });

        return Arrays.stream(
                times
                        .stream()
                        .reduce(new long[PHILOSOPHERS_NUM], (sums, r) -> {
                            IntStream.range(0, PHILOSOPHERS_NUM).forEach(i -> {
                                sums[i] += r[i];
                            });
                            return sums;
                        }))
                .map(v -> v / times.size())
                .toArray();
    }

    public static long[] testCase() {
        var philosophers = new Philosopher[PHILOSOPHERS_NUM];
        var forks = new Fork[philosophers.length];
        var arbiter = new Arbiter();

        IntStream.range(0, PHILOSOPHERS_NUM).forEach(i -> {
            forks[i] = new Fork();
        });

        IntStream.range(0, PHILOSOPHERS_NUM).forEach(i -> {
            philosophers[i] = new Philosopher(i, arbiter, forks[i], forks[(i + 1) % forks.length]);
        });

        var executor = Executors.newFixedThreadPool(5);
        Arrays.stream(philosophers).forEach(executor::submit);
        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }

        var results = new long[PHILOSOPHERS_NUM];
        IntStream.range(0, PHILOSOPHERS_NUM).forEach(i -> {
            results[i] = philosophers[i].getStarvingTime();
        });
        return results;
    }
}