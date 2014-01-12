package algorithm.string;

public class CommonPrefix {

    public static String solution(String a, String b) {
        int m = a.length() < b.length() ? a.length() : b.length();

        int p = 0;
        while (p < m && a.charAt(p) == b.charAt(p)) {
            p++;
        }

        return a.substring(0, p);
    }

    public static void main(String[] args) {
        System.out.println(CommonPrefix.solution("abccba", "ab"));
    }
}
