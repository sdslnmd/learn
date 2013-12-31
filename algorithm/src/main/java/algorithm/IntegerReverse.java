package algorithm;

public class IntegerReverse {

    public static int reverse(int val) {

        int res = 0;

        while (val > 0) {
            int mod = val % 10;
            val = val / 10;
            res = res * 10 + mod;
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println(IntegerReverse.reverse(321));
    }
}
