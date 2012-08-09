package com.sun.engineer.design.observer;

/**
 * User: sunluning
 * Date: 12-6-28
 * Time: 下午11:45
 */
public class MockTimeSink implements TimeSink {

    private int itsHours;
    private int itsMinutes;
    private int itsSeconds;

    public void setTime(int hours, int minutes, int seconds) {
        itsHours=hours;
        itsMinutes=minutes;
        itsSeconds=seconds;
    }

    public int getItsHours() {
        return itsHours;
    }

    public int getItsMinutes() {
        return itsMinutes;
    }

    public int getItsSeconds() {
        return itsSeconds;
    }
}
