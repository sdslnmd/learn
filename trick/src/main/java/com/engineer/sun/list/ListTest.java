package com.engineer.sun.list;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sunluning
 * Date: 12-10-30 下午10:17
 */
public class ListTest {

    public static void main(String[] args) {

       List<Bean> list=new ArrayList<Bean>();

       Bean b1=new Bean("b1","a1");
       Bean b2=new Bean("b2","a2");
       Bean b3=new Bean("b3","a3");

       list.add(b1);
       list.add(b2);
       list.add(b3);

        List<Bean> subList=new ArrayList<Bean>();
        Bean subb3=new Bean("b3","a3");
        Bean subb4=new Bean("b4","a4");
        subList.add(subb3);

        list.removeAll(subList);

        list.retainAll(subList);
        System.out.println(list.size());


    }
}
