package com.telenav.cserver.framework.handler;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.data.DataProcessor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

public interface DataHandler
{
	/**
	 * 
	 * @param originalData
	 * @return
	 */
	public boolean parse(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context, boolean isContinue, CliTransaction cli);
	
	/**
	 * get the transportor type
	 * 
	 * @return
	 */
	public String getHandlerType();
	
}
