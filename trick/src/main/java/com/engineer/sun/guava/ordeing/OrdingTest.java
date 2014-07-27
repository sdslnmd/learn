package com.engineer.sun.guava.ordeing;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: sunluning
 * Date: 13-4-20 下午4:03
 */
public class OrdingTest {
    public static void main(String[] args) {


        List<Integer> integers = Lists.newArrayList();

        Collections.max(integers);

        System.out.println(integers);



        Person e2 = new Person(2, "shanghai", "shanghia");
        ArrayList<Person> persons = Lists.newArrayList(new Person(1, "beijing", "bj"), e2);

        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return ComparisonChain.start()
                        //自定义排序顺序
                        .compareTrueFirst(o1.city.equals("beijing"), o2.city.equals("beijing"))
                        .compare(o1.age, o2.age)
                        .result();
            }
        });

        System.out.println(persons);
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
