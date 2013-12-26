import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.Self;

import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class TraceArguments {

    // process(Name) 格式，将name作为参数至于m方法,然后利用field（filed,name）方法获取字段值
    @OnMethod(clazz = "com.mkyong.common.controller.obj.NameProcess", method = "process")
    public static void m(Object name) {
        println(get(field("com.mkyong.common.controller.obj.Name", "name"), name));

    }

    //查看target 中的字段名称,在行参中使用@self 标记onMethod中的class,然后利用field(filed,self)方法获取值
    @OnMethod(clazz = "com.mkyong.common.controller.obj.Name", method = "getName")
    public static void m1(@Self Object name) {
        println(get(field("com.mkyong.common.controller.obj.Name", "name"), name));

    }

}
