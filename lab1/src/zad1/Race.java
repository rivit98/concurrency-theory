package zad1;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


class Race {
    public static void main(String[] args) throws InterruptedException {
        var results = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            zad1.Counter cnt = new zad1.Counter(0);
            var ti = new zad1.IThread(cnt);
            var td = new zad1.DThread(cnt);

            ti.start();
            td.start();

            ti.join();
            td.join();

            results.add(cnt.value());
        }

        System.out.println(Arrays.toString(results.toArray()));
        saveToFile("./hist1.txt", results);
    }


    public static void saveToFile(String fname, List<Integer> arr) {
        String collect = arr.stream().map(Object::toString).collect(Collectors.joining(","));
        try (
                var writer = new FileWriter(fname);
        ) {
            writer.write(collect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
