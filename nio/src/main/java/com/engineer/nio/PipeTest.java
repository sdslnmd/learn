package com.engineer.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Random;

/**
 * User: sunluning
 * Date: 12-7-29 下午10:53
 */
public class PipeTest {
    public static void main(String[] args) throws Exception {
        //Wrap a channel around stdout
        WritableByteChannel writableByteChannel = Channels.newChannel(System.out);
        //Start worker and get read end of channel
        ReadableByteChannel readableByteChannel = startWorker(10);
        ByteBuffer buffer = ByteBuffer.allocate(100);
        while (readableByteChannel.read(buffer) > 0) {
            buffer.flip();
            writableByteChannel.write(buffer);
            buffer.clear();
        }
    }

    //this method could return a SocketChannel or FileChannel instance just as easily
    private static ReadableByteChannel startWorker(int reps) throws Exception {
        Pipe pipe=Pipe.open();
        Worker worker=new Worker(pipe.sink(),reps);
        worker.start();
        return pipe.source();
    }

    //A worker thread object which writes data down a channel.
    //Note:this object knows nothing about Pipe,uses only a gerneric WritableByteChannel
    private static class Worker extends Thread{
        WritableByteChannel channel;
        private int reps;
        Worker(WritableByteChannel channel,int reps) {
            this.channel=channel;
            this.reps=reps;
        }
        
        //Thread execution begins here
        @Override
        public void run() {
            ByteBuffer buffer=ByteBuffer.allocate(100);
            for (int i = 0; i < reps; i++) {
                doSomeWork(buffer);
                //channel may not take it all at once
                try {
                    while (channel.write(buffer) > 0) {
                        //empty
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        
        private String[] products={"No good deed goes unpunished","To be,or what?","No matter where you go,there you are","just say \"Yo\"","My karma ran over my dogma"};
        private Random rand=new Random();
        
        private void doSomeWork(ByteBuffer buffer) {
            int product = rand.nextInt(products.length);
            buffer.clear();
            buffer.put(products[product].getBytes());
            buffer.put("\r\n".getBytes());
            buffer.flip();
        }
        
    }
}
