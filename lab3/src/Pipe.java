import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Function;
import java.util.stream.Collectors;
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

class PipeThread extends Thread {
    private final Function<Object, Object> transformFunction;
    private final Buffer3 _buf;
    private final Random random = new Random();
    private final int operationID;

    public PipeThread(Buffer3 buffer,
                      int operationIndex,
                      Function<Object, Object> func)
    {
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

public class Pipe {
    public static void main(String[] args) {
        IntStream.range(0, 10).forEach(i -> runCase());
    }

    public static void runCase(){
        var BUFFER_SIZE = 100;

        Function<Integer, Integer> multByTwo = i -> (2 * i);
        Function<Integer, Integer> addSeven = i -> (i + 7);
        Function<Integer, String> duplicateString = i -> (i.toString() + i.toString());
        Function<String, String> addPrefix = s -> ("p" + s);
        Function<String, String> addSufix = s -> (s + "s");

        List<Function> transforms = Arrays.asList(
                multByTwo,
                addSeven,
                duplicateString,
                addPrefix,
                addSufix
        );

        var buffer = new Buffer3(BUFFER_SIZE, transforms.size());

        List<Thread> threadList = new LinkedList<>();
        threadList.add(new Producer2(buffer));
        IntStream.range(0, transforms.size()).forEach(i -> {
            threadList.add(new PipeThread(buffer, i, transforms.get(i)));
        });
        var consumer = new Consumer2(buffer);
        threadList.add(consumer);

        threadList.forEach(Thread::start);
        threadList.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        var calculatedResults = consumer.getResults();
        var expectedResults =
                IntStream
                        .range(0, BUFFER_SIZE)
                        .mapToObj(i -> {
                            List<Object> tempList = new ArrayList<>(transforms.size());
                            tempList.add(i);
                            transforms.forEach(t ->{
                                Object o = tempList.get(tempList.size() - 1);
                                tempList.add(t.apply(o));
                            });

                            return tempList.get(tempList.size() - 1).toString();
                        })
                        .collect(Collectors.toList());

        Collections.sort(calculatedResults);
        Collections.sort(expectedResults);
        var res = calculatedResults.equals(expectedResults);
        System.out.println(res ? "Equals" : "Not equals");
    }
}