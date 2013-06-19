package com.zuche.concurrent;

import java.util.LinkedList;
import java.util.List;

/**
 * User: sunluning
 * Date: 13-6-18 上午11:33
 */
public class SingletonCurrent {
    private static final SingletonCurrent t = new SingletonCurrent();

    private List<String> list;
    private List<String> list2;

    private SingletonCurrent() {
    }

    public static SingletonCurrent getInstance() {
        return t;
    }

    public void add(String a) {

        list = new LinkedList<String>();
        list2 = new LinkedList<String>();

        list.add(a);
        list2.add(a);

        String c = "";
        for (String s : list) {
            c += s;
        }
        for (String s : list2) {
            c += s;
        }


        System.out.println(Thread.currentThread().getName()+"==="+c);
    }

}
