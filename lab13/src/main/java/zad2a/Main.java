package zad2a;

import org.jcsp.lang.*;

import java.util.stream.IntStream;

public class Main {
    static final int buffersNum = 10;
    static final int itemsNum = 10000;

    public static void main(String[] args) {
        var channelIntFactory = new StandardChannelIntFactory();
        var prodChannel = channelIntFactory.createOne2One(buffersNum);
        var consChannel = channelIntFactory.createOne2One(buffersNum);
        var bufferChannel = channelIntFactory.createOne2One(buffersNum);

        var procList = new CSProcess[buffersNum + 2];
        procList[0] = new Producer(prodChannel, bufferChannel, itemsNum);
        procList[1] = new Consumer(consChannel, itemsNum);

        IntStream.range(0, buffersNum).forEach(i -> {
            procList[i + 2] =
                    new Buffer(
                            prodChannel[i],
                            consChannel[i],
                            bufferChannel[i]
                    );
        });

        new Parallel(procList).run();
    }
}

class Producer implements CSProcess {
    private final One2OneChannelInt[] out;
    private final One2OneChannelInt[] jeszcze;
    private final int n;

    public Producer(One2OneChannelInt[] out, One2OneChannelInt[] jeszcze, int n) {
        this.out = out;
        this.jeszcze = jeszcze;
        this.n = n;
    }

    public void run() {
        var guards = new Guard[jeszcze.length];
        for (int i = 0; i < out.length; i++) {
            guards[i] = jeszcze[i].in();
        }

        var alt = new Alternative(guards);
        for (int i = 0; i < n; i++) {
            var index = alt.select();
            jeszcze[index].in().read();

            var item = (int) (Math.random() * 100) + 1;
            out[index].out().write(item);
        }
    }
}

class Consumer implements CSProcess {
    private final One2OneChannelInt[] in;
    private final int n;

    public Consumer(final One2OneChannelInt[] in, int n) {
        this.in = in;
        this.n = n;
    }

    public void run() {
        var start = System.currentTimeMillis();
        var guards = new Guard[in.length];
        for (int i = 0; i < in.length; i++)
            guards[i] = in[i].in();

        var alt = new Alternative(guards);
        for (int i = 0; i < n; i++) {
            int index = alt.select();
            int item = in[index].in().read();
        }

        var end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");
        System.exit(0);
    }
}

class Buffer implements CSProcess {
    private final One2OneChannelInt in;
    private final One2OneChannelInt out;
    private final One2OneChannelInt jeszcze;

    public Buffer(One2OneChannelInt in,
                  One2OneChannelInt out,
                  One2OneChannelInt jeszcze) {
        this.out = out;
        this.in = in;
        this.jeszcze = jeszcze;
    }

    public void run() {
        while (true) {
            jeszcze.out().write(0);
            out.out().write(in.in().read());
        }
    }
}