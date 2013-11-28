package com.engineer.sun.thread;

import com.google.common.collect.Lists;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3, 4);
        ;
        System.out.println(integers.get(integers.size()-1));;
    }
}
