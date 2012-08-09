package com.engineer.nio;

import java.nio.channels.SelectionKey;

/**
 * User: sunluning
 * Date: 12-7-27 上午12:10
 */
public class Test {
    public static void main(String[] args) {
        //key.readyOps( ) & SelectionKey.OP_ACCEPT
        System.out.println(SelectionKey.OP_ACCEPT|SelectionKey.OP_CONNECT);
    }
}
