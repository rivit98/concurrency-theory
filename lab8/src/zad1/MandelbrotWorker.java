package zad1;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

class MandelbrotWorker implements Callable<Void> {
    private final double ZOOM = 150;

    private final int iters;
    private final int w;
    private final int current_h;
    private final BufferedImage img;

    public MandelbrotWorker(int iters, int w, int current_h, BufferedImage img) {
        this.iters = iters;
        this.w = w;
        this.current_h = current_h;
        this.img = img;
    }

    @Override
    public Void call() {
        double cY = (current_h - 300) / ZOOM;
        for (int x = 0; x < w; x++) {
            double zy = 0;
            double zx = 0;
            double cX = (x - 400) / ZOOM;
            int iter = iters;
            while (zx * zx + zy * zy < 4 && iter > 0) {
                double tmp = zx * zx - zy * zy + cX;
                zy = 2.0 * zx * zy + cY;
                zx = tmp;
                iter--;
            }
            img.setRGB(x, current_h, iter | (iter << 8));
        }
        return null;
    }
}
