/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.bar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.telenav.cserver.framework.util.CSStringUtil;

/**
 * ResourceBarCollection.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-14
 *
 */
public class ResourceBarCollection 
{
	private List<ResourceBar> list = new ArrayList<ResourceBar>();
	
	public static final String VERSION = ".version";
	
	private Map metaInformation;

	/**
	 * @return the map
	 */
	public List<ResourceBar> getBarList() {
		return list;
	}

	/**
	 * @param map the map to set
	 */
	public void setBarList(List<ResourceBar> list) {
		this.list = list;
	}
	
	public void addResourceBar(ResourceBar bar)
	{
		list.add(bar);
	}

	/**
	 * @return the metaInformation
	 */
	public Map getMetaInformation() {
		return metaInformation;
	}

	/**
	 * @param metaInformation the metaInformation to set
	 */
	public void setMetaInformation(Map metaInformation) {
		this.metaInformation = metaInformation;
	}
	
	public String toString()
	{
		return "metaInformation:" + metaInformation + "\r\nlist:" + list;
	}
	
	public ResourceBar getResourceBar(String type)
	{
		for(ResourceBar rb:list)
		{
			if(CSStringUtil.isNotNil(rb.getType())&&rb.getType().equalsIgnoreCase(CSStringUtil.removeTheTail(type, VERSION)))
			{
				return rb;
			}
		}
		return null;
	}
}
