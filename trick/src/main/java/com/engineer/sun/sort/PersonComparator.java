package com.engineer.sun.sort;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.*;

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
    },
    RULE_SORT {
        public int compare(Person o1, Person o2) {
            try {
                RuleBasedCollator rule = new RuleBasedCollator("< '砸人' <'发哥' < '阿门' ");
                return rule.compare(o1.getName(),o2.getName());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return o1.getName().compareTo(o2.getName());
        }
    },
    CHINA_SORT {
        public int compare(Person o1, Person o2) {
            RuleBasedCollator collator_ch = (RuleBasedCollator) Collator.getInstance(Locale.CHINA);
            return collator_ch.compare(o1.getName(),o2.getName());
        }
    },
    ;

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
        list.add(new Person("beijing", 1, "白痴"));
        list.add(new Person("haiyang", 2, "阿门"));
        list.add(new Person("shanghai", 1, "砸人"));
        list.add(new Person("guangdong", 3, "发哥"));

        Locale loc = new Locale("sr", "RS");

        Collator col = Collator.getInstance(loc);
        col.setStrength(Collator.SECONDARY);

        Collections.sort(list, asc(getComparator(RULE_SORT)));

        for (Person person : list) {
            System.out.println(person.getAddress()+person.getName() + person.getId());
        }
    }
}