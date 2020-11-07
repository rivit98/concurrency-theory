package zad2;

import java.util.List;
import java.util.Random;

class Worker extends Thread {
    private final ILockedList list;
    private final List<Integer> operations;
    private final Random random = new Random();

    public Worker(ILockedList l, List<Integer> op) {
        list = l;
        operations = op;
    }

    @Override
    public void run() {
        for (var op : operations) {
            switch (op) {
                case 0 -> // add
                        list.add(random.nextInt(20));
                case 1 -> // remove
                        list.remove(random.nextInt(20));
                case 2 -> // contains
                        list.contains(random.nextInt(20));
            }

        }
    }
}
