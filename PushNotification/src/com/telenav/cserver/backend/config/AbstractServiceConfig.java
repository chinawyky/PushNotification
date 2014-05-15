/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.config;

import java.util.Map;
/**
 * AbstractServiceConfig.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-13
 */
public  class AbstractServiceConfig implements WebServiceConfigInterface
{
	public static final String DEFAULT_ITEM = "default";
	private WebServiceItem webServiceItem;
	
	public String getServiceUrl()
	{
		return null;
	}

	public Map getServiceUrlMapping()
	{
		return null;
	}

	/**
	 * @param webServiceItem the webServiceItem to set
	 */
	public void setWebServiceItem(WebServiceItem webServiceItem)
	{
		this.webServiceItem = webServiceItem;
	}

	/**
	 * @return the webServiceItem
	 */
	public WebServiceItem getWebServiceItem()
	{
		return webServiceItem;
	}

}
