package com.engineer.nio;

import java.nio.ByteBuffer;
import java.sql.BatchUpdateException;

/**
 * User: sunluning
 * Date: 12-7-17 下午10:18
 */
public class ByteBufferLearn {
    public static void main(String[] args) {
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        buffer.put((byte)'h');
        buffer.put((byte)'h');

        buffer.flip();

        buffer.put((byte)'h');

        buffer.position();


    }
}
