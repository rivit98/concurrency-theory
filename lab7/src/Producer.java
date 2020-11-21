import proxy.BufferProxy;

import java.util.concurrent.ThreadLocalRandom;

public class Producer extends Thread {
    private final BufferProxy proxy;
    private final int totalNumberToPut;

    public Producer(BufferProxy proxy, int totalNumberToGet) {
        this.proxy = proxy;
        this.totalNumberToPut = totalNumberToGet;
    }

    @Override
    public void run(){
        var random = ThreadLocalRandom.current();

        for (int i = 0; i < totalNumberToPut; i++) {
            var data = random.nextInt(100);
            proxy.put(data);

            System.out.println("Producer " + Thread.currentThread().getId() + " added: " + data);

            try {
                sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
