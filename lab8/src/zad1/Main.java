package zad1;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.concurrent.*;

class Main {
    public static void main(String[] args) {
        var mandelbrot = new Mandelbrot(570);
        var executor = Executors.newFixedThreadPool(4);
        mandelbrot.run(executor);
        mandelbrot.display();
    }
}