/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * ProgramCodeLoadOrder
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2011-4-27
 *
 */
public class ProgramCodeLoadOrder extends LoadOrder 
{
	public ProgramCodeLoadOrder()
	{
		setType("programcode");//must be low case:( check LoadOrders.addOrderString
	}
	
	/**
	 * get the attribute value from UserProfile
	 * 
	 * @param profile
	 * @return
	 */
	public String getAttributeValue(UserProfile profile, TnContext tnContext)
	{
		return profile.getProgramCode();
	}
	
}
