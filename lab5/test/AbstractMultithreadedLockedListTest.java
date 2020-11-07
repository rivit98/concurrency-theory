import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zad2.ILockedList;
import zad2.LockedList;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractMultithreadedLockedListTest {
    protected ILockedList list;
    protected int INITIAL_SIZE;
    protected ExecutorService executor;
    protected List<Integer> initialData;

    @BeforeEach
    public void setUp() {
        list = new LockedList(10, 10);
        list = createList();

        initialData = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        for (var i : initialData) {
            list.add(i);
        }
        INITIAL_SIZE = initialData.size();

        executor = Executors.newFixedThreadPool(4);
    }

    protected abstract ILockedList createList();

    @Test
    public void multithreadedAddTest() {
        var dataToAdd = List.of(0, 9, 8, 7, 6, 5);

        LinkedList<Runnable> threadList = new LinkedList<>();
        IntStream.range(0, 4).forEach(i -> {
            threadList.add(() -> {
                for (var i1 : dataToAdd) {
                    list.add(i1);
                }
            });
        });

        threadList.forEach(t -> executor.execute(t));
        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        assertEquals(dataToAdd.size() * 4 + INITIAL_SIZE, list.size());
    }

    @Test
    public void multithreadedRemoveTest() {
        var dataToRemove = List.of(0, 9, 8, 7, 6, 5);

        LinkedList<Runnable> threadList = new LinkedList<>();
        IntStream.range(0, 4).forEach(i -> {
            threadList.add(() -> {
                for (var i1 : dataToRemove) {
                    list.remove(i1);
                }
            });
        });

        threadList.forEach(t -> executor.execute(t));
        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        long commonElements =
                initialData
                        .stream()
                        .distinct()
                        .filter(dataToRemove::contains).count();
        assertEquals(INITIAL_SIZE - commonElements, list.size());
    }
}
