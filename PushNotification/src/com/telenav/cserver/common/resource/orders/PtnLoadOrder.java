/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.telenav.cserver.common.resource.GeneralPtnPropertyManager;
import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.common.resource.ptn.PtnProperties;
import com.telenav.cserver.common.resource.ptn.PtnPropertiesHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * PlatformLoadOrder.java
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2010-9-1
 *
 */
public class PtnLoadOrder extends LoadOrder 
{
    
    //public static final String PTN_LOAD_ORDER = "$PTN_LOAD_ORDER";
    public PtnLoadOrder()
	{
		setType("ptn");
	}
	
	/**
	 * get the attribute value from UserProfile
	 * 
	 * @param profile
	 * @return
	 */
	public String getAttributeValue(UserProfile profile, TnContext tnContext)
	{
		//return PTN_LOAD_ORDER;
		//return profile.getMin();
        PtnPropertiesHolder ptnPropertiesHolder = ResourceHolderManager.getResourceHolder(HolderType.PTN_TYPE);
        if( ptnPropertiesHolder == null )
        {
            return "";
        }
        PtnProperties ptnProperties = ptnPropertiesHolder.getPtnProperties(profile, tnContext);
        String ptn = profile.getMin();
        if (ptn == null)
        {
            return "";
        }
        String ptnDir = ptnProperties.get(ptn);
        return ptnDir==null?"":ptnDir;
	}
	
    
    
    
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
    
    public List<String> getResemblanceFullPath(String fileName, String configFileName, UserProfile profile, TnContext tnContext)
    {
        List<String> list = new ArrayList<String>();
        String ptnResourcePath = GeneralPtnPropertyManager.getPtnResourcePath(profile.getMin());
        if (!"".equals(ptnResourcePath))
        {
            list.add(ptnResourcePath + configFileName);
        }
        return list;
    }
    
    
}
