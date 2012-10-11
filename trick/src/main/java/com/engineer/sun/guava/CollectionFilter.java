package com.engineer.sun.guava;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: sunluning
 * Date: 12-10-11 上午7:52
 */
public class CollectionFilter {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        Collection<Integer> filterCollection =
                Collections2.filter(list, new Predicate<Integer>() {
                    @Override
                    public boolean apply(Integer input) {
                        return input >= 10;
                    }
                });
    }
}
