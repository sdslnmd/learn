package com.engineer.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * User: sunluning
 * Date: 12-7-24 下午9:48
 */
public class ConnectAsync {
    public static void main(String[] args) throws IOException {
        String host="localhost";
        int port=1234;

        InetSocketAddress inet=new InetSocketAddress(host,port);

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        System.out.println("initiation connnection");
        
        socketChannel.connect(inet);

        while (!socketChannel.finishConnect()) {
            doSomethingUserful();
        }

        System.out.println("connection established");

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();

        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        System.out.println(new String(bytes));
        socketChannel.close();
    }

    private static void doSomethingUserful() {
        System.out.println("doSomethingUserful");
    }
}
