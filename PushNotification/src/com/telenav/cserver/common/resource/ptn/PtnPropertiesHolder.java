/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ptn;

import java.util.Map;

import com.telenav.cserver.common.resource.AbstractResourceHolder;
import com.telenav.cserver.common.resource.GeneralPtnPropertyManager;
import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * PtnPropertiesHolder.java
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2010-9-1
 *
 */
public class PtnPropertiesHolder extends AbstractResourceHolder
{

	private static final String CONTENT_KEY = "ptn";
	
	public ResourceContent createObject(String key, UserProfile profile, TnContext tnContext)
	{
		Map map = (Map)ResourceFactory.createResource(this, profile, tnContext);
        if (map == null)
        {
            map = GeneralPtnPropertyManager.getGeneralPtnMap();
        }
        
		PtnProperties dp = new PtnProperties(map);
		
		ResourceContent content = new ResourceContent();
		content.setProperty(CONTENT_KEY, dp);
		return content;
	}
	
	public PtnProperties getPtnProperties(UserProfile profile, TnContext tnContext)
	{
		ResourceContent content = getResourceContent(profile, tnContext);
		return (PtnProperties)content.getProperty(CONTENT_KEY);
	}
    
    public void clear()
    {
        super.clear();
        GeneralPtnPropertyManager.clear();
    }
	
}