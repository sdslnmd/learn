import static com.sun.btrace.BTraceUtils.addLast;
import static com.sun.btrace.BTraceUtils.get;
import static com.sun.btrace.BTraceUtils.lastIndexOf;
import static com.sun.btrace.BTraceUtils.matches;
import static com.sun.btrace.BTraceUtils.newAtomicInteger;
import static com.sun.btrace.BTraceUtils.newDeque;
import static com.sun.btrace.BTraceUtils.printArray;
import static com.sun.btrace.BTraceUtils.println;
import static com.sun.btrace.BTraceUtils.set;
import static com.sun.btrace.BTraceUtils.size;
import static com.sun.btrace.BTraceUtils.str;
import static com.sun.btrace.BTraceUtils.strlen;
import static com.sun.btrace.BTraceUtils.substr;
import static com.sun.btrace.BTraceUtils.Collections.toArray;
import static com.sun.btrace.BTraceUtils.Strings.append;
import static com.sun.btrace.BTraceUtils.Strings.newStringBuilder;
import static com.sun.btrace.annotations.Kind.ENTRY;
import static com.sun.btrace.annotations.Kind.RETURN;

import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Duration;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnEvent;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.ProbeClassName;
import com.sun.btrace.annotations.ProbeMethodName;
import com.sun.btrace.annotations.TLS;

@BTrace
public class BProfiler {

    //com.zuche.common.inventory.calculate

	private static final String PROFILER_TAG_CLAZZ = "/.*inventory.*/";

	private static final String PROFILER_TAG_METHOD = "/.*/";

	private static final int MAX_DEEP = 30;

	private static final String PROFILER_RANGE_CLAZZ = "/com\\.zuche\\.common\\..*/";

	private static final String PROFILER_RANGE_METHOD = "/.*/";



	@TLS private static int invokerDeep = 0;

	@TLS private static long invokerTotalDuration = 0L;

	@TLS private static boolean isEntered = false;

	@TLS private static Deque<String> logStack = newDeque();

	private static final int STATUS_STOPED = 0;
	private static final int STATUS_STARTED = 1;
	private static AtomicInteger status = newAtomicInteger(STATUS_STOPED);


	final public static boolean isTagged(String pcn, String pmn) {
		return matches(PROFILER_TAG_CLAZZ, pcn) && matches(PROFILER_TAG_METHOD, pmn);
	}


	@OnMethod(clazz=PROFILER_RANGE_CLAZZ,method=PROFILER_RANGE_METHOD,location=@Location(value=ENTRY))
	public static void profilerRangeEntry(@ProbeClassName String pcn, @ProbeMethodName String pmn) {

		if( get(status) != STATUS_STARTED ) {
			return;
		}

		if( !isEntered && !isTagged(pcn, pmn) ) {
			return;
		}

		isEntered = true;
		invokerDeep++;

	}


	@OnMethod(clazz=PROFILER_RANGE_CLAZZ,method=PROFILER_RANGE_METHOD,location=@Location(value=RETURN))
	public static void profilerRangeReturn(@ProbeClassName String pcn, @ProbeMethodName String pmn, @Duration long duration) {

		if( !isEntered ) {
			return;
		}

		invokerDeep--;
		invokerTotalDuration+=duration;
		logInvoker(pcn, pmn, duration);
		resetIfTop();

	}

	final private static boolean isMaxDeep() {
		return invokerDeep > MAX_DEEP;
	}


	private static void appendLogPrefix(Appendable logBuf, int deep) {
		if( isMaxDeep() ) {
			return;
		}
		final String prefix;
		switch (deep) {
		case  0:prefix="+---";break;
		case  1:prefix="|   +---";break;
		case  2:prefix="|   |   +---";break;
		case  3:prefix="|   |   |   +---";break;
		case  4:prefix="|   |   |   |   +---";break;
		case  5:prefix="|   |   |   |   |   +---";break;
		case  6:prefix="|   |   |   |   |   |   +---";break;
		case  7:prefix="|   |   |   |   |   |   |   +---";break;
		case  8:prefix="|   |   |   |   |   |   |   |   +---";break;
		case  9:prefix="|   |   |   |   |   |   |   |   |   +---";break;
		case 10:prefix="|   |   |   |   |   |   |   |   |   |   +---";break;
		case 11:prefix="|   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 12:prefix="|   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 13:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 14:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 15:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 17:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 18:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 19:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 20:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 21:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 22:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 23:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 24:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 25:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 26:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 27:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 28:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 29:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		case 30:prefix="|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   +---";break;
		default:
			prefix="";break;
		}
		append(logBuf, prefix);
	}


	private static void logInvoker(String pcn, String pmn, long duration) {
		if( isMaxDeep() ) {
			return;
		}
		final Appendable logBuf = newStringBuilder(false);
		append(logBuf, "\n");
		appendLogPrefix(logBuf, invokerDeep);
		append(logBuf, "[");append(logBuf, str(duration/1000000));append(logBuf, "ms,");append(logBuf, str(invokerTotalDuration/1000000));append(logBuf, "ms] - ");
		append(logBuf, substr(pcn, lastIndexOf(pcn,".")+1, strlen(pcn)));append(logBuf, ".");append(logBuf, pmn);
		addLast(logStack, str(logBuf));
	}

	final private static void resetIfTop() {
		if( invokerDeep == 0 ) {
			addLast(logStack, "\n");
			printArray(toArray(logStack));
			logStack = newDeque();
			invokerTotalDuration = 0L;
			isEntered = false;
		}
	}

	@OnEvent("show")
	public static void showStatus() {
		final Appendable showBuf = newStringBuilder(true);
		append(showBuf, ";status=");append(showBuf, str(status));
		append(showBuf, ";invokerDeep=");append(showBuf, str(invokerDeep));
		append(showBuf, ";invokerTotalDuration=");append(showBuf, str(invokerTotalDuration));
		append(showBuf, ";isEntered=");append(showBuf, str(isEntered));
		append(showBuf, ";logStack.size=");append(showBuf, str(size(logStack)));
		println(str(showBuf));
	}

	@OnEvent("start")
	public static void startOnEvent() {
		set(status, STATUS_STARTED);
		println("b-profiler was start");
	}

	@OnEvent("stop")
	public static void stopOnEvent() {
		set(status, STATUS_STOPED);
		println("b-profiler was stop");
	}

}
