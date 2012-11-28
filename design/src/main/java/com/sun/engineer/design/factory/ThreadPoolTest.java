package com.sun.engineer.design.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTest {
    public static void main(String[] args) {
        int numWorkers = Integer.parseInt("5");
        int threadPoolSize = Integer.parseInt("10");

        final RuleFactory ruleFactory = RuleFactory.make("path");
        ExecutorService tpes =Executors.newFixedThreadPool(10);
    
        WorkerThread[] workers = new WorkerThread[numWorkers];
        for (int i = 0; i < 10; i++) {
            workers[i] = new WorkerThread(ruleFactory);
            tpes.execute(workers[i]);
        }
        tpes.shutdown();
    }
}