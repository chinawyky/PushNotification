/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;


/**
 *
 * @author yqchen
 * @version 1.0 2007-2-6 9:41:04
 */
public interface ResourceLoader
{
	/**
	 * load resource 
	 * 
	 * @param path
	 * @param objectName
	 * @return Object, Map for properties, Element for XML
	 */
	public Object loadResource(String path, String objectName);
}
