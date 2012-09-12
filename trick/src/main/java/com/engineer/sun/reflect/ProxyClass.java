package com.engineer.sun.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * User: luning.sun
 * Date: 12-9-12
 * Time: 下午4:29
 */
public class ProxyClass implements InvocationHandler {
    Object object;

    public ProxyClass(Object obj) {
        this.object = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("begin");
        Object invoke = method.invoke(object, args);
        System.out.println("over");
        return invoke;
    }
}
