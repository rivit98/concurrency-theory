import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

class PipeThread extends Thread {
    private final Function<Object, Object> transformFunction;
    private final Buffer3 _buf;
    private final Random random = new Random();
    private final int operationID;

    public PipeThread(Buffer3 buffer,
                      int operationIndex,
                      Function<Object, Object> func) {
        transformFunction = func;
        _buf = buffer;
        operationID = operationIndex;
    }

    @Override
    public void run() {
        IntStream.range(0, _buf.getBufferSize()).forEach(i -> {
            try {
                Thread.sleep(random.nextInt(300));
            } catch (InterruptedException ignored) {
            }

            _buf.transform(i, operationID, transformFunction);
        });
    }
}
