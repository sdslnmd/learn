package rx;


import rx.subscriptions.Subscriptions;
import rx.util.functions.Action1;

import java.util.ArrayList;
import java.util.List;

public class HelloRX {
    public static void main(String[] args) {

        final List<String> strings = new ArrayList<String>();
        strings.add("a");
        strings.add("b");

        Observable.from(strings).subscribe(new Action1() {
            public void call(Object aArg0) {
                System.out.println("call " + aArg0 + Thread.currentThread().getName());
            }
        });
    }
}
