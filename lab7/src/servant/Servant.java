package servant;

import java.util.LinkedList;
import java.util.Queue;

public class Servant {
    private final Queue<Object> queue = new LinkedList<>();
    private final int capacity;

    public Servant(int capacity) {
        this.capacity = capacity;
    }

    public void put(Object data){
        queue.add(data);
    }

    public Object get(){
        return queue.remove();
    }

    public int size(){
        return queue.size();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

    public boolean isFull(){
        return size() == capacity;
    }
}
