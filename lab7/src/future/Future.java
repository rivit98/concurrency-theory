package future;

import java.util.List;

public class Future {
    private List<Integer> object;
    private boolean done = false;

    public List<Integer> get() {
        return object;
    }

    public void set(List<Integer> o) {
        object = o;
        complete();
    }

    public synchronized void complete(){
        done = true;
        notify();
    }

    public boolean isCompleted(){
        return done;
    }

    public synchronized void await(){
        while(!isCompleted()){
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
