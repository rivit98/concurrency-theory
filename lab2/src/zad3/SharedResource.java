package zad3;

import zad3.CountingSemaphore;

import java.util.concurrent.ThreadLocalRandom;

class SharedResource {
    private final CountingSemaphore semaphore;

    SharedResource(int k){
        this.semaphore = new CountingSemaphore(k);
    }

    public void getKey(){
        semaphore.P();

        System.out.println("Thread "
                + Thread.currentThread().getId()
                + " acquired key. "
        );

        try {
            // imitate doing something
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 1000));
        } catch (InterruptedException ignored) {

        }

        System.out.println("Thread "
                + Thread.currentThread().getId()
                + " returned key. "
        );

        semaphore.V();
    }
}

