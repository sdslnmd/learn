package com.engineer.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * User: sunluning
 * Date: 12-7-25 下午9:14
 */
public class SelectSockets {
    public static int PORT_NUMBER = 1234;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws Exception {
        new SelectSockets().go(args);
    }

    public void go(String argsv[]) throws Exception {

        int port = PORT_NUMBER;
        System.out.println("Listening out port" + port);

        //allocate an unbound server socket channel

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(port));

        Selector selector = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int select = selector.select();

            if (0 == select) {
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel accept = channel.accept();
                    registerChannel(selector, channel, SelectionKey.OP_READ);
                    sayHello(accept);
                }
                if (selectionKey.isReadable()) {
                    readDateFromSocket(selectionKey);
                }
            }
        }
    }

    protected void readDateFromSocket(SelectionKey selectionKey) throws IOException {
        SocketChannel SocketChannel = (SocketChannel) selectionKey.channel();
        int count;

        buffer.clear();

        while ((count = SocketChannel.read(buffer)) > 0) {

            buffer.flip();
            while (buffer.hasRemaining()) {
                SocketChannel.write(buffer);
            }

            buffer.clear();
        }
        if (count < 0) {
            SocketChannel.close();
        }

    }

    private void registerChannel(Selector selector, ServerSocketChannel channel, int opRead) throws IOException {

        if (null == channel) {
            return;
        }

        channel.configureBlocking(false);
        channel.register(selector, opRead);
    }


    private void sayHello(SocketChannel channel) throws IOException {
        buffer.clear();
        buffer.put("hello world".getBytes());
        buffer.flip();
        channel.write(buffer);
    }
}
