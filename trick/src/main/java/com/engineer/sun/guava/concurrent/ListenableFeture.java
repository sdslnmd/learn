package com.engineer.sun.guava.concurrent;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * User: sunluning
 * Date: 13-7-10 下午10:16
 */
public class ListenableFeture {
    public static void main(String[] args) {

        Stopwatch started = Stopwatch.createStarted();

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("test-%d").build());
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(cachedThreadPool);

        List<ListenableFuture<String>> listenableFetures = Lists.newArrayList();

        for (int i = 0; i < 10; i++) {
            ListenableFuture<String> submit = listeningExecutorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(2000);
                    return Thread.currentThread().getName();
                }
            });

            listenableFetures.add(submit);
//            submit.addListener(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println(Thread.currentThread().getName() + "done");
//                }
//            }, MoreExecutors.sameThreadExecutor());
        }


        ListenableFuture<List<String>> listListenableFuture = Futures.allAsList(listenableFetures);

        Futures.addCallback(listListenableFuture,new FutureCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                System.out.println("hahha");
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


        listeningExecutorService.shutdown();
        try {
            listeningExecutorService.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(started.stop().toString());
    }

}
