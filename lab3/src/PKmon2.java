import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.IntStream;

class Buffer2 implements IBuffer {
    private final LinkedList<Integer> _buf = new LinkedList<>();
    private final CountingSemaphore notEmpty;
    private final CountingSemaphore slotsAvailable;
    private final BinarySemaphore mutex;
    private final int MAX_ITEMS_IN_BUFFER = 10;

    public Buffer2() {
        this.slotsAvailable = new CountingSemaphore(MAX_ITEMS_IN_BUFFER);
        this.notEmpty = new CountingSemaphore(0);
        this.mutex = new BinarySemaphore(true);
    }

    @Override
    public int getProductsNum(){
        return this._buf.size();
    }

    @Override
    public void put(int i) {
        this.slotsAvailable.P();

        this.mutex.P();
        this._buf.add(i);
        this.mutex.V();

//        System.out.println("Producing " + i);
        this.notEmpty.V();
    }

    @Override
    public int get() {
        this.notEmpty.P();

        this.mutex.P();
        int v = this._buf.removeFirst();
        this.mutex.V();
//        System.out.println("Consuming " + v);

        this.slotsAvailable.V();
        return v;
    }
}

public class PKmon2 {
    public static void main(String[] args) {
        runCase(1, 1); // n1 = n2 = 1
        runCase(5, 2); // n1, n2 > 1
        runCase(2, 5); // n1, n2 > 1
    }

    public static void runCase(int producersNum, int consumersNum){
        System.out.println(
                "Producers: " + producersNum +
                        " | Consumers: " + consumersNum
        );
        var lcm = lcm(producersNum, consumersNum);
        var products = lcm * 10;
        var producerIterations = products / producersNum;
        var consumerIterations = products / consumersNum;

        System.out.println("Each producer will produce "
                + producerIterations + " products");
        System.out.println("Each consumer will consume "
                + consumerIterations + " products");

        var buffer = new Buffer2();
        var threadList = new ArrayList<Thread>();
        IntStream.range(0, producersNum)
                .forEach(i -> threadList.add(
                        new Producer(buffer, producerIterations)
                ));
        IntStream.range(0, consumersNum)
                .forEach(i -> threadList.add(
                        new Consumer(buffer, consumerIterations)
                ));
        threadList.forEach(Thread::start);

        threadList.forEach(t -> {
            try{
                t.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });

        System.out.println("All threads finished!");
        System.out.println("Number of products in queue: " + buffer.getProductsNum());
        System.out.println();
    }

    private static int lcm(int a, int b) {
        return a * (b / gcd(a, b));
    }

    private static int gcd(int a, int b) {
        while (b > 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}