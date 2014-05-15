/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.resource.manager;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Category;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;
import com.telenav.cserver.framework.util.UrlUtil;
import com.telenav.cserver.resource.datatypes.ServiceItem;
import com.telenav.cserver.resource.datatypes.ServiceMapping;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * ServiceMappingManager.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-27
 *
 */
public final class ServiceMappingManager 
{	
	private ServiceMappingManager()
	{}
			
    public static String getUrlByKey(ServiceMapping serviceMapping, UserProfile userProfile, TnContext tnContext, String urlKey)
    {
        if (serviceMapping == null)
        {
            return null;
        }

        List<ServiceItem> serviceItems = serviceMapping.getServiceMapping();
        for (ServiceItem serviceItem : serviceItems)
        {
            if (urlKey.equals(serviceItem.getType()))
            {
                String url = "";
                Iterator<String> iterator = serviceItem.getUrlMap().values().iterator();
                if (iterator.hasNext())
                {
                    url = iterator.next();
                    return WebPageMappingManager.processUrl(url, userProfile, tnContext);
                }
            }
        }

        return "";
    }
    
    
    public static ServiceMapping getUserBasedServiceLocator(ServiceMapping serviceMapping, UserProfile profile, TnContext tnContext)
    {
        //need append the client info in the url, so make deep copy and append the client info 
        ServiceMapping copy = serviceMapping.deepCopy();
        List<ServiceItem> seviceItems = copy.getServiceMapping();
        
        //String clientInfo = JSONUtil.getClientJson(profile);
        
        for(ServiceItem seviceItem: seviceItems)
        {
            Map<String, String> urlMap = seviceItem.getUrlMap();
            Map<String, String> clientUrlMap = seviceItem.getUrlMap();
            Set<Entry<String, String>> set = urlMap.entrySet();
            Iterator<Entry<String, String>> iterator = set.iterator();
            while(iterator.hasNext())
            {
                Entry<String, String> entry = iterator.next();
                String key = entry.getKey();
                String value = entry.getValue();
                value = WebPageMappingManager.processUrl(value, profile, tnContext);
                clientUrlMap.put(key, value);
            }
            
            seviceItem.setUrlMap(clientUrlMap);
            
        }
        
        return copy;
    }	
}
