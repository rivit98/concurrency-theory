package request;

import future.Future;
import servant.Servant;

public class GetRequest implements IMethodRequest {
    private final Future future;
    private final Servant servant;

    public GetRequest(Future future, Servant servant) {
        this.servant = servant;
        this.future = future;
    }

    @Override
    public void call() {
        future.set(servant.get());
    }

    @Override
    public boolean guard() {
        return !servant.isEmpty();
    }
}
