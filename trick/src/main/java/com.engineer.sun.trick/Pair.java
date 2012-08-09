package test;

public class Pair<T, S> {
        private T first;
        private S second;
 
        public Pair(T f, S s) {
                first = f;
                second = s;
        }
 
        public Pair(Pair<T, S> pair) {
                if (pair != null) {
                        first = pair.first;
                        second = pair.second;
                }
        }
 
        public T getFirst() {
                return first;
        }
 
        public S getSecond() {
                return second;
        }
 
        public Pair<T, S> setFirst(T f) {
                first = f;
                return this;
        }
 
        public Pair<T, S> setSecond(S s) {
                second = s;
                return this;
        }
}