package zad2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;


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

class Mandelbrot extends JFrame {
    private final int maxIter;
    private final BufferedImage img;
    private final int width;
    private final int height;
    private long timeOfExecution;

    public Mandelbrot(int max_iter) {
        super("Mandelbrot Set");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.maxIter = max_iter;
        this.width = getWidth();
        this.height = getHeight();
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void run(ExecutorService executorService) {
        var start = System.currentTimeMillis();

        var futures = new LinkedList<Future<Void>>();
        for (int y = 0; y < height; y++) {
            var task = new MandelbrotWorker(maxIter, width, y, img);
            futures.add(executorService.submit(task));
        }

        for (var f : futures) {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        timeOfExecution = System.currentTimeMillis() - start;
    }

    public long getTimeOfExecution() {
        return timeOfExecution;
    }

    public void display() {
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, this);
    }
}

class Main {
    private static final List<Integer> maxItersList = List.of(100, 500, 1000, 5000);
    private static final List<Integer> threadCountList = List.of(2, 4, 10, 20);

    public static void saveResults(String fname, List<String> data) {
        Path out = Paths.get(fname + ".csv");
        try {
            Files.write(out, data, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void newFixedThreadPoolTest() {
        var results = new LinkedList<String>();
        for (var t : threadCountList) {
            for (var i : maxItersList) {
                var mandelbrot = new Mandelbrot(i);
                var executor = Executors.newFixedThreadPool(t);
                mandelbrot.run(executor);
                executor.shutdownNow();
                var time = mandelbrot.getTimeOfExecution();
                results.add(t + "," + i + "," + time);
            }
        }

        saveResults("newFixedThreadPool", results);
    }

    public static void newSingleThreadExecutorTest() {
        var results = new LinkedList<String>();
        for (var i : maxItersList) {
            var mandelbrot = new Mandelbrot(i);
            var executor = Executors.newSingleThreadExecutor();
            mandelbrot.run(executor);
            executor.shutdownNow();
            var time = mandelbrot.getTimeOfExecution();
            results.add(i + "," + time);
        }

        saveResults("newSingleThreadExecutor", results);
    }

    public static void newCachedThreadPoolTest() {
        var results = new LinkedList<String>();
        for (var i : maxItersList) {
            var mandelbrot = new Mandelbrot(i);
            var executor = Executors.newCachedThreadPool();
            mandelbrot.run(executor);
            executor.shutdownNow();
            var time = mandelbrot.getTimeOfExecution();
            results.add(i + "," + time);
        }

        saveResults("newCachedThreadPool", results);
    }

    public static void newWorkStealingPoolTest() {
        var results = new LinkedList<String>();
        for (var t : threadCountList) {
            for (var i : maxItersList) {
                var mandelbrot = new Mandelbrot(i);
                var executor = Executors.newWorkStealingPool(t);
                mandelbrot.run(executor);
                executor.shutdownNow();
                var time = mandelbrot.getTimeOfExecution();
                results.add(t + "," + i + "," + time);
            }
        }

        saveResults("newWorkStealingPool", results);
    }

    public static void main(String[] args) {
        newFixedThreadPoolTest();
        newSingleThreadExecutorTest();
        newCachedThreadPoolTest();
        newWorkStealingPoolTest();
    }
}