package zad2;

class Fork {
    private Boolean available = true;

    public boolean isAvailable() {
        return available;
    }

    public void pickUp() {
        available = false;
    }

    public void putDown() {
        available = true;
    }
}
