package com.engineer.sun.thread.divide;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * User: sunluning
 * Date: 13-10-23 下午8:13
 */
public class Searcher implements Runnable {

    private int firstRow;
    private int lastRow;

    private MatrixMock matrixMock;
    private Results results;

    private int number;

    private CyclicBarrier cyclicBarrier;

    public Searcher(int firstRow, int lastRow, MatrixMock matrixMock, Results results, int number, CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.matrixMock = matrixMock;
        this.number = number;
        this.results = results;
    }

    @Override
    public void run() {
        int counter;
        System.out.printf("%s: Processing lines from %d to %d.\n", Thread.currentThread().getName(), firstRow, lastRow);


        //22. 处理分配给这个线程的全部行。对于每行，记录正在查找的数字出现的次数，并在相对于的 Results 对象中保存此数据。
        for (int i = firstRow; i < lastRow; i++) {
            int row[] = matrixMock.getRow(i);
            counter = 0;
            for (int j = 0; j < row.length; j++) {
                if (row[j] == number) {
                    counter++;
                }
            }

            results.setData(i, counter);

        }

        // 打印信息到操控台表明此对象已经结束搜索。
        System.out.printf("%s: Lines processed.\n", Thread.currentThread().getName());

        //24. 调用 CyclicBarrier 对象的 await() 方法 ，由于可能抛出的异常，要加入处理 InterruptedException and BrokenBarrierException 异常的必需代码。
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

    }

}
