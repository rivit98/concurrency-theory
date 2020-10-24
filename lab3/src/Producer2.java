import java.util.Random;
import java.util.stream.IntStream;

class Producer2 extends Thread {
    private final Buffer3 _buf;
    private final Random random = new Random();

    public Producer2(Buffer3 buffer) {
        _buf = buffer;
    }

    public void run() {
        IntStream.range(0, _buf.getBufferSize()).forEach(i -> {
            try {
                Thread.sleep(random.nextInt(300));
            } catch (InterruptedException ignored) {
            }

            _buf.put(i);
        });
    }
}
