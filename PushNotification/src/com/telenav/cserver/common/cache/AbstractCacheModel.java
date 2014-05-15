/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.cache;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author yqchen
 * @version 1.0 2007-2-7 9:19:30
 */
public abstract class AbstractCacheModel 
	implements CacheModel
{
	Map map = new HashMap();
	
	public void put(Object key, Object value)
	{
		synchronized(map)
		{
			map.put(key, value);
		}
	}
	
	/**
	 * get a object from cache
	 * @param key
	 * @param argument argument to create the object
	 */
	public Object get(Object key,Object argument)
	{
		Object value = map.get(key);
		if(value == null)
		{
			synchronized(map)
			{
				value = map.get(key);
				if(value == null)
				{
					value = createObject(key, argument);
					map.put(key, value);
				}
			}
		}
		return value;
	}
	
	// ONLY FOR DEBUGGING. NEVER USE THIS FOR NORMAL CODE.
	public Map getMap()
	{
		return map;
	}
	
	/**
	 * clear all cache
	 *
	 */
	public void clear()
	{
		map.clear();
		
	}
}
