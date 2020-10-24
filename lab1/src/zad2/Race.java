package zad2;

import java.util.ArrayList;
import java.util.Arrays;

class Race {
    public static void main(String[] args) {
        var results = new ArrayList<Integer>();

        for (int i = 0; i < 100; i++) {
            Executor e = new Executor();
            Counter cnt = new Counter(0);
            e.addThread(new IThread(e, cnt));
            e.addThread(new DThread(e, cnt));

            e.start();

            results.add(cnt.value());
        }

        System.out.println(Arrays.toString(results.toArray()));
        var res = results.stream().allMatch(i -> i == 0);
        System.out.println("All elements are zero? " + res);
    }
}
