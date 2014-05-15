package com.telenav.cserver.service.chunkhandler;

import com.telenav.cserver.framework.ServerException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

public interface ChunkReadListener 
{
    public boolean readChunk(byte[] chunk) throws ServerException;
    
    public void handleResponse(ExecutorRequest request, ExecutorResponse response);
    
    public void finish();
    
    public void await() throws ServerException;
    
    public void awake();
}
