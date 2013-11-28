package com.engineer.sun.thread;

public class InterruptCheck extends Thread {
    @Override
    public void run() {
        System.out.println("start");
        while (true) {
            System.out.println("---");
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }
        System.out.println("while exit");
    }

    public static void main(String[] args) {
        Thread thread = new InterruptCheck();
        thread.start();
        try {
            sleep(2000);
        } catch (InterruptedException e) {
        }
        thread.interrupt();
    }
}

