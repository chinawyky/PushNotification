/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.util;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.telenav.cserver.backend.config.WebServiceConfigInterface;

/**
 * WebServiceUtils.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-6-4
 * 
 */
public class WebServiceUtils
{

    private static final Logger logger = Logger.getLogger(WebServiceUtils.class);

    public static ConfigurationContext createConfigurationContext(String configName)
    {
        ConfigurationContext configurationContext = null;
        WebServiceConfigInterface ws = WebServiceConfiguration.getInstance().getServiceConfig(configName);
        try
        {
            configurationContext = WebServiceManager.createNewContext(ws.getWebServiceItem());
        }
        catch (AxisFault ex)
        {
            logger.fatal("createWebServiceConfigurationContext", ex);
        }

        return configurationContext;
    }
    
    public static void cleanupStub(Stub stub){
    	if(stub == null){
    		return;
    	}
    	
    	ServiceClient _serviceClient = stub._getServiceClient();
    	if(_serviceClient != null){
    		try {
				_serviceClient.cleanupTransport();
			} catch (Exception e) {
				logger.fatal("Proxy:ServiceClient.cleanupTransport()", e);
			}
    		
    		try {
				_serviceClient.cleanup();
			} catch (Exception e) {
				logger.fatal("Proxy:ServiceClient.cleanup()", e);
			}
    		
    		_serviceClient = null;
    	}
    	
    	try {
			stub.cleanup();
		} catch (Exception e) {
			logger.fatal("Proxy:Stub.cleanup()", e);
		}
    	stub = null;
    	
    }
}
