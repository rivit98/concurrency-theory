package zad1;

import java.util.concurrent.Executors;

class Main {
    public static void main(String[] args) {
        var mandelbrot = new Mandelbrot(570);
        var executor = Executors.newFixedThreadPool(4);
        mandelbrot.run(executor);
        mandelbrot.display();
    }
}