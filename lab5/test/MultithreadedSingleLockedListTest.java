import zad2.ILockedList;
import zad2.LockedList;

public class MultithreadedSingleLockedListTest extends AbstractMultithreadedLockedListTest{
    @Override
    protected ILockedList createList() {
        return new LockedList(10, 10);
    }
}

