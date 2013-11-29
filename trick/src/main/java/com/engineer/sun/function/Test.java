package com.engineer.sun.function;

/**
 * User: ln.sun01@zuche.com
 * Date: 13-4-23 上午8:35
 */
public class Test {
    static String reverse(String arg) {
        if(arg.length() == 0) {
            return arg;
        }
        else {
            return reverse(arg.substring(1, arg.length())) + arg.substring(0,1);
        }
    }

    public static void main(String[] args) {
        System.out.println(reverse("987"));
    }
}
