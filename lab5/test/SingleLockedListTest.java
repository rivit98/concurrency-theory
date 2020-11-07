import zad2.ILockedList;
import zad2.SingleLockedList;

public class SingleLockedListTest extends AbstractLockedListTest {
    @Override
    protected ILockedList createList() {
        return new SingleLockedList(0, 0);
    }
}
