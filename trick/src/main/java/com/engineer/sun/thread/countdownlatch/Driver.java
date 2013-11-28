package com.engineer.sun.thread.countdownlatch;


import java.util.concurrent.CountDownLatch;

/**
 * User: sunluning
 * Date: 13-10-22 下午8:23
 */
public class Driver {
    private static final int total_threads = 10;
    private final CountDownLatch mStartSignal = new CountDownLatch(1);
    private final CountDownLatch mDoneSignal = new CountDownLatch(total_threads);

    void main() {

        for (int i = 0; i < total_threads; i++) {
            new Thread(new Worker(mStartSignal, mDoneSignal, i)).start();
        }
        System.out.println("Main thread Now:" + System.currentTimeMillis());

        doPrepareWork();
        mStartSignal.countDown();//计数器减一为0，工作线程真正启动具体操作
        doSomethingElse();
        try {
            mDoneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("All workers have finished now.");
        System.out.println("Main Thread Now:" + System.currentTimeMillis());

    }

    private void doSomethingElse() {
        for (int i = 0; i < 100000; i++) {
            ;// delay
        }
        System.out.println("Main Thread Do something else.");

    }

    private void doPrepareWork() {
        System.out.println("Ready,GO!");
    }

}
