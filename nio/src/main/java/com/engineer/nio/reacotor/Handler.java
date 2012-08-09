package com.engineer.nio.reacotor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * User: luning.sun
 * Date: 12-8-7
 * Time: 下午10:51
 */
final class Handler implements Runnable {
    final int MAXIN = 1024;
    final int MAXOUT = 1024;
    final SocketChannel socket;
    final SelectionKey sk;

    ByteBuffer input= ByteBuffer.allocate(1024);
    ByteBuffer output= ByteBuffer.allocate(1024);

//    ByteBuffer.
    static final int READING =0,SENDING=1;
    int state=READING;

    Handler(Selector sel, SocketChannel c) throws IOException {
        socket = c;
        c.configureBlocking(false);
        //Optionallly try first read now
        sk = socket.register(sel, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        sel.wakeup();
    }
    boolean inputIsComplete(){return true;}
    boolean outputIsComplete(){return true;}
    void process(){};

    @Override
    public void run() {
        try {
            if(state==READING) {
                read();
            }else if (state == SENDING) {
                send();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void read() throws IOException{
        socket.read(input);
    }
}
