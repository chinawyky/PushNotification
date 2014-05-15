/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telenav.cserver.framework.executor.Interceptor;
import com.telenav.cserver.framework.executor.InterceptorManager;

/**
 * DefaultInterceptorManager.java
 *
 * @author sowmit@telenav.com
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public class DefaultInterceptorManager implements InterceptorManager 
{
	protected Map<String, List<Interceptor>> preInterceptors
				= new HashMap<String, List<Interceptor>>();
	protected Map<String, List<Interceptor>> postInterceptors
				= new HashMap<String, List<Interceptor>>();

	public DefaultInterceptorManager() 
	{
	}


	public void addPostInterceptor(String serviceType, 
			Interceptor interceptor) 
	{
		List<Interceptor> list = postInterceptors.get(serviceType);
		if (list == null) {
			list = new ArrayList<Interceptor>();
			postInterceptors.put(serviceType, list);
		}
		list.add(interceptor);
	}

	public void addPreInterceptor(String serviceType, 
			Interceptor interceptor) {
		List<Interceptor> list = preInterceptors.get(serviceType);
		if (list == null) {
			list = new ArrayList<Interceptor>();
			preInterceptors.put(serviceType, list);
		}
		list.add(interceptor);
	}

	public Collection<Interceptor> getAllPostInterceptors(
			String serviceType) {
		List<Interceptor> all = new ArrayList<Interceptor>();
		List<Interceptor> serviceInt = postInterceptors.get(serviceType);
		if (serviceInt != null) {
			all.addAll(serviceInt);
		}
		return all;
	}

	public Collection<Interceptor> getAllPreInterceptors(String serviceType) {
		List<Interceptor> all = new ArrayList<Interceptor>();
		List<Interceptor> serviceInt = preInterceptors.get(serviceType);
		if (serviceInt != null) {
			all.addAll(serviceInt);
		}
		return all;
	}

	public void removePostInterceptor(String serviceType,
			Interceptor interceptor) {
		//preInterceptors.remove(serviceType);
		postInterceptors.remove(serviceType);

	}

	public void removePreInterceptor(String serviceType,
			Interceptor interceptor) {
		//postInterceptors.remove(serviceType);
		preInterceptors.remove(serviceType);

	}





	/**
	 * @param preInterceptors the preInterceptors to set
	 */
	public void setInterceptors(
			List<InterceptorConfigurationItem> interceptorEntries) 
	{
		for(InterceptorConfigurationItem entry: interceptorEntries)
		{
			String type = entry.getType();
			if (entry.getPreInterceptors() != null)
            {
                for (Interceptor interceptor : entry.getPreInterceptors())
                {
                    this.addPreInterceptor(type, interceptor);
                }
            }
			
			if (entry.getPostInterceptors() != null)
            {
                for (Interceptor interceptor : entry.getPostInterceptors())
                {
                    this.addPostInterceptor(type, interceptor);
                }
            }
		}
	}


    @Override
    public List<Interceptor> getAllPostGlobalInterceptors()
    {
        List<Interceptor> all = new ArrayList<Interceptor>();
        List<Interceptor> serviceInt = postInterceptors.get(GLOBAL_EXECUTE_TYPE);
        if (serviceInt != null) {
            all.addAll(serviceInt);
        }
        return all;
    }


    @Override
    public List<Interceptor> getAllPreGlobalInterceptors()
    {
        List<Interceptor> all = new ArrayList<Interceptor>();
        List<Interceptor> serviceInt = preInterceptors.get(GLOBAL_EXECUTE_TYPE);
        if (serviceInt != null) {
            all.addAll(serviceInt);
        }
        return all;
    }
	
	

}
