package com.engineer.sun.guava.concurrent;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * User: sunluning
 * Date: 13-7-10 下午10:16
 */
public class ListenableFeture {
    public static void main(String[] args) {


        Function<String, String> parseFun = new Function<String, String>() {
            @Override
            public String apply(String contents) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String s = contents.toLowerCase();
                System.out.println(s);
                return s;
            }
        };
        final ListeningExecutorService pool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ArrayList<String> strings = Lists.newArrayList("a", "b", "c", "d", "e");
        final ListenableFuture<String> future = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        });

        Futures.transform(future, parseFun);
    }


}
