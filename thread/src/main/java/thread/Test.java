
package thread;

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
//        t.interrupt();
        for (int i = 0; i < 2; i++) {
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
