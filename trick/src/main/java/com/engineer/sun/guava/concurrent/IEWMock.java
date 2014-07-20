package com.engineer.sun.guava.concurrent;

import com.engineer.sun.guava.concurrent.pojo.*;
import com.google.common.base.Function;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.*;

public class IEWMock {
    public static void main(String[] args) {

        Stopwatch started = Stopwatch.createStarted();
        ThreadFactory calcBuild = new ThreadFactoryBuilder().setNameFormat(" iew-calc-%d ").build();

        ExecutorService calcExecutorService = Executors.newFixedThreadPool(10,calcBuild);

        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(calcExecutorService);

        //运价筛选
        final ListenableFuture<List<Fare>> fareListFuture= listeningExecutorService.submit(new Callable<List<Fare>>() {
            @Override
            public List<Fare> call() throws Exception {

                System.out.println("运价筛选 running "+Thread.currentThread().getName());

                Thread.sleep(5000);

                return Lists.newArrayList();
            }
        });
        //路线
        ListenableFuture<List<Route>> routeListFuture= listeningExecutorService.submit(new Callable<List<Route>>() {
            @Override
            public List<Route> call() throws Exception {
                System.out.println("查询路线"+ Thread.currentThread().getName());
                Thread.sleep(2000);
                return Lists.newArrayList();
            }
        });

        //路线==AV
        ListenableFuture<List<AVOption>> avOptionFuture = Futures.transform(routeListFuture, new Function<List<Route>, List<AVOption>>() {
            @Override
            public List<AVOption> apply(List<Route> input) {
                System.out.println("根据路线获取AV running"+Thread.currentThread().getName()+System.currentTimeMillis());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return Lists.newArrayList();
            }
        },calcExecutorService);//注意service 并发执行

        //路线==>Bonus
        final ListenableFuture<List<Bonus>> bonusFuture= Futures.transform(routeListFuture, new Function<List<Route>, List<Bonus>>() {
            @Override
            public List<Bonus> apply(List<Route> input) {
                System.out.println("根据路线获取bonus running"+ Thread.currentThread().getName()+System.currentTimeMillis());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return Lists.newArrayList();
            }
        },calcExecutorService);//注意service 并发执行

        //AV Fare合并
        ListenableFuture<List<List<?>>> avFareMergeFuture = Futures.allAsList(avOptionFuture, fareListFuture);
        ListenableFuture<List<PriceUnit>> mergeAVFuture = Futures.transform(avFareMergeFuture, new Function<List<List<?>>, List<PriceUnit>>() {
            @Override
            public List<PriceUnit> apply(List<List<?>> input) {
                System.out.println("av 运价 合并" + "input size" + input.size() + Thread.currentThread().getName());

                return Lists.newArrayList();
            }
        });


        //AV 运价 政策合并
        ListenableFuture<List<List<?>>> bonusAVFareFuture = Futures.allAsList(avFareMergeFuture, bonusFuture);
        ListenableFuture<List<Result>> resultFuture = Futures.transform(bonusAVFareFuture, new Function<List<List<?>>, List<Result>>() {
            @Override
            public List<Result> apply(List<List<?>> input) {
                System.out.println("av 运价 合并" + "input size" + input.size() + Thread.currentThread().getName());

                return Lists.newArrayList();
            }
        });


        // 输出结果
        List<Result> x = null;
        try {
            x = resultFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(x);


        calcExecutorService.shutdown();
        try {
            calcExecutorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(started.stop());

    }
}
