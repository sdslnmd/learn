import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.*;

@BTrace class Profiling123 {

    @OnMethod(clazz="com.mkyong.common.controller.MovieController"
            , method="getMovie"
            , location = @Location(value=Kind.CALL,clazz = "/.*/",method = "/.*/"))

    void e(@ProbeMethodName(fqn = true) String probeMethod,@TargetMethodOrField String in) {
        println(in);
    }


}
