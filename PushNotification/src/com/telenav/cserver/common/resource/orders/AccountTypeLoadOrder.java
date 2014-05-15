/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * AccountTypeLoadOrder.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-6
 *
 */
public class AccountTypeLoadOrder extends LoadOrder 
{
	public AccountTypeLoadOrder()
	{
		setType("account_type");
	}
	
	/**
	 * get the attribute value from UserProfile
	 * 
	 * @param profile
	 * @return
	 */
	public String getAttributeValue(UserProfile profile, TnContext tnContext)
	{
		return UserProfile.DEFAULT_ACCOUNT_TYPE;
	}
	
}