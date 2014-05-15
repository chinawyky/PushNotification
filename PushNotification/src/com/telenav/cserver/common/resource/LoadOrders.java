/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.telenav.cserver.common.resource.constant.ResourceConst;


/**
 * LoadOrders.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-6
 *
 */
public class LoadOrders 
{
	private static final Logger LOGGER = Logger.getLogger(LoadOrders.class);
	private static final int LIST_SIZE = 4;

	private final List<LoadOrder> orders = new ArrayList<LoadOrder>(LIST_SIZE);
	
	public List<LoadOrder> getOrders()
	{
		return orders;
	}
	
	
	public void addOrder(LoadOrder order)
	{
		orders.add(order);
	}
	

	private boolean lowerCase;
	
	/**
	 * whether resource file name is in lower case
	 * 
	 * @return
	 */
	public boolean isLowerCase()
	{
		return lowerCase;
	}
	
	public void setLowerCase(boolean lowerCase)
	{
		this.lowerCase = lowerCase;
	}

	/**
	 * add string as order, ignore case, seperated by ','
	 * 
	 * @param orders like "locale", "locale,platform"
	 */
	public void addOrderString(String orderString)
	{
		if(LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Parsing orderString:" + orderString);
		}
		if(orderString == null || orderString.length() == 0)
		{
			return;
		}
		StringTokenizer token = new StringTokenizer(orderString, ",");
		while(token.hasMoreTokens())
		{
			String str = token.nextToken().trim().toLowerCase();
			LoadOrder loadOrder = LoadOrderManager.getLoadOrder(str);
			if(loadOrder == null)
			{
				// warning log
				LOGGER.fatal("Unexpected load order:" + str);
			}
			else
			{
				addOrder(loadOrder);
			}
				

		}
	}
	
	public String toString()
	{
		return "isLowerCase: " + isLowerCase() + ",orders:" + orders;
	}
}
