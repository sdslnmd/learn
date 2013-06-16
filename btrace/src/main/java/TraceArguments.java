import static com.sun.btrace.BTraceUtils.*;
import com.sun.btrace.annotations.*;

@BTrace
public class TraceArguments {
    @OnMethod(
            clazz = "/com\\.mkyong\\..*/",
            method = "/.*/",
            location = @Location(value = Kind.RETURN,clazz = "/.*calc.*/",method = "/.*/")
    )
    /*
      参数的顺序需要和监控的方法名一样
      @result需配置 kind.RETURN一起使用
    * */
    public static void t(String name,@Return String result) {
        println(strcat("args = ", name));
        println(strcat("re = ", result));
    }
}
