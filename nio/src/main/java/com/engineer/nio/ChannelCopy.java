package com.engineer.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * User: sunluning
 * Date: 12-7-19 下午11:07
 */
public class ChannelCopy {
    public static void main(String[] args) throws IOException {
        long begin = System.currentTimeMillis();
        ReadableByteChannel readableByteChannel = Channels.newChannel(new FileInputStream("/Users/sunluning/tmp/source.txt"));
        WritableByteChannel writableByteChannel = Channels.newChannel(new FileOutputStream("/Users/sunluning/tmp/dest.txt"));

//        channelCopy1(readableByteChannel,writableByteChannel);

        channel2Channel();

        long  over = System.currentTimeMillis();

        System.out.println(over-begin);

    }

    private static void channelCopy1(ReadableByteChannel readableByteChannel, WritableByteChannel writableByteChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (readableByteChannel.read(buffer) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                writableByteChannel.write(buffer);
            }
            buffer.clear();
        }
    }

    private static void channel2Channel() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("/Users/sunluning/tmp/source.txt");
        FileChannel channel = fileInputStream.getChannel();
        channel.transferTo(0,channel.size(),new FileOutputStream("/Users/sunluning/tmp/dest.txt").getChannel());
    }

}
