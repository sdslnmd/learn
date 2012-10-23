package com.engineer.sun.generic;

import java.util.ArrayList;
import java.util.List;

class Gerneric<S> {

    /**
     * 实例方法可以调用类定义中的形式类型参数
     * @param key
     * @return
     */
    public S add(S key) {
        return key;
    }

    /**
     * 静态方法中无法使用类定义中的形式类型参数 利用 方法返回值前的 <T extends String> 来定义泛型
     * @param key
     * @param <T>
     * @return
     */
    public static <T extends String> List<T> ts(T key) {
        ArrayList<T> ts = new ArrayList<T>();
        ts.add(key);
        return ts;
    }

    public static void main(String[] args) {
        Gerneric<String> stringUtilities = new Gerneric<String>();
    }
}