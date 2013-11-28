package com.engineer.sun.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class Atomicity {
    private static volatile int nonAtomicCounter = 0;
    private static volatile AtomicInteger atomicCounter = new AtomicInteger(0);

    private static int times = 0;

    public static void calculate() {
        times++;
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    nonAtomicCounter++;
                    atomicCounter.incrementAndGet();
                }
            }).start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
    }

    public static void main(String[] args) {
        calculate();
        while (nonAtomicCounter == 1000) {
            nonAtomicCounter = 0;
            atomicCounter.set(0);
            calculate();
        }

        System.out.println("Non-atomic counter: " + times + ":" + nonAtomicCounter);
        System.out.println("Atomic counter: " + times + ":" + atomicCounter);
    }
}
