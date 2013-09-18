import com.sun.btrace.AnyType;
import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class GetParam {

    @OnMethod(
            clazz = "/com\\.zuche\\.srms\\.order\\.controller\\..*/",
            method = "/.*/",
            location = @Location(Kind.RETURN)
    )
    public static void onGetOffer(@Return String result, @ProbeMethodName String pmn, AnyType[] args) {

        println(pmn);
//        println(get(field(classOf(args[0]), "recommended", false), args[0]));
        printArray(args);
        println(result);
    }
}