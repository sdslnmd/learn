package com.sun.engineer.design.observer;

import com.sun.engineer.design.observer.ClockDriver;

/**
 * Created by IntelliJ IDEA.
 * User: sunluning
 * Date: 12-6-28
 * Time: 上午7:58
 * To change this template use File | Settings | File Templates.
 */
public interface TimeSource {
    public void setDriver(ClockDriver driver);
}

