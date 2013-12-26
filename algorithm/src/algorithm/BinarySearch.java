package algorithm;

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
            System.out.println(mid);
            if (key < a[mid]) hi = mid - 1;
            if (key > a[mid]) lo = mid + 1;
            else return mid;
        }
        return -1;
    }

    public static int search(int val, int[] source) {
        int i = 0, j = source.length - 1;
        while (i <= j) {
            int m = i + (j - i) / 2;
            if (source[m] == val) {
                return m;
            }
            if (source[m] > val) {
                j = m - 1;
            } else {
                i = m + 1;
            }
        }
        return -1;
    }


    public static void main(String[] args) {
        System.out.println(search(8, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
    }

}
