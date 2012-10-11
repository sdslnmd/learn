package com.engineer.sun.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

enum PersonComparator implements Comparator<Person> {
    ID_SORT {
        public int compare(Person o1, Person o2) {
            return Integer.valueOf(o1.getId()).compareTo(o2.getId());
        }
    },
    NAME_SORT {
        public int compare(Person o1, Person o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    public static Comparator<Person> desc(final Comparator<Person> other) {
        return new Comparator<Person>() {
            public int compare(Person o1, Person o2) {
                return -1 * other.compare(o1, o2);
            }
        };
    }

    public static Comparator<Person> asc(final Comparator<Person> other) {
        return new Comparator<Person>() {
            public int compare(Person o1, Person o2) {
                return other.compare(o1, o2);
            }
        };
    }

    public static Comparator<Person> getComparator(final PersonComparator... multipleOptions) {
        return new Comparator<Person>() {
            public int compare(Person o1, Person o2) {
                for (PersonComparator option : multipleOptions) {
                    int result = option.compare(o1, o2);
                    if (result != 0) {//如果不相等则返回，相等则利用下一个compare继续比较；
                        return result;
                    }
                }
                return 0;
            }
        };
    }


    public static void main(String[] args) {
        List<Person> list = new ArrayList<Person>();
        list.add(new Person("beijing",1,"b"));
        list.add(new Person("shanghai",2,"a"));
        list.add(new Person("shanghai",1,"a"));
        list.add(new Person("guangdong",3,"c"));
        Collections.sort(list, asc(getComparator(NAME_SORT, ID_SORT)));

        for (Person person : list) {
            System.out.println(person.getName()+person.getId());
        }
    }
}