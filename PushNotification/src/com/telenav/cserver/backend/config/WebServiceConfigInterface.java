/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.config;

import java.util.Map;

/**
 * WebServiceConfigInterface.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-13
 */
public interface WebServiceConfigInterface
{
	public String getServiceUrl();
	
	public Map getServiceUrlMapping();
	
	public WebServiceItem getWebServiceItem();
}
