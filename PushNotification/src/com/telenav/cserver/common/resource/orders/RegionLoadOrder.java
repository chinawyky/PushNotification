/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.resource.datatypes.RegionGroup;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * RegionLoadOrder.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-6
 *
 */
public class RegionLoadOrder  extends LoadOrder 
{
	public RegionLoadOrder()
	{
		setType("region");
	}
	
	/**
	 * get the attribute value from UserProfile
	 * 
	 * @param profile
	 * @return
	 */
	public String getAttributeValue(UserProfile profile, TnContext tnContext)
	{
	    return RegionGroup.getRegionGroup(profile.getRegion());
	}
	
}