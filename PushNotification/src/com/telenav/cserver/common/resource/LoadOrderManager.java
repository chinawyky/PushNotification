/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * LoadOrderManager.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-6
 *
 */
public class LoadOrderManager 
{
	private static Map<String, LoadOrder> map = new HashMap<String, LoadOrder>();;
	
	/**
	 * register load order
	 * 
	 * @param order
	 */
	public static void register(LoadOrder order)
	{
		map.put(order.getType(), order);
	}
	
	/**
	 * get load order by type
	 * 
	 * @param type
	 * @return
	 */
	public static LoadOrder getLoadOrder(String type)
	{
		return map.get(type);
	}
}
