package com.engineer.sun.filter;

import com.engineer.sun.sort.Person;

/**
 * User: sunluning
 * Date: 12-10-26 下午9:38
 */
public enum PersonFilter implements Filter<Person> {

    nameFilter {
        @Override
        public boolean isFilter(Person s) {
            return "name".equals(s.getName());
        }
    },

    ageFilter {
        @Override
        public boolean isFilter(Person s) {
            return 12 == s.getAge();
        }
    }
}
