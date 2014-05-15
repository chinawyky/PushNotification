package com.telenav.cserver.framework.executor;

import java.util.List;

import com.telenav.cserver.framework.executor.impl.ResponseDataHandlerItem;
import com.telenav.cserver.framework.handler.DataHandler;



public interface ResponseDataHandlerManager 
{
	public void setHandlerList(List<ResponseDataHandlerItem> parserItems);
	
	
	public void addDataHandlers(String type, DataHandler parser);
	
	
	public List<DataHandler> getDataHandlers(String type);
	
}
