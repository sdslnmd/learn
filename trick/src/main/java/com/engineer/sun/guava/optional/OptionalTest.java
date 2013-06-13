package com.engineer.sun.guava.optional;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

/**
 * User: ln.sun01@zuche.com
 * Date: 13-4-21 下午3:54
 */
public class OptionalTest {
    public static void main(String[] args) {
        Optional<Integer> of = Optional.of(1);
        System.out.println(of.orNull());

        Integer integer = Objects.firstNonNull(2, 3);
        System.out.println(integer);

        String s = Strings.commonPrefix("ass", "asa");
        System.out.println(s);

    }
}
