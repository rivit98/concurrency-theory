package zad3;

class Philosopher extends Thread {
    private static final int EATING_TIME = 30;
    private static final int THINKING_TIME = 90;
    private static final int ITERATIONS = 50;

    private final Fork f1;
    private final Fork f2;
    private final Integer ID;
    private long starvingTime = 0;
    private final Arbiter arbiter;

    public Philosopher(int i, Arbiter arbiter, Fork f1, Fork f2) {
        this.ID = i;
        this.f1 = f1;
        this.f2 = f2;
        this.arbiter = arbiter;
    }

    @Override
    public void run() {
        for (int i = 0; i < ITERATIONS; i++) {
            think();
            try {
                eat();
            } catch (InterruptedException exception) {
                break;
            }
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

    public void eat() throws InterruptedException {
        long before = System.currentTimeMillis();

        arbiter.acquireForks();
        f1.acquire();
        f2.acquire();

        long after = System.currentTimeMillis() - before;
        starvingTime += after;
//        System.out.println("eating " + ID);
        action(EATING_TIME);

        f2.release();
        f1.release();
        arbiter.releaseForks();
    }

    public long getStarvingTime() {
        return starvingTime;
    }
}
