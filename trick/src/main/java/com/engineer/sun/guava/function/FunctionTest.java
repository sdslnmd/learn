package com.engineer.sun.guava.function;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.*;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * User: sunluning
 * Date: 13-4-19 下午9:10
 */
public class FunctionTest {

    public static void main(String[] args) {
        ImmutableList<String> of = ImmutableList.of("a", "b");

        Collection<String> transform = Collections2.transform(of, new Function<String, String>() {
            @Override
            public String apply(@Nullable String input) {
                return input.toUpperCase();
            }
        });

        System.out.println(transform);


        Function<Person, String> upperCase = new Function<Person, String>() {
            @Override
            public String apply(@Nullable Person input) {
                return input.getName().toUpperCase();
            }
        };

        Function<String, String> addAge = new Function<String, String>() {
            @Override
            public String  apply(@Nullable String  input) {
                return input + 10;
            }
        };

        Function<Person, String> compose = Functions.compose(addAge, upperCase);

        ImmutableList<Person> name = ImmutableList.of(new Person(12, "name"));

        Collection<String> transform1 = Collections2.transform(name, compose);
        System.out.println(transform1);



        HashMap<String,String> map = Maps.newHashMap();
        map.put("k1", "k11");

        Function<String, String> function = Functions.forMap(map);

        ArrayList<String > klist = Lists.newArrayList();
        klist.add("k1");

        Collection<String> transform2 = Collections2.transform(klist, function);

        System.out.println(transform2);


    }




}
