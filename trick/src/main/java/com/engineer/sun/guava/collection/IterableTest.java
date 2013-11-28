package com.engineer.sun.guava.collection;

import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;

/**
 * User: sunluning
 * Date: 13-4-20 下午9:58
 */
public class IterableTest {
    public static void main(String[] args) {
        Iterable<Integer> concat = Iterables.concat(Ints.asList(1, 2, 3), Ints.asList(4, 6));
        System.out.println(concat);

        Integer last = Iterables.getLast(concat);
        System.out.println(last);


    }
}
