package com.engineer.sun.reflect;

import java.lang.reflect.Proxy;

/**
 * User: luning.sun
 * Date: 12-9-12
 * Time: 下午3:59
 */
public class Invoke {
    public static void main(String[] args) {
        MyInterface o = (MyInterface) Proxy.newProxyInstance(MyInterface.class.getClassLoader(), new Class[]{MyInterface.class}, new ProxyClass(new MyInterfaceImpl()));
        o.work("123");
    }


}
