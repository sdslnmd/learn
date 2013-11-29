import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;


@BTrace public class OnThrow {    

    @TLS static Throwable currentException;


    @OnMethod(
        clazz="java.lang.Throwable",
        method="<init>"
    )
    public static void onthrow(@Self Throwable self) {
        currentException = self;
    }

    @OnMethod(
        clazz="java.lang.Throwable",
        method="<init>"
    )
    public static void onthrow1(@Self Throwable self, String s) {
        currentException = self;
    }

    @OnMethod(
        clazz="java.lang.Throwable",
        method="<init>"
    )
    public static void onthrow1(@Self Throwable self, String s, Throwable cause) {
        currentException = self;
    }

    @OnMethod(
        clazz="java.lang.Throwable",
        method="<init>"
    )
    public static void onthrow2(@Self Throwable self, Throwable cause) {
        currentException = self;
    }

    @OnMethod(
        clazz="java.lang.Throwable",
        method="<init>",
        location=@Location(Kind.RETURN)
    )
    public static void onthrowreturn() {
        if (currentException != null) {
            BTraceUtils.Threads.jstack();
            BTraceUtils.println("=====================");
            currentException = null;
        }
    }
}
