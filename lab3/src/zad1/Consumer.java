package zad1;

import java.util.Random;

public class Consumer extends Thread {
    private final IBuffer _buf;
    private final int _iters;
    private final Random random = new Random();

    public Consumer(IBuffer _buf, int iters) {
        this._buf = _buf;
        this._iters = iters;
    }

    public void run() {
        for (int i = 0; i < this._iters; ++i) {
            _buf.get();
            try {
                Thread.sleep(random.nextInt(300) + 100);
            } catch (InterruptedException ignored) {

            }
        }
    }
}
