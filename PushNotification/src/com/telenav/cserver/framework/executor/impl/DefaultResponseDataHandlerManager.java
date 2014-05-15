package com.telenav.cserver.framework.executor.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telenav.cserver.framework.executor.ResponseDataHandlerManager;
import com.telenav.cserver.framework.handler.DataHandler;


public class DefaultResponseDataHandlerManager implements ResponseDataHandlerManager
{
	protected Map<String, List<DataHandler>> dataHandlers = new HashMap<String, List<DataHandler>>();

	public DefaultResponseDataHandlerManager()
	{		
	}

	public void setHandlerList(List<ResponseDataHandlerItem> handlerItems) 
	{
		for(ResponseDataHandlerItem item : handlerItems)
		{
			String type = item.getHandlerType();
			if(item.getHandlers() != null)
			{
				for(DataHandler parser : item.getHandlers())
				{
					this.addDataHandlers(type, parser);
				}
			}
		}
	}
	
	public void addDataHandlers(String type, DataHandler parser)
	{
	    List<DataHandler> list = dataHandlers.get(type);	
	    if(list == null)
	    {
	    	list = new ArrayList<DataHandler>();
	    	dataHandlers.put(type, list);
	    }
	    list.add(parser);
	}
	
	public List<DataHandler> getDataHandlers(String type)
	{
		List<DataHandler> list = dataHandlers.get(type);
		return list;
	}
	
}
