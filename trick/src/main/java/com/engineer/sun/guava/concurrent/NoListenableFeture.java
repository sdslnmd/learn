package com.engineer.sun.guava.concurrent;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.*;

/**
 * User: sunluning
 * Date: 13-7-10 下午10:16
 */
public class NoListenableFeture {
    public static void main(String[] args) {

        Stopwatch started = Stopwatch.createStarted();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("test-%d").build());

        List<Future<String>> futureList = Lists.newArrayList();

        for (int i = 0; i < 10; i++) {
            Future<String> submit = cachedThreadPool.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(2000);
                    return Thread.currentThread().getName();
                }
            });

            futureList.add(submit);
        }

        for (Future<String> stringFuture : futureList) {
            try {

                String s = stringFuture.get();
                System.out.println(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        cachedThreadPool.shutdown();
        try {
            cachedThreadPool.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println(started.stop());

    }

}
