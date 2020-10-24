import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

class Consumer2 extends Thread {
    private final Buffer3 _buf;
    private final Random random = new Random();
    private final List<String> results = new LinkedList<>();

    public Consumer2(Buffer3 buffer) {
        _buf = buffer;
    }

    public void run() {
        IntStream.range(0, _buf.getBufferSize()).forEach(i -> {
            try {
                Thread.sleep(random.nextInt(300));
            } catch (InterruptedException ignored) {
            }

            Object obj = _buf.get(i);
//            System.out.println(obj.toString());
            results.add(obj.toString());
        });
    }

    public List<String> getResults() {
        return results;
    }
}
