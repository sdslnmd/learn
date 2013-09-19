package com.engineer.sun.performance;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * User: sunluning
 * Date: 13-7-25 下午8:07
 */
public class HighCpu {


    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            work();
        }
    }

    public static void work() {
        ArrayList<Integer> list = Lists.newArrayList();
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            list.add(random.nextInt());
        }
        Collections.sort(list);
    }

}
