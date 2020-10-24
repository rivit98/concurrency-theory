package zad3;

import java.util.concurrent.Semaphore;
import java.util.function.Function;
import java.util.stream.IntStream;

class Buffer3 {
    private final Object[] buffer;
    private final int bufferSize;
    private final Semaphore[] semaphores;
    private final int transformOperationsNum;

    public Buffer3(int bufSize, int transformThreads) {
        buffer = new Object[bufSize];
        bufferSize = bufSize;
        transformOperationsNum = transformThreads + 1; // because of consumer
        semaphores = new Semaphore[transformOperationsNum];
        IntStream.range(0, transformOperationsNum).forEach(i -> {
            semaphores[i] = new Semaphore(0);
        });
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void put(int i) {
        buffer[i] = i;
        semaphores[0].release();
    }

    public void transform(int i,
                          int operationIndex,
                          Function<Object, Object> transformFunction
    ) {
        try {
            semaphores[operationIndex].acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        buffer[i] = transformFunction.apply(buffer[i]);

        semaphores[operationIndex + 1].release();
    }

    public Object get(int i) {
        try {
            semaphores[transformOperationsNum - 1].acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return buffer[i];
    }
}
