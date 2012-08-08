package com.sun.engineer.design.stats.state2;


/**
 * User: sunluning
 * Date: 12-7-13 上午6:46
 */
public class Turnstile {
    //states
    public static final int LOCKED = 0;
    public static final int UNLOCKED = 1;

    //Events
    public static final int COIN = 0;
    public static final int PASS = 1;

    int state = LOCKED;

    private TurnstileController turnstileController;

    public Turnstile(TurnstileController action) {
        turnstileController = action;
    }

    public void event(int event) {
        switch (state) {
            case LOCKED:
                switch (event) {
                    case COIN:
                        state = UNLOCKED;
                        turnstileController.unlock();
                        break;
                    case PASS:
                        turnstileController.alerm();
                        break;
                }
                break;
            case UNLOCKED:
                switch (event) {
                    case COIN:
                        turnstileController.thankyou();
                        break;
                    case PASS:
                        turnstileController.lock();
                        break;
                }
                break;
        }
    }

}
