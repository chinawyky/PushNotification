/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.config;

import java.util.Map;

/**
 * PoiSearchServiceConfig.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-13
 */
public class PoiSearchServiceConfig extends AbstractServiceConfig
{
	private Map<String, String> serviceUrlMapping;
	public void setServiceUrlMapping(Map<String, String> mapping)
	{
		this.serviceUrlMapping = mapping;
	}
	public Map<String, String> getServiceUrlMapping()
	{
		return serviceUrlMapping;
	}
}
