package zad2;

public class ForkGroup {
    private final Fork f1;
    private final Fork f2;

    public ForkGroup(Fork f1, Fork f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    public boolean isAvailable() {
        return f1.isAvailable() && f2.isAvailable();
    }

    public void pickUp() {
        f1.pickUp();
        f2.pickUp();
    }

    public void putDown() {
        f1.putDown();
        f2.putDown();
    }
}
