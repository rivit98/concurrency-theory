package zad2;

class Philosopher extends Thread {
    private static final int EATING_TIME = 100;
    private static final int THINKING_TIME = 30;
    private static final int ITERATIONS = 100;

    private final ForkGroup forkGroup;
    private final Integer ID;
    private long starvingTime = 0;

    public Philosopher(int i, ForkGroup forkGroup) {
        this.ID = i;
        this.forkGroup = forkGroup;
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
        synchronized (forkGroup){
            long before = System.nanoTime();

            while (!forkGroup.isAvailable()) {
                wait();
            }
            forkGroup.pickUp();

            long after = System.nanoTime() - before;
            starvingTime += after;

//            System.out.println("eating " + ID);
            action(EATING_TIME);

            forkGroup.putDown();
            notify();
        }
    }

    public long getStarvingTime() {
        return starvingTime;
    }
}
