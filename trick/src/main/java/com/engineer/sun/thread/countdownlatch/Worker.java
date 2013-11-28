package com.engineer.sun.thread.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * User: sunluning
 * Date: 13-10-22 下午8:26
 */
public class Worker implements Runnable {

    private final CountDownLatch mStartSignal;
    private final CountDownLatch mDoneSignal;
    private final int mThreadIndex;


    Worker(final CountDownLatch mStartSignal, final CountDownLatch mDoneSignal, final int mThreadIndex) {
        this.mStartSignal = mStartSignal;
        this.mDoneSignal = mDoneSignal;
        this.mThreadIndex = mThreadIndex;
    }

    @Override
    public void run() {

        try {
            mStartSignal.await();//阻塞，等待 mStartSignal计数为0运行后面的代码，所有的工作线程都在等待同一个启动命令
            dowork();
            System.out.println("Thread" + mThreadIndex + " Done now " + System.currentTimeMillis());
            mDoneSignal.countDown();//完成后减1

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void dowork() {
        for (int i = 0; i < 100; i++)
        {
            ;// 耗时操作
        }
        System.out.println("Thread " + mThreadIndex + ":do work");
    }
}
