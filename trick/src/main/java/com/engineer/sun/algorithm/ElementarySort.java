package com.engineer.sun.algorithm;

/**
 * 可视化排序算法
 * http://www.sorting-algorithms.com/
 */
public class ElementarySort {

    static void selectSort(int[] source) {
        for (int i = 0; i < source.length; i++) {
            int min = i;
            for (int j = i + 1; j < source.length; j++) {//查找右边最小的元素
                if (source[j] < source[min]) {
                    //找到i之后元素的中的最小元素 然后i和min交换位置
                    //这样i之前就都是排过序的元素
                    //总结：当前元素与右边未排序元素比较 左边总是相对整个数组排序好的
                    min = j;
                }
            }

            int tmp = source[i];
            source[i] = source[min];
            source[min] = tmp;

            print(source);

        }

    }

    static void insertSort(int[] source) {
        for (int i = 1; i < source.length; i++) {
            /**
             * 当前元素与右边元素比较 左边虽是排序好的但非整个元素排序好的 右边的元素会依次插入左边排序好的结构
             */
            for (int j = i; j > 0 && (source[j] < source[j - 1]); j--) {
                int tmp = source[j];
                source[j] = source[j - 1];
                source[j - 1] = tmp;
            }
        }
    }

    static void shellSort(int[] source) {
        int length = source.length;
        int step = 1;
        //设置为3是根据一篇论文得到的最优解
        //首先根据数组的长度确定步长
        while (step < length / 3) {
            step = 3 * step + 1;
        }
        while (step >= 1) {
            //根据步长确定开始位置
            for (int i = step; i < length; i++) {
                //将当前位置与步长对称位置的值插入排序比较
                for (int j = i; j >= step && source[j] < source[j - step]; j -= step) {
                    int tmp = source[j];
                    source[j] = source[j - step];
                    source[j - step] = tmp;
                }
            }
            //重置步长为一个较小的值 直至step为1
            step = step / 3;
        }
    }

    static void print(int[] source) {
        for (int i : source) {
            System.out.print(i + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] source = {7, 3, 5, 2};
//        int[] source = {7, 3};

        print(source);

        shellSort(source);

        print(source);

    }

}
