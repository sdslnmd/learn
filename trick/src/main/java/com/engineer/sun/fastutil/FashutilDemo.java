package com.engineer.sun.fastutil;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

public class FashutilDemo {
    public static void main(String[] args) {
        Int2IntArrayMap int2IntArrayMap = new Int2IntArrayMap();
        int2IntArrayMap.put(1, 2);


        IntSet integers = int2IntArrayMap.keySet();
        for (int integer : integers) {
            System.out.println(int2IntArrayMap.get(integer));
        }

        int x = int2IntArrayMap.get(1);
        System.out.println(x);

        IntList intList = new IntArrayList();
        Iterables.filter(intList, new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return false;
            }
        });

    }
}
