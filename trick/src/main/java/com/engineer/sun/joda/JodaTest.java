package com.engineer.sun.joda;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

/**
 * User: sunluning
 * Date: 13-9-3 下午9:54
 */
public class JodaTest {
    public static void main(String[] args) {


        DateTime dateTime = new DateTime();
        String s = dateTime.toString(DateTimeFormat.longDateTime());
        System.out.println(s);
    }
}
