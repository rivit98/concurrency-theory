package example;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;
import org.jcsp.lang.StandardChannelIntFactory;

public class Main {
    public static void main(String[] args) {
        var factory = new StandardChannelIntFactory();
        var channel = factory.createOne2One();
        CSProcess[] procList = { new Producer(channel), new Consumer(channel) };

        var par = new Parallel(procList);
        par.run();
    }
}

class Producer implements CSProcess {
    private final One2OneChannelInt channel;
    public Producer(One2OneChannelInt channel){
        this.channel = channel;
    }

    @Override
    public void run() {
        int item = (int) (Math.random() * 100 + 1);
        channel.out().write(item);
    }
}

class Consumer implements CSProcess {
    private final One2OneChannelInt channel;
    public Consumer(One2OneChannelInt channel){
        this.channel = channel;
    }


    @Override
    public void run() {
        int item = channel.in().read();
        System.out.println("received: " + item);
    }
}