package zad2;

public class BinarySemaphore {
    private boolean available;
    private int waitingThreads;

    public BinarySemaphore() {
        this.available = true;
        this.waitingThreads = 0;
    }

    public synchronized void P() { //acquire
        this.waitingThreads++;
        if (!this.available) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.waitingThreads--;
        this.available = false;
    }

    public synchronized void V() { //release
        if (this.waitingThreads > 0) {
            this.notify();
        }
        this.available = true;
    }
}
