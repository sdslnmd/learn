import com.mkyong.common.controller.obj.Name;
import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class TraceArguments {

    @OnMethod(
            clazz = "com.mkyong.common.controller.obj.NameProcess",
            method = "process"
            ,location = @Location(Kind.RETURN)
    )
    public static void m(@ProbeMethodName String me,@Return Name re,Name name) {

        println(get(field("com.mkyong.common.controller.obj.Name", "name"), re));
        println(get(field("com.mkyong.common.controller.obj.Name", "name"), name));

    }

}
