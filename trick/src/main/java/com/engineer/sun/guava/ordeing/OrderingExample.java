package com.engineer.sun.guava.ordeing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

public class OrderingExample {

	@Test
	public void testOrderingByLength() {
		Ordering<String> ordering = new Ordering<String>() {
			@Override
			public int compare(String left, String right) {
				return Ints.compare(left.length(), right.length());
			}
		};

        List<String> list = new ArrayList<String>();
		list.add("aaaaaaa");
		list.add("abaaaa");
		list.add("adaaa");
		list.add("afaa");
		list.add("aga");
		// list.add(null);
        List<String> strings = ordering.reverse().sortedCopy(list);
        System.out.println(strings);

		Comparator<String> comparator = new Comparator<String>() {
			@Override
			public int compare(String left, String right) {
				return Ints.compare(left.length(), right.length());
			}
		};

		Ordering<String> from = Ordering.from(comparator);
		String min = from.min(list);
		System.out.println("min = " + min);
		list.add(null);
		String min2 = from.nullsFirst().min(list);
		System.out.println("min2 = " + min2);
		String natural = Ordering.natural().nullsFirst().max(list);
		System.out.println("natural = " + natural);

		Ordering<Long> resultOf = from.onResultOf(new Function<Long, String>() {
			@Override
			public String apply(Long input) {
				if (input == 11) {
					input = input + 1000;
				}
				return input.toString();
			}
		});
		List<Long> longs = new ArrayList<Long>();
		longs.add(11L);
		longs.add(232L);
		longs.add(3344L);
		longs.add(41234L);
		longs.add(123456L);
		longs.add(null);
		Long max2 = resultOf.reverse().nullsFirst().max(longs);
		System.out.println(max2);
		System.out.println(resultOf.isOrdered(longs));
		System.out.println(ordering.isOrdered(list));
	}
}
