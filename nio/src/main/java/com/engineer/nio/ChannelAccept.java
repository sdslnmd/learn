package com.engineer.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * User: sunluning
 * Date: 12-7-24 上午7:09
 */
public class ChannelAccept {
    public static final String GEEETIING = "hello i must be going.\r\n";

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 1234;

        ByteBuffer buffer = ByteBuffer.wrap(GEEETIING.getBytes());

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        while (true) {
            System.out.println("waiting for connections");

            SocketChannel accept = serverSocketChannel.accept();
            if (null == accept) {
                //no connections,snooze a while
                Thread.sleep(1000);
            } else {
                System.out.println("Incomming connection from:" + accept.socket().getRemoteSocketAddress());
                buffer.rewind();
                accept.write(buffer);
                accept.close();
            }
        }
    }
}
