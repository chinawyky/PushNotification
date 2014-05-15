/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.config;

/**
 * SimpleServiceConfig.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-13
 */
public class SimpleServiceConfig extends AbstractServiceConfig
{
	private String serviceUrl;
	/**
	 * @param serviceUrl the serviceUrl to set
	 */
	public void setServiceUrl(String serviceUrl)
	{
		this.serviceUrl = serviceUrl;
	}

	/**
	 * @return the serviceUrl
	 */
	public String getServiceUrl()
	{
		return serviceUrl;
	}
}
