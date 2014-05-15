package com.telenav.cserver.service.chunkhandler;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

public class ChunkCallbackImpl implements ChunkCallback
{
	ChunkReadListener listener = null;

	public ChunkCallbackImpl()
	{
	}
	
	public void setListener(ChunkReadListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public void doCallback(ExecutorRequest request, ExecutorResponse response) throws Exception
	{
		if(listener != null)
        {
        	listener.handleResponse(request, response);
        }
        else
        {
        	throw new Exception("No Chunk Listener defined");
        }
	}
	
	public void doEnd()
	{
		if(listener != null)
		{
			listener.finish();
		}
	}

}
