import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Counter {
   private int _val;

   public Counter(int n) {
       _val = n;
   }

   public void inc() {
       _val++;
   }

   public void dec() {
       _val--;
   }

   public int value() {
       return _val;
   }
}

class IThread extends Thread {
   public final Counter counter;

   IThread(Counter c) {
       this.counter = c;
   }

   public void run() {
       IntStream.range(0, 100000).forEach(i -> {
           counter.inc();
       });
   }
}

class DThread extends Thread {
   public final Counter counter;

   DThread(Counter c) {
       this.counter = c;
   }

   public void run() {
       IntStream.range(0, 100000).forEach(i -> {
           counter.dec();
       });
   }
}


class Race {
   public static void main(String[] args) throws InterruptedException {
       var results = new ArrayList<Integer>();
       for (int i = 0; i < 100; i++) {
           Counter cnt = new Counter(0);
           var ti = new IThread(cnt);
           var td = new DThread(cnt);

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
