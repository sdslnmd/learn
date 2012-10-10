package com.engineer.sun.sort;

public class Person {

    public Person(String address, int id, String name) {
        this.address = address;
        this.id = id;
        this.name = name;
    }

    private int id;
    private String name, address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}