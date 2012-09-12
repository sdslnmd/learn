package com.engineer.sun.reflect;

/**
 * User: luning.sun
 * Date: 12-9-12
 * Time: 下午3:58
 */
public class MyInterfaceImpl implements MyInterface {
    @Override
    public void work(String name) {
        System.out.println("hello "+name);
    }
}
