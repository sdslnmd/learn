package com.engineer.sun.algorithm;

public class ElementarySort {

    static void selectSort(int[] source) {
        for (int i = 0; i < source.length; i++) {
            int min = i;
            for (int j = i + 1; j < source.length; j++) {
                if (source[j] < source[min]) {
                    //找到i之后元素的中的最小元素 然后i和min交换位置
                    //这样i之前就都是排过序的元素
                    min = j;
                }
            }

            int tmp = source[i];
            source[i] = source[min];
            source[min] = tmp;

            print(source);

        }
    }

    static void print(int[] source) {
        for (int i : source) {
            System.out.print(i + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] source = {7, 3, 5, 2, 1};

        print(source);

        selectSort(source);

        print(source);

    }

}
