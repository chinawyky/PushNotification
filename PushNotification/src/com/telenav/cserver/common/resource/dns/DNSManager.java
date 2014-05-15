/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.dns;

import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.UserProfile;

/**
 * DNSManager.java
 * @author njiang
 * @version 1.0 2012-8-23
 */
public class DNSManager {
	
	public static final String WEB_SERVICE = "web_service";
	
	public static final String SERVICE_LOCATOR = "service_locator";
	
	private static final Logger LOGGER = Logger.getLogger(DNSManager.class);
	
	private DNSManager()
	{}
	
	public static String getURL(UserProfile userProfile, String baseURL, String type) {
		Properties properties = DNSPropertiesCache.getInstance().getPropertiesFromCache(userProfile, type);
		String url = baseURL;
		if(properties != null){
			for(Iterator iter = properties.keySet().iterator(); iter.hasNext();){
				String key = (String)iter.next();
				if(baseURL.contains(key)){
					url = baseURL.replace(key, (String)properties.getProperty(key)).trim();
				}
			}
		}
		LOGGER.debug("url merge result:" + url);
		
		return url;
	}
	
	public static String getURL(String baseURL, String type) {
		
		return getURL(null, baseURL, type);
		
	}
	
}
