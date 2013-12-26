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


    public static int search_recurse(int value, int[] source, int low, int high) {

        int mid = low+(high - low) / 2;

        if (value < source[mid]) {
            return search_recurse(value, source, low, mid - 1);
        }
        if (value > source[mid]) {
            return search_recurse(value, source, mid + 1, high);
        }
        if (value == source[mid]) {
            return mid;
        }

        return -1;

    }


    public static void main(String[] args) {
        int[] source = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println(search_recurse(8, source, 0, source.length));
    }

}
