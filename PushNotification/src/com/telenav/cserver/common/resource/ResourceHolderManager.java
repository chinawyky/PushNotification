/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ResourceHolderManager.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-6
 *
 */
public class ResourceHolderManager{
	private static Map<String, ResourceHolder> map = new HashMap<String, ResourceHolder>();;
	
	static
	{
	    ResourceFactory.getInstance();
	}
	
	/**
	 * register load order
	 * 
	 * @param order
	 */
	public static void register(ResourceHolder holder)
	{
		map.put(holder.getName(), holder);
	}
	
	/**
	 * get load order by type
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings(value={"unchecked"})
	public static <T extends ResourceHolder> T getResourceHolder(String type)
	{
		return (T)map.get(type);
	}

	/**
	 * return all holders
	 * 
	 * @return
	 */
	public static Collection<ResourceHolder> getAllResourceHolder()
	{
		return map.values();
	}
	
	public static String refresh()
	{
		Collection<ResourceHolder> allHolders = ResourceHolderManager.getAllResourceHolder();
		Iterator<ResourceHolder> iterator = allHolders.iterator();
		while(iterator.hasNext())
		{
			ResourceHolder holder = iterator.next();
			holder.clear();
		}
		return "refresh ok";
	}

	
}
