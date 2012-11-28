package com.sun.engineer.design.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: sunluning
 * Date: 12-10-24 下午10:36
 */
public class Test {
    public static void main(String[] args) {

        final RuleFactory ruleFactory = RuleFactory.make("path");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
//                ruleFactory.sayHello();
                System.out.println("111");
            }
        });

    }
}
