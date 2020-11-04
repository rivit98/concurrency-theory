package zad1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class LockLibrary implements ILibrary {
    private final Lock lock = new ReentrantLock();
    private final Condition noWriters = lock.newCondition();
    private final Condition noReadWrite = lock.newCondition();
    private boolean isWriter = false;
    private int readersCount = 0;

    @Override
    public void read() throws InterruptedException {
        lock.lock();
        try {
            while (isWriter) {
                noWriters.await();
            }
            readersCount++;
        } finally {
            lock.unlock();
        }

        // read something

        lock.lock();
        try {
            readersCount--;
            if (readersCount == 0) {
                noReadWrite.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void write() throws InterruptedException {
        lock.lock();
        try {
            while (readersCount > 0 || isWriter) {
                noReadWrite.await();
            }

            isWriter = true;
        } finally {
            lock.unlock();
        }

        // write something

        lock.lock();
        try {
            isWriter = false;
            noReadWrite.signal();
            noWriters.signal();
        } finally {
            lock.unlock();
        }
    }
}
