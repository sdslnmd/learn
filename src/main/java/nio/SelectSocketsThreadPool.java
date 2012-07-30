package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * User: sunluning
 * Date: 12-7-26 下午10:48
 */
public class SelectSocketsThreadPool extends SelectSockets {
    private static final int MAX_THREADS = 5;
    private ThreadPool pool=new ThreadPool(MAX_THREADS);

    public static void main(String[] args) throws Exception {
        new SelectSocketsThreadPool().go(args);
    }

    @Override
    protected void readDateFromSocket(SelectionKey selectionKey) throws IOException {
        WorkerThread worker=pool.getWorker();
        if (null == worker) {
            return;
        }
        worker.serviceChannel(selectionKey);
    }



    private class ThreadPool {
        List idle = new LinkedList();

        ThreadPool(int poolSize) {
            //fill up the pool with worker threads
            for (int i = 0; i < poolSize; i++) {
                WorkerThread workerThread = new WorkerThread(this);

                //Set thread name for debugging.Start it
                workerThread.setName("worker"+(i+1));
                workerThread.start();

                idle.add(workerThread);
            }
        }

        //Find an idel worker thread,if any,could return null;
        WorkerThread getWorker() {
            WorkerThread worker=null;
            synchronized (idle) {
                if (idle.size()>0) {
                    worker=(WorkerThread)idle.remove(0);
                }
            }
            return worker;
        }

        void returnWorker(WorkerThread worker) {
            synchronized (idle) {
                idle.add(worker);
            }
        }
    }

    private class WorkerThread extends Thread {
        private ByteBuffer buffer = ByteBuffer.allocate(1024);
        private ThreadPool pool;
        private SelectionKey key;

        WorkerThread(ThreadPool pool) {
            this.pool = pool;
        }

        //loop forever waiting for work to do
        public synchronized void run() {
            System.out.printf(this.getName() + "is ready");

            while (true) {
                try {
                    //sleep and release object lock
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    this.isInterrupted();
                }

                if (key == null) {
                    continue;//just in case
                }
                System.out.println(this.getName() + " has been awakened");
                try {
                    drainChannel(key);
                } catch (Exception e) {
                    System.out.println("Caught '" + e + "' closing channel");
                }
                //close channel and nudge selector
                try {
                    key.channel().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                key = null;
                //Done. Ready for more,Return to pool

            }
        }

        synchronized void serviceChannel(SelectionKey key) {
            this.key=key;
            key.interestOps(key.interestOps()&(~SelectionKey.OP_READ));
            this.notify();//Awaken the thread
        }

        void drainChannel(SelectionKey key) throws Exception {
            SocketChannel channel = (SocketChannel) key.channel();
            int count;
            buffer.clear();
            //Loop while date is available;channel is nonblocking
            while ((count = channel.read(buffer)) > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
                buffer.clear();
            }

            if (count < 0) {
                //close channel on EOF,invalidates the key;
                channel.close();
                return;
            }

            //Resume interest in OP_READ
            key.interestOps(key.interestOps() | SelectionKey.OP_READ);

            //cycle the selector so this key is active again
            key.selector().wakeup();
        }


    }

}
