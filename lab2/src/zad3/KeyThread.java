package zad3;

class KeyThread extends Thread {
    private final SharedResource sr;

    KeyThread(SharedResource sr) {
        this.sr = sr;
    }

    public void run() {
        sr.getKey();
    }
}
