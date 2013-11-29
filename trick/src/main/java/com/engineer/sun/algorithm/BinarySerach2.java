package com.engineer.sun.algorithm;

public class BinarySerach2 {
    public static int search(int[] source, int val) {
        int i = 0, j = source.length - 1;
        while (i < j) {
            int m = (j - i) / 2;
            if (source[m] == val) {
                return m;
            }
            if (source[m] > val) {
                i = m;
            } else {
                j = m;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(search(new int[]{1, 2, 3}, 5));
    }
}
