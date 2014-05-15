/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.HashMap;
import java.util.Map;

import com.telenav.cserver.common.cache.MemorySize;
import com.telenav.j2me.datatypes.TxNode;

/**
 * ResourceContent: to reprensent the content of special resource
 * 
 * @author yqchen
 * @version 1.0 2007-2-7 10:43:00
 */
public class ResourceContent implements MemorySize, java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2955746988933064909L;
	private static final int MAP_SIZE = 4;
	private TxNode[] nodes;
	private String version;
	private volatile Map props;
	//object property is added for holding object returned by spring type loader
	private Object object;
	private long   size;
	
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	public TxNode getNode()
	{
		if(nodes != null && nodes.length == 1)
		{
			return nodes[0];			
		}
		return null;
	}
	
	

	public void setNode(TxNode node)
	{
		if(node != null)
		{
			this.nodes = new TxNode[]{node};
		}
		
	}
	
	public void setNodes(TxNode[] nodes)
	{
		this.nodes = nodes;
	}
	
	public TxNode[] getNodes()
	{
		return this.nodes;
	}
	
	public Map getProps()
	{		
		if(props == null)
		{
			synchronized(this)
			{
				if(props == null)
				{
					props = new HashMap(MAP_SIZE);
				}
			}
		}
		
		return props;
	}
	
	public void setProps(Map props)
	{
		this.props = props;
	}
	
	public void setProperty(Object key, Object value)
	{
		getProps().put(key, value);
	}
	
	public Object getProperty(Object key)
	{
		return getProps().get(key);
	}
	
	public String getVersion()
	{
		return version;
	}
	public void setVersion(String version)
	{
		this.version = version;
	}

	public long getMemorySize() {
		return size;
	}

	public void setMemorySize(long size) {
		this.size = size;
	}
	
	
	
}
