package com.engineer.sun.reflect;

import java.io.Serializable;

/**
 * User: luning.sun
 * Date: 12-9-14
 * Time: 下午12:31
 */
public class UserBean implements IUserBean {
    private String name;
    private int age;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public void setAge(int age) {
        this.age = age;
    }
}
