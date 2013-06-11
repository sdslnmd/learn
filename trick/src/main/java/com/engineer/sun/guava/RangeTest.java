package com.engineer.sun.guava;

import com.google.common.collect.Range;

/**
 * User: ln.sun01@zuche.com
 * Date: 13-4-24 上午9:14
 */
public class RangeTest {
    public static void main(String[] args) {
        System.out.println(Range.closedOpen(10, 100).contains(100));
    }
}
