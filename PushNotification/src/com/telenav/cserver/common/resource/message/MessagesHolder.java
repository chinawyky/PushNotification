/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.message;

import java.util.Map;

import com.telenav.cserver.common.resource.AbstractResourceHolder;
import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * MessagesHolder.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-7
 *
 */
public class MessagesHolder extends AbstractResourceHolder
{

	private static final String CONTENT_KEY = "messages";
	
	public ResourceContent createObject(String key, UserProfile profile, TnContext tnContext)
	{
		Map map = (Map)ResourceFactory.createResource(this, profile, tnContext);
		Messages dp = new Messages(map);
		
		ResourceContent content = new ResourceContent();
		content.setProperty(CONTENT_KEY, dp);
		return content;
	}
	
	public Messages getMessages(UserProfile profile, TnContext tnContext)
	{
		ResourceContent content = getResourceContent(profile, tnContext);
		return (Messages)content.getProperty(CONTENT_KEY);
	}
	
}