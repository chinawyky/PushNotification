/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * the order for loading
 * 
 * @author yqchen
 * @version 2.0 2009-5-6 17:46
 */
public abstract class LoadOrder
{	
	private String type;
		
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
		
		LoadOrderManager.register(this);
	}
	
	/**
	 * get the attribute value from UserProfile
	 * 
	 * @param profile
	 * @param tnContext TODO
	 * @return
	 */
	public abstract String getAttributeValue(UserProfile profile, TnContext tnContext);
    
    /**
     * get attribute value list from UserProfile
     * because some attribute value represents multiple values
     * e.g. the "device" represents both "device" and "resolution"
     * @param profile
     * @param tnContext
     * @return
     */
    public List<String> getAttributeValueList(UserProfile profile, TnContext tnContext)
    {
        List<String> list = new ArrayList<String>();
        String value = getAttributeValue(profile, tnContext);
        if (StringUtils.isNotBlank(value))
        {
            list.add(value);
        }
        return list;
    }

	public String toString()
	{
		return "type:" + type;
	}
}
