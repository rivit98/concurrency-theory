package zad2;

public class LockedList implements ILockedList {
    private final Node sentinel = new Node(null);
    private final int insertTime;
    private final int compareTime;

    public LockedList(int iTime, int cTime) {
        insertTime = iTime * 1000;
        compareTime = cTime * 1000;
    }

    @Override
    public void add(Object o) {
        Node current = sentinel;
        try {
            current.lock();
            Node next = sentinel.next();

            while (next != null) {
                next.lock();

                Node temp = current;
                current = next;
                temp.unlock();

                next = current.next();
            }

            try {
                Thread.sleep(0, insertTime);
            } catch (InterruptedException ignored) {
            }

            current.setNext(new Node(o));
        } finally {
            current.unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        Node prev = sentinel;
        Node current = null;
        try {
            prev.lock();
            current = sentinel.next();
            while (current != null) {
                current.lock();

                try {
                    Thread.sleep(0, compareTime);
                } catch (InterruptedException ignored) {
                }

                if (current.getData().equals(o)) {
                    return true;
                }

                prev.unlock();
                prev = current;
                current = current.next();
            }
        } finally {
            if (current != null) {
                current.unlock();
            }
            prev.unlock();
        }
        return false;
    }

    @Override
    public void remove(Object o) {
        Node prev = sentinel;
        Node current = null;
        try {
            prev.lock();
            current = sentinel.next();
            while (current != null) {
                current.lock();

                try {
                    Thread.sleep(0, compareTime);
                } catch (InterruptedException ignored) {
                }

                if (current.getData().equals(o)) {
                    prev.setNext(current.next());
                    return;
                }

                prev.unlock();
                prev = current;
                current = current.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (current != null) {
                current.unlock();
            }
            prev.unlock();
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
