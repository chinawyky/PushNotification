/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;
import com.telenav.cserver.framework.executor.Executor;
import com.telenav.cserver.framework.executor.ExecutorRegistry;
import com.telenav.cserver.framework.executor.ExecutorRegistryLoader;

/**
 * DefaultExecutorRegistryLoader.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public class DefaultExecutorRegistryLoader
		implements ExecutorRegistryLoader
{

	public void loadRegistry(ExecutorRegistry registry)
	{		
		try
		{			
			DefaultExecutorRegistryLoader instance 
							= (DefaultExecutorRegistryLoader)Configurator.getObject("executor/executor_mapping.xml", "executor_registry_loader");
			//System.out.println("==================" + registry);
			for(Executor executor : instance.getExecutorList())
			{
				registry.addAction(executor.getExecutorType(), executor);
//				logs printing
				System.out.println("registion:" + executor.getExecutorType() + ", class:"
						+ executor.getClass());	
			}
					
			
		}
		catch(ConfigurationException e)
		{
			e.printStackTrace();
		}
	}
	
	List<Executor> list = new ArrayList<Executor>();
	public void setExecutorList(List<Executor> list)
	{
		this.list = list;
	}
	
	public List<Executor> getExecutorList()
	{
		return this.list;
	}

}
