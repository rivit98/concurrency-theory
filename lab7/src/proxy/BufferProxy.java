package proxy;

import future.Future;
import request.AddRequest;
import request.GetRequest;
import scheduler.Scheduler;
import servant.Servant;

public class BufferProxy {
    private final Servant servant;
    private final Scheduler scheduler = new Scheduler();

    public BufferProxy(int bufferSize) {
        this.servant = new Servant(bufferSize);

        scheduler.setDaemon(true);
        scheduler.start();
    }

    public void put(Object o) {
        scheduler.insert(new AddRequest(servant, o));
    }

    public Future get() {
        var future = new Future();
        scheduler.insert(new GetRequest(future, servant));
        return future;
    }
}
