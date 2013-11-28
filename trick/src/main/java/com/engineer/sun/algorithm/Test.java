package com.engineer.sun.algorithm;

import java.util.Scanner;

/**
 * User: sunluning
 * Date: 12-11-18 下午3:46
 */
public class Test {
    public static void main(String[] args) {
        String s = "(12+88)";
        String r = s.replaceAll("(\\d+)", " $0 ");
        System.out.println(r);


    }

}
