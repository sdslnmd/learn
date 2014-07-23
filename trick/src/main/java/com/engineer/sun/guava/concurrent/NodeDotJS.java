package com.engineer.sun.guava.concurrent;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NodeDotJS {

    //pool for execute blocking actions

    private ExecutorService mailbox = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("mailbox-%d").build());
    private ExecutorService forBlocker = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("pool-%d").build());
//    private ExecutorService mailbox;
//    private ExecutorService forBlocker;

//    public NodeDotJS(ExecutorService mailbox, ExecutorService forBlocker) {
//        this.mailbox = mailbox;
//        this.forBlocker = forBlocker;
//    }

    //public method to receive request for outside
    public void receiveRequest(final Object req) {
        //force all requests onto  single main thread in Executor mailbox
        mailbox.execute(new Runnable() {
            @Override
            public void run() {
                processRequest(req);
            }
        });

    }

    HashMap someMap = Maps.newHashMap();
    int someInt;

    //executor in mailbox-thread only
    private void processRequest(Object req) {
        someInt++;
        // ok now lets execute the blocking action in our pool reserved for blocking action
        forBlocker.execute(new Runnable() {
            @Override
            public void run() {
                //do blocking call ~~~~~~
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //put the result to the queue of out single thread this way we ensure single threaded execution

                mailbox.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("done");
                        someMap.get("bla_" + someInt);
                        //yay call back hell,but single thread
                    }
                });


            }
        });

    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService mailbox = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("mailbox-%d").build());
        ExecutorService forBlocker = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("pool-%d").build());
//        NodeDotJS nodeDotJS = new NodeDotJS(mailbox, forBlocker);


        Stopwatch started = Stopwatch.createStarted();
        NodeDotJS nodeDotJS = new NodeDotJS();
        nodeDotJS.receiveRequest("sdf");
//
//        mailbox.shutdown();
//        forBlocker.shutdown();
//        mailbox.awaitTermination(1, TimeUnit.DAYS);
//        forBlocker.awaitTermination(1, TimeUnit.DAYS);


        System.out.println(started.stop());

    }

}
