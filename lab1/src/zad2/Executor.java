package zad2;

import java.util.ArrayList;
import java.util.List;

class Executor {
    private final List<MyThread> threads;
    private int finishedNum = 0;
    private final int SWITCH_TIME = 10;

    public Executor() {
        threads = new ArrayList<>();
    }

    public void addThread(MyThread t) {
        threads.add(t);
    }

    public void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        do {
            finishedNum = 0;
            threads.forEach(current -> {
                if (current.isFinished()) {
                    finishedNum++;
                    return;
                }

                if (current.isStarted()) {
                    current.interrupt(); // wake up thread
                } else {
                    current.setStarted(true);
                    current.start(); // start thread
                }

                delay(SWITCH_TIME); // give time for thread to operate
                current.sleep(SWITCH_TIME * threads.size() * 3); //suspend current thread
                delay(SWITCH_TIME); //give time for thread to sleep
            });

        } while (finishedNum != threads.size());

        threads.forEach(t -> { //let threads finish/close
            try {
                t.join();
            } catch (Exception ignored) {

            }
        });
    }

    public void notifyAboutFinished(MyThread thread) {
        thread.setFinished(true);
        this.finishedNum++;
    }
}
