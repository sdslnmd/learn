package algorithm;

public class StringReverse {

    public static String reverse(String str) {

        char[] chars = str.toCharArray();

        int i = 0;
        int j = chars.length-1;

        while (i < j) {
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
            i++;
            j--;
        }

        return new String(chars);
    }

    public static void main(String[] args) {
        String reverse = StringReverse.reverse("4321");
        System.out.println(reverse);

    }
}
