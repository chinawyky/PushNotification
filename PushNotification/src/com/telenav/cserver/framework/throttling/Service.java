/**
 * (c) Copyright 2008 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.throttling;

import java.util.ArrayList;
import java.util.List;

import com.telenav.kernel.util.datatypes.TnContext;

/**
 * C-Server service
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2008-7-11
 *
 */
public class Service
{

	/**
	 * service name
	 */
	private String name;
	
	/**
	 * a list of service name belong to this service	
	 */
	private String[] serviceTypes;
	
	/**
	 * max allowed concurrent online number
	 */
	private int maxAllowedOnlineNumber;
	
	/**
	 * concurrent online number
	 */
	private int onlineNumber;
	
	/**
	 * online context list
	 */
	private ArrayList onlineContextList;

	/**
	 * get service name
	 * 
	 * @return
	 */

	public String getName()
	{
		return name;
	}
	
	/**
	 * set service name
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * it is a list of service types
	 * 
	 * @return
	 */
	public String[] getServiceTypes()
	{
		return serviceTypes;
	}
	
	/**
	 * set service types
	 * 
	 * @param actionIDs
	 */
	public void setServiceTypes(String[] serviceTypes)
	{
		this.serviceTypes = serviceTypes;
	}
	
	
	/**
	 * return max allowed concurrent online number
	 * 
	 * @return
	 */
	public int getMaxAllowedOnlineNumber()
	{
		return maxAllowedOnlineNumber;
	}

	/**
	 * set max allowed concurrent online number
	 * 
	 * @param maxAllowedOnlineNumber
	 */
	public void setMaxAllowedOnlineNumber(int maxAllowedOnlineNumber)
	{
		this.maxAllowedOnlineNumber = maxAllowedOnlineNumber;
	}
	
	/**
	 * can we add one more online user
	 * 
	 * @return
	 */
	public boolean hasReachMaxOnlineNumber()
	{
		return onlineNumber >= maxAllowedOnlineNumber;
	}
	
	/**
	 * increase online number
	 */
	public void increaseOnlineUser()
	{
		onlineNumber ++;
	}
	
	/**
	 * decrease online number
	 */
	public void decreaseOnlineUser()
	{
		onlineNumber --;
	}

	/**
	 * return concurrent online number for this service
	 * 
	 * @return
	 */
	public int getOnlineNumber()
	{
		return onlineNumber;
	}
	
	/**
	 * set concurrent online number for this service
	 * 
	 * @param onlineNumber
	 */
	public void setOnlineNumber(int onlineNumber)
	{
		this.onlineNumber = onlineNumber;
	}
	
	/**
	 * return online context list
	 * 
	 * @return
	 */
	public ArrayList getOnlineContextList()
	{
		if(onlineContextList == null)
		{
			onlineContextList = new ArrayList();
		}
		return onlineContextList;
	}
	
	/**
	 * set online context list
	 * 
	 * @param onlineContextList
	 */
	public void setOnlineContextList(ArrayList onlineContextList)
	{
		this.onlineContextList = onlineContextList;
	}
	
	/**
	 * add online context
	 * 
	 * @param context
	 */
	public void addOnlineContext(TnContext context, String serviceType)
	{
		ServiceContext serviceContext = new ServiceContext();
		String login = context.getProperty(TnContext.PROP_LOGIN_NAME);
		if(login != null)
		{
			serviceContext.setMin(login);
		}
		serviceContext.setTnContext(context);
	//	serviceContext.setActionType(actionType);
		serviceContext.setServiceType(serviceType);
		getOnlineContextList().add(serviceContext);
	}
	
	/**
	 * remove online context
	 * 
	 * @param context
	 */
	public void removeOnlineContext(TnContext context)
	{
		List list = getOnlineContextList();
		for(int i = 0, size = list.size(); i < size; i ++)
		{
			ServiceContext serviceContext = (ServiceContext)list.get(i);
			String login = context.getProperty(TnContext.PROP_LOGIN_NAME);
			if(login == null) login = "";
			if(serviceContext.getMin().equalsIgnoreCase(login))
			{
				list.remove(i);
				return;
			}
		}
	}

	/**
	 * is the action id belong to the service
	 * 
	 * @param serviceType
	 * @return
	 */
	public boolean isInService(String serviceType)
	{
		int size = serviceTypes == null ? 0: serviceTypes.length;		
		for(int i = 0; i < size; i ++)
		{
			if(serviceTypes[i].equalsIgnoreCase(serviceType))
			{
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		StringBuffer serviceTypesString = new StringBuffer();
		for(int i = 0; serviceTypes != null && i < serviceTypes.length; i ++)
		{
			serviceTypesString.append(serviceTypes[i]).append(":");
		}
		return "total online number: "
			+ getOnlineNumber()
			+ " for service:" + this.getName()
			+ ",MaxAllowedOnlineNumber:" + this.getMaxAllowedOnlineNumber()
		    + ",Service:" + serviceTypesString;
	}
	
	
	
}
