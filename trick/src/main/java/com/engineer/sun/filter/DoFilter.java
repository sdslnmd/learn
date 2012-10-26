package com.engineer.sun.filter;

import com.engineer.sun.sort.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sunluning
 * Date: 12-10-26 下午9:49
 */
public class DoFilter {
    public static void main(String[] args) {
        List<Person> list = new ArrayList<Person>();


    }

    public static boolean doFilter (Person person) {
        List<Filter<Person>> filters=new ArrayList<Filter<Person>>();
        filters.add(PersonFilter.ageFilter);
        filters.add(PersonFilter.nameFilter);

        for (Filter<Person> filter : filters) {
            if (filter.isFilter(person)) {
                return true;
            }

        }

        return false;
    }
}
