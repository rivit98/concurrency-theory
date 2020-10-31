package zad1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


public class PKMain {
    enum MODE {
        MODE_MONITORS,
        MODE_CONCURRENT_LIB
    }

    public static void main(String[] args) {
        var producers = List.of(3, 8, 15);
        var consumers = List.of(3, 8, 15);
        var bufferSizes = List.of(5, 10, 25, 50);
//        var bufferSizes = List.of(5);
//        var producers = List.of(3);
//        var consumers = List.of(8);

        var results = new LinkedList<String>();

        for (var p : producers) {
            for (var c : consumers) {
                for (var bs : bufferSizes) {
                    var avg1 = testCaseWrapper(p, c, bs, MODE.MODE_MONITORS);
                    var avg2 = testCaseWrapper(p, c, bs, MODE.MODE_CONCURRENT_LIB);
//                    var avg1 = 0.0;
//                    var avg2 = 0.0;
//                    testCaseWrapper(p, c, bs, MODE.MODE_MONITORS);
                    System.out.println(p + "p/" + c + "c [" + bs + "]: " + avg1 + "ms / " + avg2 + "ms");
                    results.add(p + ", " + c + ", " + bs + ", " + avg1 + ", " + avg2);
                }
            }
        }

        Path out = Paths.get("results.csv");
        try {
            Files.write(out, results, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double testCaseWrapper(int prodNum, int consNum, int halfBufSize, MODE mode) {
        var times = new LinkedList<Long>();

        IntStream.range(0, 200).forEach(i -> {
//            System.out.println(prodNum + "p/" + consNum + "c [" + halfBufSize + "]");

            long before = System.currentTimeMillis();

            if (mode == MODE.MODE_CONCURRENT_LIB) {
                testConcurrentLibrary(prodNum, consNum, halfBufSize);
            } else {
                testMonitors(prodNum, consNum, halfBufSize);
            }

            long after = System.currentTimeMillis();
            long diff = after - before;
            times.add(diff);

//            System.out.println("===========");
        });

        return times.stream().mapToLong(i -> i).average().getAsDouble();
    }

    public static void testConcurrentLibrary(int m, int n, int M) {
        var buffer = new Buffer2(M, m, n);
        var executor = new ExecutorServiceWrapper(n+m);

        runTasks(m, n, buffer, executor);
    }

    public static void testMonitors(int m, int n, int M) {
        var buffer = new Buffer(M, m, n);
        var executor = new MyExecutor();

        runTasks(m, n, buffer, executor);
    }

    public static void runTasks(int m, int n, IBuffer buffer, IExecutor executor){
        IntStream.range(0, n).forEach(i ->
                executor.submit(
                        new Consumer(executor, buffer, 100)
                ));
        IntStream.range(0, m).forEach(i ->
                executor.submit(
                        new Producer(executor, buffer, 100)
                ));

        executor.awaitTermination();
    }
}
