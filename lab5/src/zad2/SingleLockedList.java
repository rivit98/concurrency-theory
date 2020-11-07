package zad2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleLockedList implements ILockedList {

    private final Node sentinel = new Node(null);
    private final int insertTime;
    private final int compareTime;
    private final Lock lock = new ReentrantLock();

    public SingleLockedList(int iTime, int cTime) {
        insertTime = iTime * 1000;
        compareTime = cTime * 1000;
    }

    @Override
    public void add(Object o) {
        lock.lock();

        try {
            Node prev = sentinel;
            Node current = sentinel.next();

            while (current != null) {
                prev = current;
                current = current.next();
            }

            try {
                Thread.sleep(0, insertTime);
            } catch (InterruptedException ignored) {
            }

            prev.setNext(new Node(o));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        lock.lock();
        try {
            Node current = sentinel.next();

            while (current != null) {
                try {
                    Thread.sleep(0, compareTime);
                } catch (InterruptedException ignored) {
                }

                if (current.getData().equals(o)) {
                    return true;
                }

                current = current.next();
            }
        } finally {
            lock.unlock();
        }

        return false;
    }

    @Override
    public void remove(Object o) {
        lock.lock();
        try {
            Node prev = sentinel;
            Node current = sentinel.next();

            while (current != null) {
                try {
                    Thread.sleep(0, compareTime);
                } catch (InterruptedException ignored) {
                }

                if (current.getData().equals(o)) {
                    prev.setNext(current.next());
                    return;
                }

                prev = current;
                current = current.next();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        sentinel.setNext(null);
    }

    @Override
    public int size() {
        int size = 0;
        Node current = sentinel;
        while(current != null){
            size++;
            current = current.next();
        }

        return size - 1;
    }
}
