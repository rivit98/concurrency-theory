import proxy.BufferProxy;

import java.util.concurrent.ThreadLocalRandom;

public class Consumer extends Thread {
    private final BufferProxy proxy;
    private final int totalNumberToGet;

    public Consumer(BufferProxy proxy, int totalNumberToGet) {
        this.proxy = proxy;
        this.totalNumberToGet = totalNumberToGet;
    }

    @Override
    public void run(){
        var random = ThreadLocalRandom.current();
        for (int i = 0; i < totalNumberToGet; i++) {
            var future = proxy.get();
            while(!future.isAvailable()){
                try{
                    sleep(20);
                }catch (InterruptedException ignored){
                }
            }

            System.out.println("Consumer " + Thread.currentThread().getId() + " got: " + future.get());

            try {
                sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }
}