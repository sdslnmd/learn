import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.jstack;
import static com.sun.btrace.BTraceUtils.print;
import static com.sun.btrace.BTraceUtils.println;

@BTrace
public class Throwable {

    @OnMethod(clazz = "com.zuche.srms.inventoryweb.controller.InventoryQueryController", method = "execute", location = @Location(Kind.CATCH))

    public static void throwException(@ProbeClassName String pcn, @ProbeMethodName String pmn, Throwable throwable) {


        print(pcn);

        print(".");

        print(pmn);

        print("(");

        print(") throw ");

        println(throwable);
        jstack();

    }



}
