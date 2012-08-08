package com.engineer.nio;

import java.nio.CharBuffer;

/**
 * User: sunluning
 * Date: 12-7-17 下午10:31
 */
public class BufferFileDrain {
    public static void main(String[] args) {
        CharBuffer buffer=CharBuffer.allocate(100);
        while (fillBuffer(buffer)) {
            buffer.flip();
            drainBuffer(buffer);
            buffer.clear();
        }

    }
    
    private static String[] strings={"hello","world"};

    private static int index=0;

    private static void  drainBuffer(CharBuffer buffer) {
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
        System.out.println("");

    }

    private static boolean fillBuffer(CharBuffer buffer) {
        if (index > strings.length) {
            return false;
        }

        String string=strings[index++];

        for (int i = 0; i < string.length(); i++) {
            buffer.put(string.charAt(i));
        }

        return true;
    }

}
