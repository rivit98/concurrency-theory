package zad3;

import java.util.ArrayList;
import java.util.stream.IntStream;

class Main {
    public static void main(String[] args) {
        int keysNum = 3;
        SharedResource sr = new SharedResource(keysNum);

        var threadList = new ArrayList<KeyThread>();
        IntStream.range(0, 10).forEach(x -> threadList.add(new KeyThread(sr)));
        threadList.forEach(Thread::start);

        threadList.forEach(t -> {
            try {
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}