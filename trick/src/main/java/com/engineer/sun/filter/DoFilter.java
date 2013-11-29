package com.engineer.sun.filter;

import com.engineer.sun.sort.Person;

import javax.lang.model.element.Name;
import java.util.ArrayList;
import java.util.List;

/**
 * User: sunluning
 * Date: 12-10-26 下午9:49
 */
public class DoFilter {
    public static void main(String[] args) {
        List<Person> list = new ArrayList<Person>();

        Person p1 = new Person("address", 1, "1ame");
        p1.setAge(11);

        list.add(p1);

        for (Person person : list) {
            System.out.println(doFilter(person));
        }
    }

    public static boolean doFilter (Person person) {
        List<Filter<Person>> filters=new ArrayList<Filter<Person>>();
        filters.add(PersonFilter.ageFilter);
        filters.add(PersonFilter.nameFilter);

        for (Filter<Person> filter : filters) {
            if (!filter.isFilter(person)) {
                return false;
            }
        }

        System.out.println("t");
        return true;
    }
}
