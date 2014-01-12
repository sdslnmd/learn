package algorithm;

public class MergeSort {

    private static int[] aux;

    public static void sort(int[] a) {
        aux = new int[a.length];
        sort(a, 0, a.length - 1);
    }

    private static void sort(int[] a, int lo, int hi) {
        System.out.println("sort "+"lo=" + lo + " hi=" + hi);
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, lo, mid);
        sort(a, mid + 1, hi);
        merge(a, lo, mid, hi);
    }

    private static void merge(int[] a, int lo, int mid, int hi) {
        System.out.println("merge "+"lo=" + lo + " hi=" + hi);
        int left = lo, right = mid + 1;
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        for (int k = lo; k <= hi; k++) {
            if (left > mid) {
                a[k] = aux[right++];
            } else if (right > hi) {
                a[k] = aux[left++];
            } else if (aux[right] <= aux[left]) {
                a[k] = aux[right++];
            } else if (aux[right] > aux[left]) {
                a[k] = aux[left++];
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {4, 1, 3, 2};
        MergeSort.sort(a);
        System.out.println(a);
    }
}
