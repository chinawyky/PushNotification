/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import java.util.HashMap;
import java.util.Map;

/**
 * Executor Registry
 *
 * @author sowmit@telenav.com
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public class ExecutorRegistry 
{
	private Map<String, Executor> actionRegistry = new HashMap<String, Executor>();


	public void addAction(String type,
			Executor actionInstance) {
		actionRegistry.put(type, actionInstance);
	}
	
	public Executor getAction(String type)
			throws ExecutorException {
		return actionRegistry.get(type);
	}

}
