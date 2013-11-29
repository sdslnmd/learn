<<<<<<< HEAD
package com.engineer.sun.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
 
public class Test {
 
    private ReentrantLock lock = new ReentrantLock();
     
    private Condition condition = lock.newCondition();
     
    public static void main(String[] args) throws Exception{
        Thread t = new Thread(new Runnable() {
            public void run() {
                Test test = new Test();
                test.say();
            }
        });
        t.start();
        t.interrupt();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    Test test = new Test();
                    test.say();
                }
            }).start();
        }
    }
 
    public void say(){
        try{
            lock.lock();
            while(true){
                try{
                    condition.await();
                }
                catch(InterruptedException e){
                    System.out.println("interrupted" +Thread.currentThread().getName());
                    Thread.currentThread().interrupt();
                }
            }
        }
        finally{
            lock.unlock();
        }
    }
     
}
=======
package com.engineer.sun.thread;

import com.google.common.collect.Lists;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3, 4);
        ;
        System.out.println(integers.get(integers.size()-1));;
    }
}
>>>>>>> 29624ad7b7584c9645e838812b3655fd0403c01a
