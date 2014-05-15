package com.telenav.cserver.service.chunkhandler;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

public interface ChunkCallback 
{
	public void setListener(ChunkReadListener listener);
	
	public void doCallback(ExecutorRequest request, ExecutorResponse response) throws Exception;
	
	public void doEnd();
}
