/**
 * (c) Copyright 2008 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.external_service;


import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Manager to markup/markdown external services, i.e, billing
 *  
 * @author yqchen@telenav.cn
 * @version 2.0 2009-3-26
 *
 */
public class ExternalServiceManager
{
	/**
	 * a description of the property list.
	 * Will be written as the first line of property file
	 */
	static String header = "MarkUp/MarkDown services";
	
	/**
	 * property file name 
	 */
	static String fileName = "management/external-services.properties";
	
	/**
	 * Properties to store the service status(true/false)
	 * true: enable the service
	 * false: disable it
	 */
	static Properties props = new Properties();
	
	static String absoluteFilePath = null;
	
	/**
	 * log4j
	 */
	private static Logger logger = Logger.getLogger(ExternalServiceManager.class);
	
	static
	{
		//load the property file and init the Properties
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream is = cl.getResourceAsStream(fileName);
		
		absoluteFilePath = cl.getResource(fileName).getPath();
		try
		{
			props.load(is);
            
		} catch (Exception e)
		{
			logger.fatal("No resource for:" + fileName);
		}
        finally
        {
            try
            {
                if (is != null)
                {
                    is.close();
                }
            } catch (Exception e)
            {
                logger.warn("file close failed.", e);
            }
        }
        
        if(logger.isDebugEnabled())
        {
        	logger.debug("external services:" + props);
        }
	}
	
	/**
	 * set status to the service
	 * 
	 * @param service
	 * @param status
	 * @return
	 */
	private static boolean markStatus(String service, boolean status)
	{
		if(logger.isDebugEnabled())
        {
        	logger.debug("markStatus service:" + service + ",status:" + status);
        }
		
		if(service == null || service.length() == 0)
		{
			throw new IllegalArgumentException("Service can't be null or empty.");
		}
		if(!props.containsKey(service))
		{
			logger.warn("There is no service:" + service);
			return false;
		}		
		
		props.setProperty(service, status? "true": "false");
		
		store();
		return true;
	}
	
	/**
	 * mark up the service
	 * 
	 * @param service
	 * @return
	 */
	public static boolean markUp(String service)
	{
		return markStatus(service, true);
	}
	
	
	/**
	 * mark down the service
	 * 
	 * @param service
	 * @return
	 */
	public static boolean markDown(String service)
	{
		return markStatus(service, false);
	}
	
	
	/**
	 * return the status for service
	 * 
	 * @param service
	 * @return
	 */
	public static boolean getStatus(String service)
	{
		if(service == null || service.length() == 0)
		{
			throw new IllegalArgumentException("Service can't be null or empty.");
		}
		if(!props.containsKey(service))
		{
			logger.warn("There is no service:" + service);
			return false;
		}
		
		String value = props.getProperty(service);
		return value.equalsIgnoreCase("true");
	}
	
	/**
	 * store the modified properties
	 */
	private static void store()
	{
		if(absoluteFilePath == null)
		{
			logger.fatal("The absoluteFilePath is null");
			return;
		}
		OutputStream out = null;
		try
		{
			out = new FileOutputStream(absoluteFilePath);		
			props.store(out, header);
		}
		catch(Exception e)
		{
			logger.warn("file store failed.", e);
		}
		finally
        {
            try
            {
                if (out != null)
                {
                	out.close();
                }
            } catch (Exception e)
            {
                logger.warn("file close failed.", e);
            }
        }
	}
	
	
//	public static void main(String[] args)
//	{
//		System.out.println("aaa");
//		System.out.println(ExternalServiceManager.getStatus("db"));
//		ExternalServiceManager.markDown("db");
//	}
}

