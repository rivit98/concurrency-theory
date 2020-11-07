import zad2.ILockedList;
import zad2.LockedList;

public class LockedListTest extends AbstractLockedListTest {
    @Override
    protected ILockedList createList() {
        return new LockedList(0, 0);
    }
}
