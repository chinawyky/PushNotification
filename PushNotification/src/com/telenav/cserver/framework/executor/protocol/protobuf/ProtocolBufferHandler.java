package com.telenav.cserver.framework.executor.protocol.protobuf;

import com.telenav.cserver.framework.executor.protocol.ProtocolHandler;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;

public class ProtocolBufferHandler implements ProtocolHandler
{
	ProtocolRequestParser requestParser = new ProtocolBufferRequestParser();
    
    ProtocolResponseFormatter responseFormatter = new ProtocolBufferResponseFormatter();
    
	@Override
	public ProtocolRequestParser getRequestParser() 
	{
		return requestParser;
	}
	
    public void setRequestParser(ProtocolRequestParser requestParser) 
    {
		this.requestParser = requestParser;
	}

	@Override
	public ProtocolResponseFormatter getResponseFormatter()
	{
		return responseFormatter;
	}
	
	public void setResponseFormatter(ProtocolResponseFormatter responseFormatter) 
	{
		this.responseFormatter = responseFormatter;
	}
}
