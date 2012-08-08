package nio.reacotor;

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

    ByteBuffer.allocate(1024);

    Handler(Selector sel, SocketChannel c) throws IOException {
        socket = c;
        c.configureBlocking(false);
        sk = socket.register(sel, 0);
        sk.attach(this);

        //Optionallly try first read now
    }

    @Override
    public void run() {

    }
}
