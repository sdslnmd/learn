package com.engineer.sun.guava.collection;

import com.engineer.sun.guava.function.Person;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sunluning
 * Date: 13-4-20 下午5:48
 */
public class CollectionTest {
    public static void main(String[] args) {
        Table<Integer, Integer, String> table = HashBasedTable.create();
        table.put(1, 1, "Hamlet");
        table.put(1, 2, "Dierk");
        table.put(2, 1, "Andres");
        table.put(2, 2, "Matthius");


        System.out.println(table.row(1));
        System.out.println(table.get(1, 2));


        ArrayList<Person> persons = Lists.newArrayList(new Person(12, "12"), new Person(1, "1"), new Person(2, "2"), new Person(2, "2"));
        List<List<Person>> partition = Lists.partition(persons, 2);
        System.out.println(partition);
    }
}
