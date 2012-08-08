package com.sun.engineer.design.stats;


public class TestClientState {
    public static void main(String[] args) {
        StateContext sc = new StateContext();
        sc.writeName("Monday");
        sc.writeName("Tuesday");
        sc.writeName("Wednesday");
        sc.writeName("Thursday");
        sc.writeName("Saturday");
        sc.writeName("Sunday");
    }
}