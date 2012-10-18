/**
 * 
 */
package com.taobao.top.xbox.framework.event;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.taobao.top.xbox.framework.http.HttpPipeData;
import com.taobao.top.xbox.framework.http.HttpPipeInput;
import com.taobao.top.xbox.framework.http.HttpPipeResult;

/**
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-5-5
 *
 */
public class AsynHttpTaskEvent <I extends HttpPipeInput, R extends HttpPipeResult,
					D extends HttpPipeData<I,R>> extends AsynTaskEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -776369268728830480L;
	private static final Log logger = LogFactory.getLog(AsynHttpTaskEvent.class);
	
	D context;
	
	public AsynHttpTaskEvent(D source,long timeout) {
		super(source,timeout);
		context = source;
	}

	
	public D getContext() {
		return context;
	}

	public void setContext(D context) {
		this.context = context;
	}



	@Override
	public void doTimeOut() {
		//由于当前Continuation的timeout已经和Event的保持一致
		//，因此这里不需要对Continuation的资源做回收
		if (logger.isInfoEnabled())
			logger.info("timeout release resource...");

	}


	@Override
	public AsynTaskEvent cloneEvent(boolean needStatus) {
		AsynHttpTaskEvent<I,R,D> clone = new AsynHttpTaskEvent<I,R,D>(this.context,this.timeout);
		clone.setTasks(this.getTasks());
		
		if (needStatus)
			clone.setStatus(this.getStatus());
		
		return clone;
	}
	
}
