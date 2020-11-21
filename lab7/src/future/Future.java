package future;

public class Future {
    private Object object;

    public Object get() {
        return object;
    }

    public void set(Object o) {
        object = o;
    }

    public boolean isAvailable(){
        return object != null;
    }
}
