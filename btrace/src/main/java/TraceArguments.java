import static com.sun.btrace.BTraceUtils.*;
import com.sun.btrace.annotations.*;

@BTrace
public class TraceArguments {
    @OnMethod(
            clazz = "/com\\.mkyong\\..*/",
            method = "/.*/",
            location = @Location(value = Kind.RETURN,clazz = "/.*calc.*/",method = "/.*/")
    )
    public static void t(String name,@Return String result) {
        println(strcat("args = ", name));
        println(strcat("re = ", result));
    }
}
