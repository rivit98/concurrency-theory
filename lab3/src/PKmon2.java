import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.IntStream;

class Buffer2 {
    private final LinkedList<Integer> _buf = new LinkedList<>();
    private final BinarySemaphore _bufSemaphore = new BinarySemaphore();
    private final int MAX_ITEMS_IN_BUFFER = 10;

    public int getProductsNum(){
        return this._buf.size();
    }

    public synchronized void put(int i) {
        this._bufSemaphore.P();

        this._buf.add(i);
        System.out.println("Producing " + i);

        this._bufSemaphore.V();
    }

    public synchronized int get() {
        this._bufSemaphore.P();

        int v = this._buf.removeFirst();
        System.out.println("Consuming " + v);

        this._bufSemaphore.V();
        return v;
    }
}

public class PKmon2 {
    public static void main(String[] args) throws InterruptedException {

    }
}