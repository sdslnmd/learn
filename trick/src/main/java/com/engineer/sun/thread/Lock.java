package com.engineer.sun.thread;

public class Lock {
    private static Object o = new Object();
    static Lock lock = new Lock();

    public synchronized void dynamicMethod() {
        System.out.println("dynamic method");

        sleepSilently(2000);

    }

    private static void sleepSilently(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
