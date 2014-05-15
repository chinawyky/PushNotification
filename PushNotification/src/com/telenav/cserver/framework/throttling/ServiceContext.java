/**
 * (c) Copyright 2008 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.throttling;

import java.util.Date;

import com.telenav.kernel.util.datatypes.TnContext;

/**
 * Context to describe user serivce
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2008-7-11
 *
 */
public class ServiceContext
{
	private String min = "";
	private TnContext tnContext;
	private long serviceTime = System.currentTimeMillis();
	private int actionType;
	private String serviceType;
		
	/**
	 * get actionId
	 * 
	 * @return
	 */
	public int getActionType()
	{
		return actionType;
	}

	/**
	 * set actionId
	 * 
	 * @param actionType
	 */
	public void setActionType(int actionType)
	{
		this.actionType = actionType;
	}
	
	
	
	public String getServiceType()
	{
		return serviceType;
	}

	public void setServiceType(String serviceType)
	{
		this.serviceType = serviceType;
	}

	public String getMin()
	{
		return min;
	}
	public void setMin(String min)
	{
		this.min = min;
	}
	public TnContext getTnContext()
	{
		return tnContext;
	}
	public void setTnContext(TnContext tnContext)
	{
		this.tnContext = tnContext;
	}
	public long getServiceTime()
	{
		return serviceTime;
	}
	public void setServiceTime(long serviceTime)
	{
		this.serviceTime = serviceTime;
	}
	public String toString()
	{
		return new Date(serviceTime) + " " + tnContext+" ,serviceType:"+serviceType;
	}
	
	
}

