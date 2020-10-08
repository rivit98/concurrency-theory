import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Counter {
    private int _val;

    public Counter(int n) {
        _val = n;
    }

    public void inc() {
        _val++;
    }

    public void dec() {
        _val--;
    }

    public int value() {
        return _val;
    }
}

class MyThread extends Thread{
    public final Counter counter;
    protected final Executor executor;
    private boolean isStarted = false;
    private boolean isFinished = false;
    protected int sleepingTime = 0;

    MyThread(Executor e, Counter c) {
        this.counter = c;
        this.executor = e;
    }

    public void sleep(int time) {
        this.sleepingTime = time;
    }

    public void wakeup(){
        this.sleepingTime = 0;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}

class IThread extends MyThread {
    IThread(Executor e, Counter c) {
        super(e, c);
    }

    public void run() {
        IntStream.range(0, 100000).forEach(i -> {
            if (this.sleepingTime > 0) {
                try {
                    Thread.sleep(this.sleepingTime);
                } catch (InterruptedException e) {
                    this.wakeup();
                }
            }

            counter.inc();
        });
        this.executor.notifyAboutFinished(this);
    }
}

class DThread extends MyThread {
    DThread(Executor e, Counter c) {
        super(e, c);
    }

    public void run() {
        IntStream.range(0, 100000).forEach(i -> {
            if (this.sleepingTime > 0) {
                try {
                    Thread.sleep(this.sleepingTime);
                } catch (InterruptedException e) {
                    this.wakeup();
                }
            }

            counter.dec();
        });
        this.executor.notifyAboutFinished(this);
    }
}

class Executor{
    private final List<MyThread> threads;
    private int finishedNum = 0;
    private final int SWITCH_TIME = 10;

    public Executor(){
        threads = new ArrayList<>();
    }

    public void addThread(MyThread t){
        threads.add(t);
    }

    public void delay(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(){
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

        threads.forEach(t ->{ //let threads finish/close
            try{
                t.join();
            }catch (Exception ignored){

            }
        });
    }

    public void notifyAboutFinished(MyThread thread){
        thread.setFinished(true);
        this.finishedNum++;
    }
}

class Race {
    public static void main(String[] args) {
        var results = new ArrayList<Integer>();

        for (int i = 0; i < 100; i++) {
            Executor e = new Executor();
            Counter cnt = new Counter(0);
            e.addThread(new IThread(e, cnt));
            e.addThread(new DThread(e, cnt));

            e.start();

            results.add(cnt.value());
        }

        System.out.println(Arrays.toString(results.toArray()));
        var res = results.stream().allMatch(i -> i == 0);
        System.out.println("All elements are zero? " + res);
    }
}
