

import static com.sun.btrace.BTraceUtils.*;

import com.sun.btrace.AnyType;
import com.sun.btrace.annotations.*;


@BTrace class Profiling {
    @OnMethod(
            clazz="/com\\.zuche\\.srms\\.order\\.controller\\..*/",
            method="/.*/"
    )
    public static void onWebserviceReturn(@ProbeClassName String pcn , @ProbeMethodName String pmn, @Duration long d,AnyType[] args) {


        Class aClass = classOf(pcn);
        Object name = get(field(aClass, "name"));
        print(name);

    }


    @OnMethod(
            clazz="com.engineer.sun.thread.InterruptCheck",
            method="/.*/"
    )
    public static void onWebserviceReturna(@ProbeClassName String pcn , AnyType[] args) {



        printArray(args);

    }

    @OnMethod(
            clazz="/com\\.zuche\\.srms\\.order\\.controller\\..*/",
            method="/.*/"
    )
    public static void onWebserviceReturnaa(String name,Integer name2) {



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
