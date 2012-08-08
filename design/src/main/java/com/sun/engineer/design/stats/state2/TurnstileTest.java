package com.sun.engineer.design.stats.state2;

import junit.framework.TestCase;

/**
 * User: sunluning
 * Date: 12-7-13 上午7:11
 */
public class TurnstileTest extends TestCase {
    public TurnstileTest(String name) {
        super(name);
    }

    private Turnstile t;
    private boolean lockCalled=false;
    private boolean unlockCalled=false;
    private boolean thankyouCalled=false;
    private boolean alermCalled=false;

    public void setUp() {
        TurnstileController controllerSpoof=new TurnstileController() {
            public void unlock() {
                unlockCalled=true;
            }
            public void alerm() {
                alermCalled=true;
            }
            public void thankyou() {
                thankyouCalled=true;
            }
            public void lock() {
                lockCalled=true;
            }
        };
        t = new Turnstile(controllerSpoof);
    }
    
    public void testInitialConditions() {
        assertEquals(Turnstile.LOCKED, t.state);
    }

    public void testCoinInLockedState() {
        t.state=Turnstile.LOCKED;
        t.event(Turnstile.COIN);
        assertEquals(Turnstile.UNLOCKED,t.state);
        assert(unlockCalled);
    }


}
