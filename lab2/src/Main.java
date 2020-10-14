import java.util.ArrayList;
import java.util.stream.IntStream;

class Main {
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

        System.out.println(results);
        System.out.println(results.stream().allMatch(v -> v == 0));
    }
}

class Main2 {
    public static void main(String[] args) {
        int keysNum = 3;
        SharedResource sr = new SharedResource(keysNum);

        var threadList = new ArrayList<KeyThread>();
        IntStream.range(0, 10).forEach(x -> threadList.add(new KeyThread(sr)));
        threadList.forEach(Thread::start);

        threadList.forEach(t -> {
            try{
                t.join();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}