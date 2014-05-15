package com.telenav.cserver.framework.handler;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

public class EndChunkHandler extends ChunkedDataHandler 
{
	private static Logger logger = Logger.getLogger(EndChunkHandler.class);
	
	public boolean doParse(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Sending End chunk");
		}
		resp.setStatus(ExecutorResponse.STATUS_WRITE_FINISHED);
		return false;
	}
}
