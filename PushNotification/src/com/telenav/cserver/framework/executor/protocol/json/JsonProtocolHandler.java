package com.telenav.cserver.framework.executor.protocol.json;

import com.telenav.cserver.framework.executor.protocol.ProtocolHandler;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;

public class JsonProtocolHandler implements ProtocolHandler
{
    ProtocolRequestParser requestParser = new JsonRequestParser();
    
    ProtocolResponseFormatter responseFormatter = new JsonResponseFormatter();
    
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
