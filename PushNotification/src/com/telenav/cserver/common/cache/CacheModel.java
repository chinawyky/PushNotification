/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.cache;

/**
 * model to be used for caching
 * 
 * @author yqchen
 * @version 1.0 2007-2-7 9:18:13
 */
public interface CacheModel
{
//	public void put(Object key, Object value);
	
	/**
	 * get a object from cache
	 * @param key
	 * @param argument argument to create the object
	 */
	public Object get(Object key,Object argument);
	
	/**
	 * create a object 
	 * 
	 * @param key
	 * @param argument argument to create the object
	 * @return
	 */
	public Object createObject(Object key, Object argument);
	
	/**
	 * clear all cache
	 *
	 */
	public void clear();
}
