/**
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.hanlder;

import java.util.HashMap;
import java.util.Map;

/**
 * ResourceHandlerFactory
 * @author kwwang
 *
 */
public class ResourceHandlerFactory {
	
	private Map<String,ResourceHandler> resourceHandlerMap =new HashMap<String,ResourceHandler>();
	
	public Map<String, ResourceHandler> getResourceHandlerMap() {
		return resourceHandlerMap;
	}

	public void setResourceHandlerMap(
			Map<String, ResourceHandler> resourceHandlerMap) {
		this.resourceHandlerMap = resourceHandlerMap;
	}

	public ResourceHandler getResourceHandlerByType(String type)
	{
		return resourceHandlerMap.get(type);
	}
	
}
