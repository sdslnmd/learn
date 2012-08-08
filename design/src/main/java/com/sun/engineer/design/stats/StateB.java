package com.sun.engineer.design.stats;

class StateB implements State {
        private int count=0;
        public void writeName(StateContext stateContext, String name){
                System.out.println(name.toUpperCase());
                // change state after StateB's writeName() gets invoked twice
                if(++count>1) {
                        stateContext.setState(new StateA());
                }
        }
}