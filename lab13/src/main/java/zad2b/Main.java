package zad2b;

import org.jcsp.lang.*;

import java.util.stream.IntStream;

public class Main {
    static final int buffersNum = 10;
    static final int itemsNum = 10000;

    public static void main(String[] args) {
        var channelIntFactory = new StandardChannelIntFactory();
        var channels = channelIntFactory.createOne2One(buffersNum + 1);

        var procList = new CSProcess[buffersNum + 2];
        procList[0] = new Producer(channels[0], itemsNum);
        procList[1] = new Consumer(channels[buffersNum], itemsNum);

        IntStream.range(0, buffersNum).forEach(i -> {
            procList[i + 2] =
                    new Buffer(
                            channels[i],
                            channels[i + 1]
                    );
        });

        new Parallel(procList).run();
    }
}

class Producer implements CSProcess {
    private final One2OneChannelInt out;
    private final int n;

    public Producer(One2OneChannelInt out, int n) {
        this.out = out;
        this.n = n;
    }

    public void run() {
        for (int i = 0; i < n; i++) {
            var item = (int) (Math.random() * 100) + 1;
            out.out().write(item);
        }
    }
}

class Consumer implements CSProcess {
    private final One2OneChannelInt in;
    private final int n;

    public Consumer(final One2OneChannelInt in, int n) {
        this.in = in;
        this.n = n;
    }

    public void run() {
        var start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            int item = in.in().read();
        }

        var end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");
        System.exit(0);
    }
}

class Buffer implements CSProcess {
    private final One2OneChannelInt in;
    private final One2OneChannelInt out;

    public Buffer(One2OneChannelInt in,
                  One2OneChannelInt out) {
        this.out = out;
        this.in = in;
    }

    public void run() {
        while (true) {
            out.out().write(in.in().read());
        }
    }
}