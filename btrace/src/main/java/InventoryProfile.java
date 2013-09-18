import com.sun.btrace.AnyType;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.ProbeClassName;
import com.sun.btrace.annotations.ProbeMethodName;

import java.lang.reflect.Field;

import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class InventoryProfile {

    @OnMethod(clazz = "com.zuche.srms.inventoryweb.controller.InventoryQueryController", method = "/.*/")
    public static void q(@ProbeClassName String className, @ProbeMethodName String methodName, AnyType[] args) {

        AnyType arg = args[0];
        Field aaa = field(str(arg), "service_type");
        println(get(aaa));
    }
}
