import future.Future;
import proxy.BufferProxy;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Consumer extends Thread {
    private final BufferProxy proxy;
    private final int totalNumberToGet;

    public Consumer(BufferProxy proxy, int totalNumberToGet) {
        this.proxy = proxy;
        this.totalNumberToGet = totalNumberToGet;
    }

    public void _sleep(int milis){
        try{
            sleep(milis);
        }catch (InterruptedException ignored){
        }
    }

    @Override
    public void run(){
        long id = Thread.currentThread().getId();

        List<Future> futureList = new LinkedList<>();
        int elementsLeft = totalNumberToGet;
        var random = ThreadLocalRandom.current();
        while(elementsLeft > 0) {
            int toGet = Math.min(elementsLeft, random.nextInt(5) + 1);
            elementsLeft -= toGet;

            var future = proxy.get(toGet);
            futureList.add(future);

            _sleep(100);
        }

        System.out.println("Consumer " + id + " finished requesting!");
        for(var f : futureList){
            f.await();
        }
        System.out.println("Consumer " + id + " finished job!");
    }
}