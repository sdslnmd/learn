package com.engineer.sun.thread;

/**
 * User: ln.sun01@zuche.com
 * Date: 13-5-2 下午12:56
 */
public class ThreadBufferTest {
    public StringBuffer sb = new StringBuffer();
    public void addProperty(String name, String value) {
        if (value != null && value.length() > 0) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(name).append('=').append(value);
        }
    }

    public static void main(String[] args) {
        final ThreadBufferTest test = new ThreadBufferTest();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test.addProperty("a", "b");
            }
        }).run();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test.addProperty("c", "d");
            }
        }).run();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(test.sb.toString());
            }
        }).run();

    }
}
