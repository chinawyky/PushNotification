/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.throttling;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;

/**
 * Throttling Configuration
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 * 
 */
public class ThrottlingConfiguration
{
	protected static Logger logger = Logger.getLogger(ThrottlingConfiguration.class);

	private static ThrottlingConfiguration instance = null; 
	private List serviceList = new ArrayList();
	private boolean isEnabled = false;

	static
	{
		try
		{			
			instance = (ThrottlingConfiguration)Configurator.getObject("management/throttling.xml", "throttling_manager");
		
			//logs printing
			logger.info("isEnabled:" + instance.isEnabled);			
			for (int i = 0, size = instance.serviceList.size(); i < size; i++)
			{
				Service service = (Service) instance.serviceList.get(i);
				logger.info(service);
			}
		}
		catch(ConfigurationException e)
		{
			logger.fatal(e, e);
		}
	
	}

	private ThrottlingConfiguration()
	{
		
	}
	
	public List getServiceList()
	{
		return serviceList;
	}
	
	public void setServiceList(List list)
	{
		this.serviceList = list;
	}

	public boolean isEnabled()
	{
		return isEnabled;
	}
	
	public void setEnabled(boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}
	

	public static ThrottlingConfiguration getInstance() 
	{
		return instance;
	}
	
	public static void main(String[] args)
	{
		
	}
}
