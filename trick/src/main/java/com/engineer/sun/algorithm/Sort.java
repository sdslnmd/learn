package com.engineer.sun.algorithm;

public class Sort {
    public static void qsort(int[] a, int start, int end) {

        System.out.println("start=" + start + " end=" + end);

        System.out.print("输入: ");
        for (int i : a) {
            System.out.print(i + ",");
        }
        System.out.println("  ");


        if (end <= start) return; // 其实这句可有可无, 只要调用的时候不故意出错

        int left = start;
        int right = end;
        int pivot = a[left + (right - left) / 2];

        System.out.println("pivot= " + pivot);

        // selection
        while (left <= right) {
            while (a[left] < pivot) left++;
            while (a[right] > pivot) right--;
            System.out.println("left= " + left + " val= "+a[left]+" right= " + right+" val= "+a[right]);
            if (left <= right) {
                int tmp = a[left];
                a[left] = a[right];
                a[right] = tmp;
                left++;
                right--;
            }
        }
        // divide
        if (left < end) qsort(a, left, end);
        if (right > start) qsort(a, start, right);
    }

    public static void main(String[] args) {
        int[] a = {0,3,1,2};

        for (int i : a) {
            System.out.print(i + ",");
        }
        System.out.println("start--->\r\n");
        qsort(a, 0, a.length - 1);

        System.out.println("");
        System.out.println("<-----end\r\n");
        for (int i : a) {
            System.out.print(i + ",");
        }

    }
}
