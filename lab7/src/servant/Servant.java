package servant;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Servant {
    private final Queue<Integer> queue = new LinkedList<>();

    private final int capacity;

    public Servant(int capacity) {
        this.capacity = capacity;
    }

    public void put(List<Integer> data){
        queue.addAll(data);
    }

    public List<Integer> get(int elements){
        List<Integer> out = new LinkedList<>();
        while(elements-- > 0){
           out.add(queue.remove());
        }

        return out;
    }

    public int size(){
        return queue.size();
    }

    public int getCapacity() {
        return capacity;
    }
}
