package zad1;

class Reader extends Thread {
    private final ILibrary library;
    private final int iters;

    public Reader(ILibrary library, int iters) {
        super();
        this.library = library;
        this.iters = iters;
    }

    @Override
    public void run() {
        for (int i = 0; i < iters; i++) {
            try {
                library.read();
            } catch (InterruptedException exception) {
                break;
            }
        }
    }
}
