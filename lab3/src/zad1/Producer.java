package zad1;

import java.util.Random;

public class Producer extends Thread {
    private final IBuffer _buf;
    private final int _iters;
    private final Random random = new Random();

    public Producer(IBuffer _buf, int iters) {
        this._buf = _buf;
        this._iters = iters;
    }

    public void run() {
        for (int i = 0; i < this._iters; ++i) {
            _buf.put(i);
            try {
                Thread.sleep(random.nextInt(300));
            } catch (InterruptedException ignored) {

            }
        }
    }
}
