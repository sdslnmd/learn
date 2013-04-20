package com.engineer.sun.guava.ordeing;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * User: sunluning
 * Date: 13-4-20 下午4:03
 */
public class OrdingTest {
    public static void main(String[] args) {

        Person e2 = new Person(2, "shanghai", "shanghia");

        ImmutableList<Person> of = ImmutableList.of(new Person(1, "beijing", "bj"), e2);
        ArrayList<Person> persons = Lists.newArrayList(of);

        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return ComparisonChain.start()
                        .compareTrueFirst(o1.city.equals("shanghai"), o2.city.equals("shanghai"))
                        .compare(o1.age, o2.age)
                        .result();
            }
        });


        System.out.println(of);

    }

    private static class Person {
        int age;
        String name;
        String city;

        private Person(int age, String city, String name) {
            this.age = age;
            this.city = city;
            this.name = name;
        }


    }
}
