package thread.Phaser;

import java.util.ArrayList;
import java.util.List;

/**
 * http://niklasschlimm.blogspot.com/2011/12/java-7-understanding-phaser.html
 */

public class PhaserExample {

    public static void main(String[] args) {
        List<Runnable> tasks = new ArrayList<Runnable>();
        for (int i = 0; i < 2; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int a = 0, b = 1;
                    for (int i = 0; i < 20000; i++) {
                        a = a + b;
                        b = a - b;
                    }
                }
            };
            tasks.add(runnable);
        }

        try {
            new PhaserExample().runTasks(tasks);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void runTasks(List<Runnable> tasks) throws InterruptedException {

//        final Phaser phaser=new arg
    }
}
