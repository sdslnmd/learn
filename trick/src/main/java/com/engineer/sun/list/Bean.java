package com.engineer.sun.list;

/**
 * User: sunluning
 * Date: 12-10-30 下午10:21
 */
public class Bean {


    public Bean(String address, String name) {
        this.address = address;
        this.name = name;
    }

    private String name,address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bean bean = (Bean) o;

        if (!address.equals(bean.address)) return false;
        if (!name.equals(bean.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + address.hashCode();
        return result;
    }
}
