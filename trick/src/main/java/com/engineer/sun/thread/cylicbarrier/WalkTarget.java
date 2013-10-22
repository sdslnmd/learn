package com.engineer.sun.thread.cylicbarrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: sunluning
 * Date: 13-10-22 下午10:57
 */
public class WalkTarget {
    private final int mCount = 5;
    private final CyclicBarrier mBarrier;

    ExecutorService mExecutorService;

    class BarrierAction implements Runnable {
        @Override
        public void run() {

            System.out.println("所有线程都已完成任务，计数达到预设值");
//            mBarrier.reset(); //恢复到初始化状态

        }
    }

    WalkTarget() {

        //初始化CyclicBarrier

        mBarrier = new CyclicBarrier(mCount, new BarrierAction());

        mExecutorService = Executors.newFixedThreadPool(mCount);

        for (int i = 0; i < mCount; i++) {

            mExecutorService.execute(new Walker(mBarrier,i));
        }

    }

}
