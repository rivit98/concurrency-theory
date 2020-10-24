package utils;

public class CountingSemaphore {
    private int count;
    private final BinarySemaphore countAccess;
    private final BinarySemaphore canDecrease;

    public CountingSemaphore(int count) {
        this.canDecrease = new BinarySemaphore(true);
        this.countAccess = new BinarySemaphore(true);
        this.count = count;
    }

    public void P() { //acquire
        this.canDecrease.P();

        this.countAccess.P();

        this.count--;
        if (this.count > 0) {
            this.canDecrease.V();
        }

        this.countAccess.V();
    }

    public void V() { //release
        this.countAccess.P();

        this.count++;
        if (this.count == 1) {
            this.canDecrease.V();
        }

        this.countAccess.V();
    }
}
