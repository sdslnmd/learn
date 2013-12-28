package thread;

public class Volatile {
 
    public static void main(String[] args) {
        final Volatile volObj = new Volatile();
        Thread t2 = new Thread() {
            public void run() {
                while (true) {
                    volObj.check();
                }
            }
        };
        t2.start();
        Thread t1 = new Thread() {
            public void run() {
                while (true) {
                    volObj.swap();
                }
            }
        };
        t1.start();
    }
 
    boolean boolValue;// use volatile to print "WTF!"
 
    public void check() {
        if (boolValue == !boolValue)
            System.out.println("WTF!");
    }
 
    public void swap() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolValue = !boolValue;
    }
 
}