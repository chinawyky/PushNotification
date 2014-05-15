/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.resource.common;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.Assert;

import com.telenav.cserver.common.resource.AbstractResourceHolder;
import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.common.resource.SpringObjectNameAware;
import com.telenav.cserver.common.resource.dns.DNSManager;
import com.telenav.cserver.common.resource.dns.DNSPropertiesCache;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.resource.datatypes.BrowserServiceItem;
import com.telenav.cserver.resource.datatypes.ServiceItem;
import com.telenav.cserver.resource.datatypes.ServiceMapping;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * This class will deal with the Service locator file.
 * @author kwwang
 *
 */
public class ServiceLocatorHolder extends AbstractResourceHolder implements SpringObjectNameAware{

	@Override
	public ResourceContent createObject(String key, UserProfile profile,
			TnContext tnContext) {
		//FIXME:this assert should be replaced with our own one.
		Assert.notNull(profile, "UserProfile can not be null.");
		
		Object result=ResourceFactory.createResource(this, profile, tnContext);
		ServiceMapping serviceMapping = (ServiceMapping)result;
		if(serviceMapping!=null){
			List<ServiceItem> itemList = serviceMapping.getServiceMapping();
			Map<String, String> urlMap = null;
			String url = "";
			String baseUrl = "";
			Entry<String, String> entry = null;
			for(ServiceItem item : itemList){
				if(item instanceof BrowserServiceItem){
					baseUrl = ((BrowserServiceItem)item).getBaseUrl();
					url = DNSManager.getURL(profile, baseUrl, DNSManager.SERVICE_LOCATOR);
					((BrowserServiceItem) item).setBaseUrl(url);
				}
				else{
					urlMap = item.getUrlMap();
					for(Iterator iter = urlMap.entrySet().iterator(); iter.hasNext();){
						entry = (Entry<String, String>)iter.next();
						url = DNSManager.getURL(profile, entry.getValue(), DNSManager.SERVICE_LOCATOR);
						urlMap.put(entry.getKey(), url);
					}
				}
			}
		}
		
		ResourceContent rc=new ResourceContent();
		rc.setObject(serviceMapping);
		
		return rc;
	}

	@Override
	public String getObjectName() {
		return "basic_service_mapping";
	}

}
