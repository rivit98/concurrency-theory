package zad2;

public interface ILockedList {
    void add(Object o);

    boolean contains(Object o);

    void remove(Object o);

    void clear();

    int size();
}
