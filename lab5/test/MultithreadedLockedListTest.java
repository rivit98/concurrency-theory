import zad2.ILockedList;
import zad2.SingleLockedList;

public class MultithreadedLockedListTest extends AbstractMultithreadedLockedListTest {
    @Override
    protected ILockedList createList() {
        return new SingleLockedList(10, 10);
    }
}
