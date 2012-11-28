package com.engineer.sun.event;

/**
 * User: luning.sun
 * Date: 12-11-28
 * Time: 下午1:47
 */
public class Wait {
    public static void milliser(long nbMsec) {
        long t0, t1;
        t0 = System.currentTimeMillis();
        do {
            t1 = System.currentTimeMillis();
        } while (t1 - t0 < nbMsec);
    }
}
