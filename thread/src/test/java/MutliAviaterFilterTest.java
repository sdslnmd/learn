import thread.SingletonCurrent;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MutliAviaterFilterTest {

    @Test
    public void test_simple() {
        int count = 60;

        ExecutorService executor = Executors.newFixedThreadPool(count);

        final CountDownLatch countDown = new CountDownLatch(count);
        final AtomicInteger successed = new AtomicInteger(0);

        for (int i = 0; i < count; i++) {
            executor.submit(new Runnable() {
                public void run() {
                    try {
                        for (int i = 0; i < 100; i++) {
                        }
                        try {
                            test();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        successed.incrementAndGet();
                    } finally {
                        countDown.countDown();
                    }
                }
            });
        }

        try {
            countDown.await();
        } catch (InterruptedException e) {
        }

        Assert.assertEquals(count, successed.get());
        executor.shutdownNow();
    }

    private void test() {
        SingletonCurrent instance = SingletonCurrent.getInstance();
        instance.add("c");
    }

}  