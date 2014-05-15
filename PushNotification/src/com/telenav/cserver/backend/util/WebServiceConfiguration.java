/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;

/**
 * WebServiceManager.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-10
 */
public class WebServiceConfiguration
{
	static Logger logger = Logger.getLogger(WebServiceConfiguration.class);
	private static WebServiceConfiguration instance = new WebServiceConfiguration();
	public static final String DEFAULT_SERVICE_CONFIG = "DEFAULT-WS";
	static
	{
		try
		{
			instance = (WebServiceConfiguration)Configurator.getObject("config/web-service.xml", "webservice-configuration");
		}
		catch(ConfigurationException e)
		{
			logger.fatal(e, e);
			e.printStackTrace();
		}
	}
	
	public static WebServiceConfiguration getInstance()
	{
		return instance;
	}

	private Map<String, WebServiceConfigInterface> serviceConfigMap = new HashMap<String, WebServiceConfigInterface>();

	/**
	 * @param serviceConfigMap the serviceConfigMap to set
	 */
	public void setServiceConfigMap(Map<String, WebServiceConfigInterface> serviceConfigMap)
	{
		this.serviceConfigMap = serviceConfigMap;
	}

	/**
	 * @return the serviceConfigMap
	 */
	public Map<String, WebServiceConfigInterface> getServiceConfigMap()
	{
		return serviceConfigMap;
	}
	public WebServiceConfigInterface getServiceConfig(String serviceName)
	{
		WebServiceConfigInterface serviceConfig = null;
		if(serviceConfigMap != null)
		{
			serviceConfig = (WebServiceConfigInterface)serviceConfigMap.get(serviceName);
		}
		return serviceConfig;
	}
	


	public static void main(String args[])
	{
		WebServiceConfiguration wsConfig = WebServiceConfiguration.getInstance();
		WebServiceConfigInterface ws = wsConfig.getServiceConfig("FEEDBACK");
		System.out.println(ws.getServiceUrl());
		System.out.println(ws.getWebServiceItem().getWebServiceMaxConnectionPerHost());
		
		WebServiceConfigInterface wss = wsConfig.getServiceConfig("POI_SEARCH");
		System.out.println(wss.getServiceUrlMapping());
		System.out.println(wss.getWebServiceItem().getWebServiceMaxConnectionPerHost());
	}


}
