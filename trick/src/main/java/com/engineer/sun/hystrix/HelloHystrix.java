package com.engineer.sun.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;

public class HelloHystrix {


    public static void main(String[] args) {
        HystrixCommandGroupKey group = new HystrixCommandGroupKey() {
            @Override
            public String name() {
                return "group1";
            }
        };
        Observable<String> observe1 = new CommandHelloWorld(group).observe();
        Observable<String> observe2 = new CommandHelloWorld(group).observe();

        Observable<String> zip = Observable.zip(observe1, observe2, new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s + s2;
            }
        });

        zip.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println(Thread.currentThread().getName() + "done");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(Thread.currentThread().getName() + "next");
            }
        });
    }


    public static class CommandHelloWorld extends HystrixCommand<String> {

        protected CommandHelloWorld(HystrixCommandGroupKey group) {
            super(group);
        }

        @Override
        protected String run() throws Exception {
//            Thread.sleep(1000);
            return "hello world";
        }

    }

}


