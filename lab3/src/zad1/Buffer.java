package zad1;

import java.util.LinkedList;

class Buffer implements IBuffer {
    private final LinkedList<Integer> _buf = new LinkedList<>();
    private final int MAX_ITEMS_IN_BUFFER = 10;

    @Override
    public int getProductsNum() {
        return this._buf.size();
    }

    @Override
    public synchronized void put(int i) {
        while (this._buf.size() >= MAX_ITEMS_IN_BUFFER) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this._buf.add(i);
//        System.out.println("Producing " + i);
        this.notifyAll();
    }

    @Override
    public synchronized int get() {
        while (this._buf.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int v = this._buf.removeFirst();
//        System.out.println("Consuming " + v);
        this.notifyAll();
        return v;
    }
}
