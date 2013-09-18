package com.engineer.sun.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RuntimeExecution {
    public static void main(String[] args) {
        ThreadPoolExecutor workers = new ThreadPoolExecutor(10, 100, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
        for (int i = 0; i < 110; i++) {
            workers.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    throw new RuntimeException();
                }
            });
        }

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            System.out.println(String.format("task count:%s; active count:%s", workers.getTaskCount(), workers.getActiveCount()));
        }
    }
}
