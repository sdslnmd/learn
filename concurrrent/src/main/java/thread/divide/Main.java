package thread.divide;

import java.util.concurrent.CyclicBarrier;

/**
 * User: sunluning
 * Date: 13-10-23 下午8:22
 */
public class Main {
    public static void main(String[] args) {
        final int ROWS = 1000;
        final int NUMBERS = 1000;
        final int SEARCH = 5;
        final int PARTICIPANTS = 5;
        final int LINES_PARTICIPANT = 20;

        MatrixMock mock = new MatrixMock(ROWS, NUMBERS, SEARCH);

        Results results = new Results(ROWS);

        Grouper grouper = new Grouper(results);

//        创建 CyclicBarrier 对象，名为 barrier。此对象会等待5个线程。当此线程结束后，它会执行前面创建的 Grouper 对象
        CyclicBarrier barrier = new CyclicBarrier(PARTICIPANTS, grouper);

        //38. 创建5个 Searcher 对象，5个执行他们的线程，并开始这5个线程。
        Searcher searchers[] = new Searcher[PARTICIPANTS];
        for (int i = 0; i < PARTICIPANTS; i++) {
            searchers[i] = new Searcher(i * LINES_PARTICIPANT, (i * LINES_PARTICIPANT) + LINES_PARTICIPANT, mock, results, 5, barrier);
            Thread thread = new Thread(searchers[i]);
            thread.start();
        }
        System.out.printf("Main: The main thread has finished.\n");


    }
}
