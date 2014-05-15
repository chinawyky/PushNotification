/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.device;

import java.util.Map;

import com.telenav.cserver.common.resource.AbstractResourceHolder;
import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;
 
/**
 * DevicePropertiesHolder.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-6
 *
 */
public class DevicePropertiesHolder extends AbstractResourceHolder
{

	private static final String CONTENT_KEY = "device";
	
	public ResourceContent createObject(String key, UserProfile profile, TnContext tnContext)
	{
		Map map = (Map)ResourceFactory.createResource(this, profile, tnContext);
		DeviceProperties dp = new DeviceProperties(map);
		
		ResourceContent content = new ResourceContent();
		content.setProperty(CONTENT_KEY, dp);
		return content;
	}
	
	public DeviceProperties getDeviceProperties(UserProfile profile, TnContext tnContext)
	{
		ResourceContent content = getResourceContent(profile, tnContext);
		return (DeviceProperties)content.getProperty(CONTENT_KEY);
	}
	
}
