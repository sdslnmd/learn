package com.engineer.sun.guava.range;

import com.google.common.collect.Range;
import org.joda.time.DateTime;

public class RangeTest {
    public static void main(String[] args) {
        DateTime s = new DateTime(2014, 12, 21, 12, 0);
        DateTime e = new DateTime(2014, 12, 25, 12, 0);

        DateTime m1 = new DateTime(2014, 12, 23, 12, 0);
        DateTime m2 = new DateTime(2014, 12, 24, 12, 0);


        boolean contains = Range.closed(s, e).isConnected(Range.closed(m1, m2));
        System.out.println(contains);

    }
}
