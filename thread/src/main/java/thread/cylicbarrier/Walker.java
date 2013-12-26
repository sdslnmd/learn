package thread.cylicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * User: sunluning
 * Date: 13-10-22 下午11:04
 */
public class Walker implements Runnable {

    private final CyclicBarrier mBarrier;
    private final int mThreadIndex;

    public Walker(CyclicBarrier mBarrier, int i) {
        this.mBarrier = mBarrier;
        this.mThreadIndex = i;
    }

    @Override
    public void run() {

        System.out.println("Thread" + mThreadIndex + " is running");

        try {
            TimeUnit.MICROSECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //完成任务以后，等待其他线程完成任务

        try {
            System.out.println("Thread" + mThreadIndex + "await before");
            mBarrier.await();
            System.out.println("Thread" + mThreadIndex + "await after");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        //其他线程任务完成后，阻塞解除，可以继续接下来的任务

    }
}
