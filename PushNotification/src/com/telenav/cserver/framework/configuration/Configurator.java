/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.configuration;

import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.telenav.cserver.common.resource.ext.EncodeResource;


/**
 * Configurator, to provide a simple interface to application layer, 
 * abstracting from the implementation, use the Spring framework currently.
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 * 
 */
public class Configurator 
{
	private Configurator()
	{}
	/**
	 * get object from given configuration file paths and object name,
	 * you can have multiple configuration files in the input argument
	 * 
	 * @param configFilePaths
	 * @param objectName
	 * @return
	 * @throws ConfigurationException
	 */
	public static Object getObject(String[] configFilePaths, String objectName) throws ConfigurationException 
	{
		try {
			
			ApplicationContext context = new ClassPathXmlApplicationContext(
					configFilePaths);

			// of course, an ApplicationContext is just a BeanFactory
			BeanFactory factory = context;
			return factory.getBean(objectName);

			
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}
	
	/**
	 * get object from given configuration file path and object name
	 * 
	 * @param configFilePath
	 * @param objectName
	 * @return
	 * @throws ConfigurationException
	 */
	public static Object getObject(String configFilePath, String objectName) throws ConfigurationException 
	{
		return getObject(new String[]{configFilePath}, objectName);
		 
	}
	
	public static Object getObjectThrougEncodeSource(String configFilePath, String objectName) throws ConfigurationException
    {
        try
        {
            Resource resource = new EncodeResource(configFilePath);
            XmlBeanFactory factory = new XmlBeanFactory(resource);
            return factory.getBean(objectName);
        }
        catch (Exception e)
        {
            throw new ConfigurationException(e);
        }

    }
	
	public static void loadConfigFile(String configFilePath)
		throws ConfigurationException {
		loadConfigFile(new String[]{configFilePath});
	}
	
	public static void loadConfigFile(String[] configFilePaths)
			throws ConfigurationException {
		try {
			new ClassPathXmlApplicationContext(
					configFilePaths);

		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}
	
	public static Object getObjects(String[] configFilePaths, Class clazz) throws ConfigurationException 
	{
        try
        {
            ApplicationContext context = new FileSystemXmlApplicationContext(configFilePaths);
            return context.getBeansOfType(clazz);

        }
        catch (Exception e)
        {
            throw new ConfigurationException(e);
        }
	}
	
	public static Object getObjects(String configFilePath, Class clazz) throws ConfigurationException 
    {
        return getObjects(new String[]{configFilePath}, clazz);
    }
	
}
