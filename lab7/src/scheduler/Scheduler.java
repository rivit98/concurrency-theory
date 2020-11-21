package scheduler;

import request.IMethodRequest;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Scheduler extends Thread{
    private final Queue<IMethodRequest> activationQueue = new ConcurrentLinkedQueue<>();

    public void insert(IMethodRequest methodRequest){
        activationQueue.add(methodRequest);
    }

    @Override
    public void run(){
        while(true){
            var item = activationQueue.poll();
            if (item != null) {
                if (item.guard()) {
                    item.call();
                }else{
                    activationQueue.add(item);
                }
            }
        }
    }
}
