package com.engineer.sun.algorithm;

/**
 * 二分查找
 * User: sunluning
 * Date: 12-11-17 下午2:28
 */
public class BinarySearch {
    public static int rank(int key, int[] a) {
        //数组必须是有序的
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            //被查找的key要么不存在 要么在a[lo]-a[hi]之间
            int mid = lo + (hi - lo) / 2;
            if(key<a[mid]) hi = mid - 1;
            if(key>a[mid]) lo = mid + 1;
            else return mid;
        }
        return -1;
    }


}
