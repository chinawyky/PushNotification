/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl.interceptor;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.Interceptor;

/**
 * LoggingInterceptor.java
 *
 * @author sowmit@telenav.com
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public class LoggingInterceptor implements Interceptor 
{

	Logger log = Logger.getLogger(LoggingInterceptor.class);

	public InterceptResult intercept(ExecutorRequest request,
			ExecutorResponse response, ExecutorContext context) 
	{
	    if (log.isDebugEnabled())
	    {
    		if (request != null)
    		{
    			log.debug("request:" + request);
    		}
    		if (response != null)
    		{
    			log.debug("response:" + response);
    		}
	    }
		return InterceptResult.PROCEED;
	}


}
