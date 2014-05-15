/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.txnode;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;


/**
 * ActionRquestFactory.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 *
 */
public class ExecutorDataFactory 
{
	static Logger log = Logger.getLogger(ExecutorDataFactory.class);
	
	private static final String TYPE_DEFAULT = "DEFAULT";
	private static ExecutorDataFactory instance = new ExecutorDataFactory();
	private ExecutorDataFactory()
	{
		
	}

	static
	{
		try
		{			
			instance = (ExecutorDataFactory)Configurator.getObject("executor/executor_protocol.xml", "executor_data_factory");

			//logs printing
			System.out.println("instance:" + instance);			
			
		}
		catch(ConfigurationException e)
		{
			log.fatal(e, e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return
	 */
	public static ExecutorDataFactory getInstance() 
	{
		return instance;
	}

	private Map<String, Item> classMap = new HashMap<String, Item>();
	
	/**
	 * @return the classMap
	 */
	public Map<String, Item> getClassMap() {
		return classMap;
	}

	/**
	 * @param classMap the classMap to set
	 */
	public void setClassMap(Map<String, Item> classMap) {
		this.classMap = classMap;
	}

	/**
	 * create the executor request from the given node
	 * 
	 * @param executorType
	 * @return
	 */
	public ProtocolRequestParser createProtocolRequestParser(String executorType) 
	{
		Item item = classMap.get(executorType);
		if(item == null)
		{
			item = classMap.get(TYPE_DEFAULT);
		}
		if(item == null)
		{
			throw new IllegalArgumentException("Invalid executor type:" + executorType);
		}
		ProtocolRequestParser instance = null;
		instance = item.getRequestParser();
			
		return instance;
	}
	
	/**
	 * create the executor request from the given node
	 * 
	 * @param executorType
	 * @return
	 */
	public ProtocolResponseFormatter createProtocolResponseFormatter(String executorType) 
	{
		Item item = classMap.get(executorType);
		if(item == null)
		{
			item = classMap.get(TYPE_DEFAULT);
		}
		if(item == null)
		{
			throw new IllegalArgumentException("Invalid node type:" + executorType);
		}
		ProtocolResponseFormatter instance = item.getResponseFormatter();
		return instance;
	}

	/**
	 * create the executor request from the given node
	 * 
	 * @param executorType
	 * @return
	 */
	public ExecutorResponse createExecutorResponse(String executorType) 
	{
		Item item = classMap.get(executorType);
		if(item == null)
		{
			item = classMap.get("Proto_"+executorType);
		}
		if(item == null)
        {
            item = classMap.get(TYPE_DEFAULT);
        }
		if(item == null)
		{
			throw new IllegalArgumentException("Invalid node type:" + executorType);
		}
		ExecutorResponse instance = null;
		try
		{
			instance = (ExecutorResponse) (item.getResponseClass().newInstance());
			
		} catch (Exception e)
		{
			log.fatal("Exception when create for executorType:" + executorType
					+ " with class " + item.getResponseClassName(), e);
		}
		instance.setExecutorType(executorType);
		
		return instance;
	}

	
	public static class Item
	{	
		private ProtocolRequestParser requestParser;
		private ProtocolResponseFormatter responseFormatter;
		
		public String responseClassName;
		private Class responseClazz;
		
		
		public Item()
		{
			
		}

		/**
		 * @return the responseClassName
		 */
		public String getResponseClassName() {
			return responseClassName;
		}

		/**
		 * @param responseClassName the responseClassName to set
		 */
		public void setResponseClassName(String responseClassName) {
			this.responseClassName = responseClassName;
		}

		/**
		 * @return the responseClass
		 */
		public Class getResponseClass()  throws ClassNotFoundException
		{
			if(responseClazz == null)
			{
				responseClazz = Class.forName(responseClassName);
			}
			return responseClazz;
		}
		
		
		/**
		 * @return the requestParser
		 */
		public ProtocolRequestParser getRequestParser() {
			return requestParser;
		}

		/**
		 * @param requestParser the requestParser to set
		 */
		public void setRequestParser(ProtocolRequestParser requestParser) {
			this.requestParser = requestParser;
		}

		/**
		 * @return the responseFormatter
		 */
		public ProtocolResponseFormatter getResponseFormatter() {
			return responseFormatter;
		}

		/**
		 * @param responseFormatter the responseFormatter to set
		 */
		public void setResponseFormatter(ProtocolResponseFormatter responseFormatter) {
			this.responseFormatter = responseFormatter;
		}
		
	}
}
