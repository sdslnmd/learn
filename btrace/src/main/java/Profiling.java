

import static com.sun.btrace.BTraceUtils.*;

import com.sun.btrace.AnyType;
import com.sun.btrace.annotations.*;


@BTrace class Profiling {
    @OnMethod(
            clazz="/com\\.zuche\\.srms\\.order\\.controller\\..*/",
            method="/.*/",
            location=@Location(Kind.RETURN)
    )
    public static void onWebserviceReturn(@ProbeClassName String pcn , @ProbeMethodName String pmn, @Duration long d,AnyType[] args) {
        println(Strings.strcat(Strings.strcat(pcn, "."), pmn));
        println(Strings.strcat("Time taken (sec) ", Strings.str(d / 1000/1000)));
        printArray(args);
        println("==========================");

    }


   /* @OnMethod(
            clazz="/com\\.zuche\\.srms\\.order\\.controller\\..*//*",
            method="/.*//*"
    )
    public static void e(@ProbeClassName String pcn , @ProbeMethodName String pmn,AnyType[] args) {
        println(Strings.strcat(Strings.strcat(pcn, "."), pmn));
//        printArray(args);
        println("==========================");
    }*/
}
