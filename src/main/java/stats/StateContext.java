package stats;

public class StateContext {
        private State myState;
        public StateContext() {
                setState(new StateA());
        }
 
        // normally only called by classes implementing the State interface
        public void setState(State newState) {
                this.myState = newState;
        }
 
        public void writeName(String name) {
                this.myState.writeName(this, name);
        }
}