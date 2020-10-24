package zad2;

class MyThread extends Thread {
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

    public void wakeup() {
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
