package zad2;

class Philosopher extends Thread {
    private static final int EATING_TIME = 90;
    private static final int THINKING_TIME = 30;
    private static final int ITERATIONS = 50;

    private final Integer ID;
    private long starvingTime = 0;
    private final Fork left;
    private final Fork right;

    public Philosopher(int i, Fork left, Fork right) {
        this.ID = i;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        int i = 0;
        while (i < ITERATIONS) {
            think();
            eat();
            i++;
        }
    }

    public void action(Integer time) {
        try {
            sleep(time);
        } catch (InterruptedException ignored) {
        }
    }

    public void think() {
//        System.out.println("thinking " + ID);
        action(THINKING_TIME);
    }

    public void eat() {
        boolean success = false;
        long before = System.currentTimeMillis();

        while (!success) {
            if (left.tryAcquire()) {
                if (right.tryAcquire()) {
                    long after = System.currentTimeMillis() - before;
                    starvingTime += after;

//            System.out.println("eating " + ID);
                    action(EATING_TIME);
                    success = true;

                    right.release();
                }
                left.release();
            }
        }
    }

    public long getStarvingTime() {
        return starvingTime;
    }
}
