package com.sun.engineer.design.factory;

public class WorkerThread implements Runnable {
    private RuleFactory ruleFactory;

    WorkerThread(RuleFactory number) {
        ruleFactory = number;
    }

    public void run() {

        ruleFactory.sayHello();
    }
}
