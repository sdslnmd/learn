package com.engineer.sun.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: sunluning
 * Date: 13-10-23 下午9:31
 */
public class MutliThreadTestPattern {
    public static void main(String[] args) {

        int count = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(count);

        final CountDownLatch countDownLatch = new CountDownLatch(count);
        final AtomicInteger successInteget = new AtomicInteger(0);

        for (int i = 0; i < count; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        //dosomethint
                        successInteget.incrementAndGet();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }

        // 获取成功数据
        successInteget.get();
        executorService.shutdownNow();
    }
}
