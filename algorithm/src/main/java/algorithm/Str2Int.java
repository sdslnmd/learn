package algorithm;

public class Str2Int {
    public static int solution(String arg) {
        return 0;
    }

    public static void main(String[] args) {
        String tmp = "12345";
        int result = 0;
        for (int i = 0; i < tmp.length(); i++) {
            char digit = (char) (tmp.charAt(i) - '0');
            result += (digit * Math.pow(10, (tmp.length() - i - 1)));

        }

        System.out.println(result);

    }
}
