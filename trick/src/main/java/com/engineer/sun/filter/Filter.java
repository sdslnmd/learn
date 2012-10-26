package com.engineer.sun.filter;

/**
 * User: sunluning
 * Date: 12-10-26 下午9:37
 */
public interface Filter<T> {
    public boolean isFilter(T t);
}
