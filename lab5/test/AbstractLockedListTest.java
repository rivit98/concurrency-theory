import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zad2.ILockedList;
import zad2.LockedList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractLockedListTest {
    protected ILockedList list;
    protected int INITIAL_SIZE;

    @BeforeEach
    public void setUp(){
        list = createList();
        var data = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (var i : data) {
            list.add(i);
        }
        INITIAL_SIZE = data.length;
    }

    protected abstract ILockedList createList();

    @Test
    public void sizeTest() {
        assertEquals(INITIAL_SIZE, list.size());
    }

    @Test
    public void addTest() {
        var data = List.of(111, 222, 333, 444);

        for (var i : data) {
            list.add(i);
        }

        assertEquals(INITIAL_SIZE + data.size(), list.size());
    }

    @Test
    public void removeExistingTest() {
        list.remove(1);
        list.remove(5);

        assertEquals(INITIAL_SIZE - 2, list.size());
    }

    @Test
    public void removeNotExistingTest() {
        list.remove(-2);

        assertEquals(INITIAL_SIZE, list.size());
    }

    @Test
    public void removeEmptyTest() {
        var emptyList = new LockedList(0, 0);

        assertEquals(0, emptyList.size());

        emptyList.remove(-2);

        assertEquals(0, emptyList.size());
    }

    @Test
    public void containsTest() {
        var existing = list.contains(1);
        var nonExisting = list.contains(-2);

        assertTrue(existing);
        assertFalse(nonExisting);
    }

    @Test
    public void containsEmptyTest() {
        var emptyList = new LockedList(0, 0);
        var nonExisting = emptyList.contains(1);

        assertFalse(nonExisting);
    }
}
