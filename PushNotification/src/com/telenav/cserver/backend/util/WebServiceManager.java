/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.util;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.util.threadpool.ThreadPool;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.log4j.Logger;

import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.config.WebServiceItem;

/**
 * WebServiceManager.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-10
 */
public class WebServiceManager
{
	static Logger logger = Logger.getLogger(WebServiceManager.class);
	private static ConfigurationContext sharedContext = null;
	
	static int defaultMaxConnPerHost = 10;
	static int defaultMinThreads = 10;
	static int defaultMaxThreads = 30;
	static int defaultConnectionTimeout = 2000;
	static int defaultSoTimeout = 5000;
	
	static
	{
		try
		{
			WebServiceConfiguration wsConfig = WebServiceConfiguration.getInstance();
			WebServiceConfigInterface defaultServiceConfig = wsConfig.getServiceConfig(WebServiceConfiguration.DEFAULT_SERVICE_CONFIG);
			if(defaultServiceConfig != null)
			{
				WebServiceItem defaultServiceItem = defaultServiceConfig.getWebServiceItem();
				if(defaultServiceItem != null)
				{
					defaultMaxConnPerHost = defaultServiceItem.getWebServiceMaxConnectionPerHost();
					defaultMinThreads = defaultServiceItem.getWebServiceMinimumPoolSize();
					defaultMaxThreads = defaultServiceItem.getWebServiceMaximumPoolSize();
					defaultConnectionTimeout = defaultServiceItem.getWebServiceConnectionTimeout();
					defaultSoTimeout = defaultServiceItem.getWebServiceTimeout();
				}
			}
			
		}
		catch(Exception e)
		{
			logger.warn(e, e);
		}
	}
	/**
	 * Returns the shared configuration context, creates one if one is not there
	 * already.
	 * 
	 * @return
	 * @throws AxisFault
	 */
//	public static ConfigurationContext getSharedContext() throws AxisFault 
//	{
//		if (sharedContext == null) {
//			synchronized (WebServiceManager.class) {
//				
//				sharedContext = createNewContext(defaultMaxConnPerHost, defaultMinThreads, 
//				defaultMaxThreads, defaultConnectionTimeout, defaultSoTimeout);
//			}
//		}
//		return sharedContext;
//	}
	
	/**
	 * Creates a new configuration context with the given parameters.
	 * 
	 * @param maxConnPerHost
	 * @param minThreads
	 * @param maxThreads
	 * @return
	 * @throws AxisFault
	 */
	public static ConfigurationContext createNewContext(WebServiceItem serviceItem) throws AxisFault {
		ConfigurationContext configContext = ConfigurationContextFactory
			.createConfigurationContextFromFileSystem(null, null);
		// create the multi-threaded HTTP manager & the client with it
		MultiThreadedHttpConnectionManager mgr = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
		connectionManagerParams.setDefaultMaxConnectionsPerHost(serviceItem.getWebServiceMaxConnectionPerHost());
		connectionManagerParams.setTcpNoDelay(true);
		connectionManagerParams.setStaleCheckingEnabled(true);
		connectionManagerParams.setLinger(0);
		
		// set the timeouts
        connectionManagerParams.setConnectionTimeout(serviceItem.getWebServiceConnectionTimeout()); // in milliseconds
        connectionManagerParams.setSoTimeout(serviceItem.getWebServiceTimeout()); // in milliseconds     


		mgr.setParams(connectionManagerParams);
		HttpClient client = new HttpClient(mgr);
		// cache the HTTP client
		configContext.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, Boolean.TRUE);
		configContext.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, client);
		// set the thread pool
		configContext.setThreadPool(new ThreadPool(serviceItem.getWebServiceMinimumPoolSize(), serviceItem.getWebServiceMaximumPoolSize()));
		return configContext;
	}

}
