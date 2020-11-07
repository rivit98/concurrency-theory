package zad2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Node {
    private Node next = null;
    private final Lock lock = new ReentrantLock();
    private Object data;

    public Node(Object data) {
        this.data = data;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public Node next() {
        return next;
    }

    public void setNext(Node n) {
        next = n;
    }

    public Object getData() {
        return data;
    }
}
