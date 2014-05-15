package com.telenav.cserver.service.chunkhandler;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

public interface ChunkProcessor 
{
	public ChunkCallback getCallback();
	
	public void setCallback(ChunkCallback callback);
	
	public void process(ExecutorRequest request, ExecutorResponse response, ExecutorContext context) throws Exception;
}
