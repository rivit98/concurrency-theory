package request;

import servant.Servant;

public class AddRequest implements IMethodRequest {
    private final Servant servant;
    private final Object object;

    public AddRequest(Servant servant, Object o) {
        this.servant = servant;
        this.object = o;
    }

    @Override
    public void call() {
        servant.put(object);
    }

    @Override
    public boolean guard() {
        return !servant.isFull();
    }
}
