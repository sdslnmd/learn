package com.engineer.sun.guava.supplier;

import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.util.List;

public class SuppliersPojo {
    private String args;

    public SuppliersPojo(String args) {
        this.args = args;
    }

    Supplier<List<String>> m = Suppliers.memoize(new Supplier<List<String>>() {
        @Override
        public List<String> get() {
            System.out.println("split");
            return Splitter.on("-").splitToList(args);
        }
    });

    public List<String> parse() {
        return m.get();

    }
}
