import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class TraceArguments {
    /*@OnMethod(
            clazz = "/com\\.mkyong\\..*//*",
            method = "/.*//*",
            location = @Location(value = Kind.CALL, clazz = "/.*//*", method = "/.*//*")
    )
    public static void t(@TargetInstance Object ins, @TargetMethodOrField String target, @ProbeClassName String classname, AnyType[] anyTypes) {
        println(strcat(classname, "call ====="));
        print(strcat("instance == ",Reflective.name(Reflective.classOf(ins))));
        println(strcat("targe  ", target));
        printArray(anyTypes);
    }*/

    @OnMethod(
            clazz="/com\\.mkyong\\..*/",
            method="/.*/",
            location = @Location(value = Kind.CALL)
    )
    public static void m(@TargetInstance Object ins, @TargetMethodOrField String target,@ProbeClassName String probeClass, @ProbeMethodName String probeMethod) {
        print(strcat("instance  ",Reflective.name(Reflective.classOf(ins))));
        println(strcat(".",target));

        print(Strings.strcat("entered ", probeClass));
        println(Strings.strcat(".", probeMethod));
    }

}
