package zad2;

import java.util.ArrayList;

class Main {
    public static void main(String[] args) throws InterruptedException {
        var results = new ArrayList<Integer>();
        for (int i = 0; i < 1000; i++) {
            Counter cnt = new Counter(0);
            var ti = new IThread(cnt);
            var td = new DThread(cnt);

            ti.start();
            td.start();

            ti.join();
            td.join();

            results.add(cnt.value());
        }

        System.out.println(results);
        System.out.println(results.stream().allMatch(v -> v == 0));
    }
}
