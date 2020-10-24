import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.IntStream;

class Producer extends Thread {
    private final IBuffer _buf;
    private final int _iters;
    private final Random random = new Random();

    public Producer(IBuffer _buf, int iters) {
        this._buf = _buf;
        this._iters = iters;
    }

    public void run() {
        for (int i = 0; i < this._iters; ++i) {
            _buf.put(i);
            try {
                Thread.sleep(random.nextInt(300));
            } catch (InterruptedException ignored) {

            }
        }
    }
}

class Consumer extends Thread {
    private final IBuffer _buf;
    private final int _iters;
    private final Random random = new Random();

    public Consumer(IBuffer _buf, int iters) {
        this._buf = _buf;
        this._iters = iters;
    }

    public void run() {
        for (int i = 0; i < this._iters; ++i) {
            _buf.get();
            try {
                Thread.sleep(random.nextInt(300) + 100);
            } catch (InterruptedException ignored) {

            }
        }
    }
}

class Buffer implements IBuffer {
    private final LinkedList<Integer> _buf = new LinkedList<>();
    private final int MAX_ITEMS_IN_BUFFER = 10;

    @Override
    public int getProductsNum(){
        return this._buf.size();
    }

    @Override
    public synchronized void put(int i) {
        while(this._buf.size() >= MAX_ITEMS_IN_BUFFER){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this._buf.add(i);
//        System.out.println("Producing " + i);
        this.notifyAll();
    }

    @Override
    public synchronized int get() {
        while(this._buf.isEmpty()){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int v = this._buf.removeFirst();
//        System.out.println("Consuming " + v);
        this.notifyAll();
        return v;
    }
}

public class PKmon {
    public static void main(String[] args) {
        runCase(1, 1); // n1 = n2 = 1
        runCase(2,2);
        runCase(5, 2); // n1 > n2
        runCase(5, 5); // n1 = n2 = 5
        runCase(2, 5); // n1 < n2
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

        var buffer = new Buffer();
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