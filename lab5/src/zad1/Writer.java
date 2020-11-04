package zad1;

class Writer extends Thread {
    private final ILibrary library;
    private final int iters;

    public Writer(ILibrary library, int iters) {
        super();
        this.library = library;
        this.iters = iters;
    }

    @Override
    public void run() {
        for (int i = 0; i < iters; i++) {
            try {
                library.write();
            } catch (InterruptedException exception) {
                break;
            }
        }
    }
}
