import future.Future;
import proxy.BufferProxy;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Producer extends Thread {
    private final BufferProxy proxy;
    private final int totalNumberToPut;
    private ThreadLocalRandom random;

    public Producer(BufferProxy proxy, int totalNumberToGet) {
        this.proxy = proxy;
        this.totalNumberToPut = totalNumberToGet;
    }

    public void _sleep(int milis){
        try{
            sleep(milis);
        }catch (InterruptedException ignored){
        }
    }

    public List<Integer> generateData(int elems){
        List<Integer> out = new LinkedList<>();
        while(elems > 0){
            out.add(random.nextInt(100));
            elems--;
        }
        return out;
    }

    @Override
    public void run(){
        random = ThreadLocalRandom.current();
        long id = Thread.currentThread().getId();

        List<Future> futureList = new LinkedList<>();
        int elementsLeft = totalNumberToPut;
        while(elementsLeft > 0) {
            int toPut = Math.min(elementsLeft, random.nextInt(5) + 1);
            elementsLeft -= toPut;

            var future = proxy.put(generateData(toPut));
            futureList.add(future);

            _sleep(200);
        }

        System.out.println("Producer " + id + " finished requesting!");
        for(var f : futureList){
            f.await();
        }
        System.out.println("Producer " + id + " finished job!");
    }
}
