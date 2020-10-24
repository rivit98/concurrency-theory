package zad2;

public class Counter {
    BinarySemaphore semaphore = new BinarySemaphore();
    private int _val;

    public Counter(int n) {
        _val = n;
    }

    public void inc() {
        this.semaphore.P();
        _val++;
        this.semaphore.V();
    }

    public void dec() {
        this.semaphore.P();
        _val--;
        this.semaphore.V();
    }

    public int value() {
        return _val;
    }
}