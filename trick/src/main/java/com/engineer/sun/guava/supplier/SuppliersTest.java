package com.engineer.sun.guava.supplier;

import java.util.List;

public class SuppliersTest {

    public static void main(String[] args) {
        SuppliersPojo suppliersPojo = new SuppliersPojo("aa-bb");

        List<String> parse = suppliersPojo.parse();
        List<String> parse2 = suppliersPojo.parse();
        System.out.println(parse);
        System.out.println(parse2);

    }
}
