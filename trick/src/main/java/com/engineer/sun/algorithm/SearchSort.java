package com.engineer.sun.algorithm;

public class SearchSort {

    public static void sort(int[] source) {

        for (int i = 0; i < source.length; i++) {
            int min = i;
            for (int j = i + 1; j < source.length; j++) {
                if (source[j] < min) {
                    min = j;
                }
                int t = source[i];
                source[i] = source[j];
                source[j] = t;
            }
        }
    }

    public static void main(String[] args) {
        int[] source = {3, 1, 2};
        sort(source);

        for (int i : source) {
            System.out.println(i);

        }
    }
}
