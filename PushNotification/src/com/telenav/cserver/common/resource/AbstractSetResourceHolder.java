/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;


/**
 * AbstractResourceHolder for holders with set config,
 * gives a more easy way to retrive set index and apply in resource loading
 * 
 * @author yqchen
 * @version 1.0 2007-2-9 11:05:03
 */
public abstract class AbstractSetResourceHolder extends AbstractResourceHolder
{
	/**
	 * get set index from wrapper 
	 * 
	 * @param wrapper
	 * @return
	 */
	protected abstract String getSetIndex(UserProfile profile, TnContext tc);
	
	/**
	 * generate key from UserInfoWrapper
	 * 
	 * @param wrapper
	 * @return
	 */
	public String getKey(UserProfile profile, TnContext tc)
	{
		String setIndex = getSetIndex(profile, tc);		
		return super.getKey(profile, tc) + "_" + setIndex;		
	}
}
