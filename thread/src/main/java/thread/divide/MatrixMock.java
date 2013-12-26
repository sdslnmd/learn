package thread.divide;

import java.util.Random;

/**
 * 随机生成一个在1-10之间的数字矩阵，我们将从中查找数字
 * User: sunluning
 * Date: 13-10-23 下午8:02
 */

public class MatrixMock {

    private int data[][];

    //行数 长度 我们将要查找的数字
    public MatrixMock(int size, int length, int number) {

        int counter = 0;
        data = new int[size][length];
        Random random = new Random();

        //  用随机数字填充矩阵。每生成一个数字就与要查找的数字对比，如果相等，就增加counter值。
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < length; j++) {
                data[i][j] = random.nextInt(10);
                if (data[i][j] == number) {
                    counter++;
                }
            }
        }

        //最后，在操控台打印一条信息，表示查找的数字在生成的矩阵里的出现次数。此信息是用来检查线程们获得的正确结果的。
        System.out.printf("Mock: There are %d ocurrences of number in generated data.\n", counter, number);
    }

    //7.    实现 getRow() 方法。此方法接收一个 int为参数，是矩阵的行数。返回行数如果存在，否则返回null。
    public int[] getRow(int row) {
        if ((row >= 0) && (row < data.length)) {
            return data[row];
        }
        return null;
    }

}
