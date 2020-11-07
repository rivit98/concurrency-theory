package zad2;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


public class Main {
    public static void main(String[] args) {
        var insertTime = List.of(10, 500, 999);
        var compareTime = List.of(10, 500, 999);

        var results = new LinkedList<String>();
        for (var i : insertTime) {
            for (var c : compareTime) {
                    var fineGrainedList = new LockedList(i, c);
                    var singleLockedList = new SingleLockedList(i, c);
                    var avg1 = testCaseWrapper(fineGrainedList);
                    var avg2 = testCaseWrapper(singleLockedList);

                    System.out.println("insertTime: " + i + "ns, compareTime: " + c + "ns | avg1: " + avg1 + "ms," + " avg2: " + avg2 + "ms");
                    results.add(i + ", " + c + ", " + round(avg1, 2) + ", " + round(avg2, 2));
            }
        }

        Path out = Paths.get("results2.csv");
        try {
            Files.write(out, results, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double round(double v, int places){
        v = Math.round(v * Math.pow(10, places));
        v = v / Math.pow(10, places);
        return v;
    }

    public static double testCaseWrapper(ILockedList list) {
        var times = new LinkedList<Long>();

        IntStream.range(0, 10).forEach(i -> {
            long before = System.currentTimeMillis();

            testCase(list);

            long diff = System.currentTimeMillis() - before;
            times.add(diff);
        });

        return times.stream().mapToLong(i -> i).average().getAsDouble();
    }

    private static void testCase(ILockedList list) {
        var random = new Random();
        final int OPERATIONS_NUM = 30;
        final int THREAD_NUM = 2;

        var threadList = new LinkedList<Thread>();
        IntStream.range(0, THREAD_NUM).forEach(i -> {
            var op = new LinkedList<Integer>();
            IntStream.range(0, OPERATIONS_NUM).forEach(j -> {
                op.add(random.nextInt(3));
            });
            threadList.add(new Worker(list, op));
        });

        var executor = Executors.newFixedThreadPool(THREAD_NUM);
        threadList.forEach(executor::submit);
        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        list.clear();
    }
}
