package com.telenav.cserver.service.chunkhandler;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.Interceptor;
import com.telenav.cserver.framework.executor.Interceptor.InterceptResult;
import com.telenav.cserver.framework.executor.InterceptorManager;
import com.telenav.kernel.util.datatypes.TnContext;

public abstract class AbstractChunkProcssor implements ChunkProcessor
{
	private static Logger logger = Logger.getLogger(AbstractChunkProcssor.class);
	
    public String processorName;

	public ChunkCallback callback;

	public String getProcessorName() {
		return processorName;
	}

	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}
	
	public ChunkCallback getCallback() {
		return callback;
	}

	public void setCallback(ChunkCallback callback) {
		this.callback = callback;
	}
	
	public void process(ExecutorRequest request, ExecutorResponse response, ExecutorContext context) throws Exception
	{
		String processType = request.getExecutorType();
		long hsStartTime = System.currentTimeMillis();
		CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
		cli.setFunctionName(processType);
		UserProfile user = request.getUserProfile();
		String minInfo = "";
		if(user != null)
		{
			minInfo = "min:" + user.getMin();
		}
		logger.info("=========== Star Action = " + processType + " =========" + minInfo);
		//TnContext tc = context.getTnContext();
		try
		{
			InterceptorManager interceptorManager = ExecutorDispatcher.getInstance().getInterceptorManager();
			Collection<Interceptor> preIntList = interceptorManager.getAllPreInterceptors(processType);
			preIntList.addAll(interceptorManager.getAllPreGlobalInterceptors());
			Iterator<Interceptor> preIntIter = preIntList.iterator();
			while(preIntIter.hasNext())
			{
				Interceptor interceptor = preIntIter.next();
				InterceptResult result = interceptor.intercept(request, response, context);
				if(result == InterceptResult.HALT)
				{
					logger.fatal("HALT!!! in " + interceptor.toString());
					cli.addData(CliConstants.LABEL_ERROR, "Halt in pre-interceptor:" + interceptor.toString());
					return;
				}
			}
			
			doProcess(request, response, context);
			
			Collection<Interceptor> postIntList = interceptorManager.getAllPostInterceptors(processType);
			postIntList.addAll(interceptorManager.getAllPostGlobalInterceptors());
			Iterator<Interceptor> postIntIter = postIntList.iterator();
			while(postIntIter.hasNext())
			{
				Interceptor interceptor = postIntIter.next();
				InterceptResult result = interceptor.intercept(request, response, context);
				if(result == InterceptResult.HALT)
				{
					logger.fatal("HALT!!! in " + interceptor.toString());
					cli.addData(CliConstants.LABEL_ERROR, "Halt in post-interceptor:" + interceptor.toString());
					return;
				}
			}
			long lDuration = System.currentTimeMillis() - hsStartTime;
			logger.info("===== End Action = " + processType + ", " + lDuration + " ms =====" + minInfo);
		}
		finally
		{
			cli.complete();
		}
	}
    
    public abstract void doProcess(ExecutorRequest request, ExecutorResponse response, ExecutorContext context) throws Exception;
}
