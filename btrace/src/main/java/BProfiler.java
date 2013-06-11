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


/**
 * <p>BProfiler是Btrace Profiler的简写，写来主要是为了方便排查问题所使用的</p>
 * <p>BTrace版本使用1.2.2</p>
 * 
 * <p style="color:red;">为了我亲爱的老婆luanjia@taobao.com</p>
 * @author dukun@taobao.com
 * @since 2012-10-22
 */
@BTrace
public class BProfiler {

	/**
	 * <p>被探测的类全路径，正则表达式</p>
	 */
	private static final String PROFILER_TAG_CLAZZ = ".*ItemQueryServiceImpl";
	
	/**
	 * <p>被探测的方法名称，正则表达式，不支持区别重载的情况</p>
	 */
	private static final String PROFILER_TAG_METHOD = "queryItemById";
	
	/**
	 * <p>侦测的方法深度，最大只能支持到30</p>
	 */
	private static final int MAX_DEEP = 30;
	
	/**
	 * <p>需要纳入侦测点的范围（类）</p>
	 */
	private static final String PROFILER_RANGE_CLAZZ = "/com\\.taobao\\.item\\.(service|manager|dao|domain|util|search|checkstep).*/";
	
	/**
	 * <p>需要纳入侦测点的范围（方法）</p>
	 */
	private static final String PROFILER_RANGE_METHOD = "/.*/";
	
	
	// --------------------------------------------------- 我是配置分隔符 ---------------------------------------------------
	
	// 当前调用深度
	@TLS private static int invokerDeep = 0;
	
	// 调用总耗时，随着方法的return而增加
	@TLS private static long invokerTotalDuration = 0L;
	
	// 标记当前线程是否已经进入侦测状态，避免重复进入
	@TLS private static boolean isEntered = false;
	
	// 日志堆栈
	@TLS private static Deque<String> logStack = newDeque();
	
	// 是否开始 由Event决定
	private static final int STATUS_STOPED = 0;
	private static final int STATUS_STARTED = 1;
	private static AtomicInteger status = newAtomicInteger(STATUS_STOPED);
	
	/**
	 * 判断当前方法是否已被标记
	 * @param pcn
	 * @param pmn
	 * @return
	 */
	final public static boolean isTagged(String pcn, String pmn) {
		return matches(PROFILER_TAG_CLAZZ, pcn) && matches(PROFILER_TAG_METHOD, pmn);
	}
	
	/**
	 * entry
	 */
	@OnMethod(clazz=PROFILER_RANGE_CLAZZ,method=PROFILER_RANGE_METHOD,location=@Location(value=ENTRY))
	public static void profilerRangeEntry(
			@ProbeClassName String pcn, 
			@ProbeMethodName String pmn) {
		
		if( get(status) != STATUS_STARTED ) {
			return;
		}
		
		if( !isEntered && !isTagged(pcn, pmn) ) {
			return;
		}
		
		isEntered = true;
		invokerDeep++;
		
	}
	
	/**
	 * return
	 */
	@OnMethod(clazz=PROFILER_RANGE_CLAZZ,method=PROFILER_RANGE_METHOD,location=@Location(value=RETURN))
	public static void profilerRangeReturn(
			@ProbeClassName String pcn, 
			@ProbeMethodName String pmn,
			@Duration long duration) {
		
		if( !isEntered ) {
			return;
		}
		
		invokerDeep--;
		invokerTotalDuration+=duration;
		logInvoker(pcn, pmn, duration);
		resetIfTop();
		
	}
	
	/**
	 * 是否已经达到最大深度<br/>
	 * @return true 已大最大深度 / false 未达最大深度
	 */
	final private static boolean isMaxDeep() {
		return invokerDeep > MAX_DEEP;
	}
	
	/**
	 * <p>添加log前缀</p>
	 * <p>因为在BTrace中如果不开启unsafe你是无法使用循环函数的，所以这里只能穷举所有的深度</p>
	 * <p>如果当前调用深度大于最大调用深度，则主动忽略</p>
	 * @param logBuf log日志
	 * @param deep 当前调用深度
	 */
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
			// impossable
			prefix="";break;
		}
		append(logBuf, prefix);
	}
	
	/**
	 * 构造一行log，并将当前log压入队列中<br/>
	 * <p>由于队列是倒序的，这也是为什么BProfiler输出的日志只能倒着看的原因</p>
	 * @param pcn 全类名
	 * @param pmn 方法名
	 * @param duration 当前方法调用耗时
	 */
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
	
	/**
	 * 如果已经到了边界，则需要将日志输出同时清理之前所记录的信息。<br/>
	 */
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
