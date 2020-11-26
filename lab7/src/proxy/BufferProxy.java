package proxy;

import future.Future;
import request.AddRequest;
import request.GetRequest;
import scheduler.Scheduler;
import servant.Servant;

import java.util.List;

public class BufferProxy {
    private final Servant servant;
    private final Scheduler scheduler = new Scheduler();

    public BufferProxy(int bufferSize) {
        this.servant = new Servant(bufferSize);

        scheduler.setDaemon(true);
        scheduler.start();
    }

    public Future put(List<Integer> o) {
        var future = new Future();
        scheduler.insert(new AddRequest(future, servant, o));
        return future;
    }

    public Future get(int numberOfElements) {
        var future = new Future();
        scheduler.insert(new GetRequest(future, servant, numberOfElements));
        return future;
    }
}
