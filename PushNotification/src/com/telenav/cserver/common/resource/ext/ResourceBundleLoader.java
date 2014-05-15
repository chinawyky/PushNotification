/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import org.apache.log4j.Category;

import com.telenav.cserver.common.resource.ResourceLoader;

/**
 * ResourceBundle Loader, to load properties and return as Map Object
 * 
 * @author yqchen
 * @version 1.0 2007-2-6 9:43:20
 */
public class ResourceBundleLoader 
	implements ResourceLoader
{
	Category logger = Category.getInstance(getClass());
	
	/**
	 * load resource 
	 * 
	 * @param path
	 * @param objectName
	 * @return Object, Map for properties, Element for XML
	 */
	public Object loadResource(String path, String objectName)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Loading resource:" + path);
		}
		TelenavResourceBundler trb = new TelenavResourceBundler(path, null, null);
		return trb.getProperties();
	}
	
	
}
