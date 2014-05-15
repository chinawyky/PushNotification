/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Resource Set: a set to manage a mapping relation 
 * between index and config/description/others
 * 
 * 
 * @author yqchen
 * @version 1.0 2007-2-6 9:44:32
 */
public class ResourceSet
{
	/**
	 * set name
	 */
	private String name;
	
	private String dir;
	
	private Map mapping ;

	public Map getMapping()
	{
		if(mapping == null)
		{
			mapping = new HashMap(8);
		}
		return mapping;
	}

	public void setMapping(Map mapping)
	{
		this.mapping = mapping;
	}
	
	/**
	 * add mapping
	 * 
	 * @param key
	 * @param value can be config file name, description or other String
	 */
	public void addConfig(String key, String value)
	{
		getMapping().put(key, value);
	}
	
	/**
	 * return value
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key)
	{
		if(dir != null)
		{
			return dir + "." + (String)getMapping().get(key);
		}
		else
		{
			return (String)getMapping().get(key);
		}
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setDir(String dir)
	{
		this.dir = dir;
	}
	
	
}
