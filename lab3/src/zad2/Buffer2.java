package zad2;

import utils.BinarySemaphore;
import utils.CountingSemaphore;
import zad1.IBuffer;

import java.util.LinkedList;

class Buffer2 implements IBuffer {
    private final LinkedList<Integer> _buf = new LinkedList<>();
    private final CountingSemaphore notEmpty;
    private final CountingSemaphore slotsAvailable;
    private final BinarySemaphore mutex;
    private final int MAX_ITEMS_IN_BUFFER = 10;

    public Buffer2() {
        this.slotsAvailable = new CountingSemaphore(MAX_ITEMS_IN_BUFFER);
        this.notEmpty = new CountingSemaphore(0);
        this.mutex = new BinarySemaphore(true);
    }

    @Override
    public int getProductsNum() {
        return this._buf.size();
    }

    @Override
    public void put(int i) {
        this.slotsAvailable.P();

        this.mutex.P();
        this._buf.add(i);
        this.mutex.V();

//        System.out.println("Producing " + i);
        this.notEmpty.V();
    }

    @Override
    public int get() {
        this.notEmpty.P();

        this.mutex.P();
        int v = this._buf.removeFirst();
        this.mutex.V();
//        System.out.println("Consuming " + v);

        this.slotsAvailable.V();
        return v;
    }
}
