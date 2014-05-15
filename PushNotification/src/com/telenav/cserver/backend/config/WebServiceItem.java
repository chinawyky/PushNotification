/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.config;

/**
 * WebServiceItem.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-13
 */
public class WebServiceItem
{

	private String serviceName = "default";
	private int webServiceMaxConnectionPerHost= 20;
	private int webServiceMinimumPoolSize= 10;
	private int webServiceMaximumPoolSize= 30;
	private int webServiceConnectionTimeout= 2000;
	private int webServiceTimeout= 5000;
	private int webServiceMaxTotalConnection = 40;

	/**
	 * @param webServiceMaxConnectionPerHost the webServiceMaxConnectionPerHost to set
	 */
	public void setWebServiceMaxConnectionPerHost(
			int webServiceMaxConnectionPerHost)
	{
		this.webServiceMaxConnectionPerHost = webServiceMaxConnectionPerHost;
	}
	/**
	 * @return the webServiceMaxConnectionPerHost
	 */
	public int getWebServiceMaxConnectionPerHost()
	{
		return webServiceMaxConnectionPerHost;
	}
	/**
	 * @param webServiceMinimumPoolSize the webServiceMinimumPoolSize to set
	 */
	public void setWebServiceMinimumPoolSize(int webServiceMinimumPoolSize)
	{
		this.webServiceMinimumPoolSize = webServiceMinimumPoolSize;
	}
	/**
	 * @return the webServiceMinimumPoolSize
	 */
	public int getWebServiceMinimumPoolSize()
	{
		return webServiceMinimumPoolSize;
	}
	/**
	 * @param webServiceConnectionTimeout the webServiceConnectionTimeout to set
	 */
	public void setWebServiceConnectionTimeout(int webServiceConnectionTimeout)
	{
		this.webServiceConnectionTimeout = webServiceConnectionTimeout;
	}
	/**
	 * @return the webServiceConnectionTimeout
	 */
	public int getWebServiceConnectionTimeout()
	{
		return webServiceConnectionTimeout;
	}
	/**
	 * @param webServiceTimeout the webServiceTimeout to set
	 */
	public void setWebServiceTimeout(int webServiceTimeout)
	{
		this.webServiceTimeout = webServiceTimeout;
	}
	/**
	 * @return the webServiceTimeout
	 */
	public int getWebServiceTimeout()
	{
		return webServiceTimeout;
	}
	/**
	 * @param webServiceMaximumPoolSize the webServiceMaximumPoolSize to set
	 */
	public void setWebServiceMaximumPoolSize(int webServiceMaximumPoolSize)
	{
		this.webServiceMaximumPoolSize = webServiceMaximumPoolSize;
	}
	/**
	 * @return the webServiceMaximumPoolSize
	 */
	public int getWebServiceMaximumPoolSize()
	{
		return webServiceMaximumPoolSize;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}
	/**
	 * @return the serviceName
	 */
	public String getServiceName()
	{
		return serviceName;
	}
	
	public int getWebServiceMaxTotalConnection() {
		return webServiceMaxTotalConnection;
	}
	
	public void setWebServiceMaxTotalConnection(int webServiceMaxTotalConnection) {
		this.webServiceMaxTotalConnection = webServiceMaxTotalConnection;
	}

}
