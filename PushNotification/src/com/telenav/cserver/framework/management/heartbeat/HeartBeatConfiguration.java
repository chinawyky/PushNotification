/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.heartbeat;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;


/**
 * HeartBeatConfiguration.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public final class HeartBeatConfiguration 
{
	private String headString;
	private String connectionStatusString;
	private String transactionStatusString;
	
	private String serverName;
	
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	/**
	 * @return the connectionStatusString
	 */
	public String getConnectionStatusString() {
		return connectionStatusString;
	}
	/**
	 * @param connectionStatusString the connectionStatusString to set
	 */
	public void setConnectionStatusString(String connectionStatusString) {
		this.connectionStatusString = connectionStatusString;
	}
	/**
	 * @return the headString
	 */
	public String getHeadString() {
		return headString;
	}
	/**
	 * @param headString the headString to set
	 */
	public void setHeadString(String headString) {
		this.headString = headString;
	}
	/**
	 * @return the transactionStatusString
	 */
	public String getTransactionStatusString() {
		return transactionStatusString;
	}
	/**
	 * @param transactionStatusString the transactionStatusString to set
	 */
	public void setTransactionStatusString(String transactionStatusString) {
		this.transactionStatusString = transactionStatusString;
	}
	
	private static HeartBeatConfiguration instance = null;
	
	private static final Logger LOGGER = Logger.getLogger(HeartBeatConfiguration.class);
	
	public static final String REPLACE_FLAG1 = "&1&";
    public static final String REPLACE_FLAG2 = "&2&";
    public static final String REPLACE_FLAG3 = "&3&";
    public static final String REPLACE_FLAG4 = "&4&";
    
	static
	{
		try
		{			
			instance = (HeartBeatConfiguration)Configurator.getObject("management/heartbeat.xml", "HeartBeatConfiguration");
		
			//logs printing
			LOGGER.info("HeartBeatConfiguration:" + instance);		
			
		}
		catch(ConfigurationException e)
		{
			LOGGER.fatal(e, e);
		}
	}
	private HeartBeatConfiguration()
	{
		
	}
	
	public static HeartBeatConfiguration getInstance() 
	{
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "\r\nheadString: " + headString
			  +"\r\nconnectionStatusString: " + connectionStatusString
			  +"\r\ntransactionStatusString: " + transactionStatusString;
	}
	
//	public static void main(String[] args)
//	{
//		
//	}
	
	
	
}
