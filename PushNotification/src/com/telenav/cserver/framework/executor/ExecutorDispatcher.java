/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.device.DeviceProperties;
import com.telenav.cserver.common.resource.device.DevicePropertiesHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;
import com.telenav.cserver.framework.executor.Interceptor.InterceptResult;
import com.telenav.cserver.framework.handler.DataHandler;
import com.telenav.cserver.framework.transportation.http.ServletTransportor;


import com.telenav.kernel.util.datatypes.TnContext;

/**
 * Executor Dispatcher
 *
 * @author sowmit@telenav.com
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public class ExecutorDispatcher 
{
    public static final String CHUNKED_HTTP_FLAG = "tn-cs-chunked";
    
	private InterceptorManager interceptorManager = null;
	private ExecutorRegistryLoader executorRegistryLoader = null;
	private ResponseDataHandlerManager dataHandlerManager = null;

	private ExecutorRegistry executorRegistry = new ExecutorRegistry();


	private static Logger logger = Logger.getLogger(ExecutorDispatcher.class);


	public InterceptorManager getInterceptorManager() {
		return interceptorManager;
	}

	public void setInterceptorManager(
			InterceptorManager input) {
		interceptorManager = input;
		
	}
	
	public ExecutorRegistryLoader getExecutorRegistryLoader() {
		return executorRegistryLoader;
	}

	public void setExecutorRegistryLoader(
			ExecutorRegistryLoader input) {
		executorRegistryLoader = input;

	}
	public void setExecutorRegistry(ExecutorRegistry executorRegistry){
		this.executorRegistry = executorRegistry;
	}
	
	public ResponseDataHandlerManager getDataHandlerManager() 
	{
		return dataHandlerManager;
	}

	public void setDataHandlerManager(ResponseDataHandlerManager dataHandlerManager) 
	{
		this.dataHandlerManager = dataHandlerManager;
	}
	
	static DevicePropertiesHolder devicePropertiesHolder = null;


	public void execute(ExecutorRequest request, 
			ExecutorResponse response, 
			ExecutorContext context)
			throws ExecutorException 
	{
		String executorType = request.getExecutorType();
		long hsStartTime = System.currentTimeMillis();
		
            
		CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
		cli.setFunctionName(executorType);
		UserProfile user = request.getUserProfile();
        
        String minInfo = "";
        if (user != null)
        {
            minInfo = "min:" + user.getMin(); 
        }
        
        logger.info("======== Start Action = " + executorType + " ======="+minInfo);
        
//		if(user != null)
//		{
//			cli.addData(CliConstants.LABEL_CLIENT_INTO, "url=" + executorType
//					+"&userid=" + user.getUserId() 
//					+ "&min=" + user.getMin() 
//					+ "&carrier=" + user.getCarrier() 
//					+ "&platform=" + user.getPlatform() 
//					+ "&device=" + user.getDevice()
//					+ "&version=" + user.getVersion()
//					+ "&buildNo=" + user.getBuildNumber());
//		}
//		else
//		{
//			cli.addData(CliConstants.LABEL_CLIENT_INTO, "url=" + executorType);
//		}
		
		
		UserProfile profile = request.getUserProfile();
		TnContext tnContext = context.getTnContext();	
		if(devicePropertiesHolder != null)
		{
			DeviceProperties dp = devicePropertiesHolder.getDeviceProperties(profile, tnContext);
			if(dp != null)
			{
				int maxPayloadSize = dp.getInt("MAX_PAYLOAD_SIZE", -1); 
				response.setMaxPayloadSize(maxPayloadSize);
			}
		}
		
		try
		{
			// Run all the PRE interceptors before executing the service
			Collection<Interceptor> preIntList = interceptorManager
					.getAllPreInterceptors(executorType);
			
			//Merge global interceptors
			preIntList.addAll(interceptorManager.getAllPreGlobalInterceptors());
			
			Iterator<Interceptor> preIntIter = preIntList.iterator();
			while (preIntIter.hasNext()) {
				Interceptor interceptor = preIntIter.next();
				InterceptResult result = interceptor.intercept(request, response, context);
				if (result == InterceptResult.HALT) 
				{
					logger.fatal("HALT!!! in " + interceptor.toString());
					cli.addData(CliConstants.LABEL_ERROR, "Halt in pre-interceptor:" + interceptor.toString());
					return;
	//				throw new ExecutorException(
	//						"Error during pre-service interception: " + interceptor);
				}
			}
	
			// Execute the service
			Executor executor = executorRegistry.getAction(executorType);
			
	//		System.out.println("==================" + executorRegistry);
			if(executor == null)
			{
				cli.addData(CliConstants.LABEL_ERROR, "Invalid request type: " + executorType);
				throw new ExecutorException(
						"Invalid request type: " + executorType);
			}
			executor.execute(request, response, context);
			if(dataHandlerManager != null)
			{
				Collection<DataHandler> dataParserList = dataHandlerManager.getDataHandlers(executorType);
				if(dataParserList != null && dataParserList.size() > 0)
				{
					ServletTransportor trans = (ServletTransportor)context.getTransportor();
				    trans.getResponse().addHeader(CHUNKED_HTTP_FLAG, "true");
				    
					Iterator<DataHandler> dataParserIter = dataParserList.iterator();
					boolean isContinue = true;
					while(dataParserIter.hasNext())
					{
						DataHandler dataHandler = dataParserIter.next();
						isContinue = dataHandler.parse(request, response, context, isContinue, cli);
					}
				}
			}
			
			// Run all the POST interceptors after executing the service.
			Collection<Interceptor> postIntList = interceptorManager
					.getAllPostInterceptors(executorType);
			
			//Merge global interceptors
			postIntList.addAll(interceptorManager.getAllPostGlobalInterceptors());
			
			Iterator<Interceptor> postIntIter = postIntList.iterator();
			while (postIntIter.hasNext()) {
				Interceptor interceptor = postIntIter.next();
				InterceptResult result = interceptor.intercept(request, response, context);
				if (result == InterceptResult.HALT) {
					logger.fatal("HALT!!! in " + interceptor.toString());
					cli.addData(CliConstants.LABEL_ERROR, "Halt in post-interceptor:" + interceptor.toString());
					return;
	//				throw new ExecutorException(
	//						"Error during post-service interception: " + interceptor);
				}
			}
	
			long lDuration = System.currentTimeMillis() - hsStartTime;
			
			
			logger.info("===== End Action = " + executorType  + ", " + lDuration + " ms =====" + minInfo );
			
		}
		finally
		{		
			cli.complete();
		}
	}
	
	private ExecutorDispatcher()
	{
		
	}
	private static ExecutorDispatcher instance = null;
	
	static
	{
		try
		{
			devicePropertiesHolder = 
				(DevicePropertiesHolder)ResourceHolderManager.getResourceHolder("device");
		}
		catch(Exception e)
		{
			//the resource config(device,message) could be null in such cases,
			//so just print warn logs
			logger.warn(e, e);
		}
		try
		{			
			instance = (ExecutorDispatcher)Configurator.getObject("executor/executor_mapping.xml", "executor_handler");

			instance.executorRegistryLoader.loadRegistry(instance.executorRegistry);
			//logs printing
			logger.debug("instance:" + instance);	
			
		}
		catch(ConfigurationException e)
		{
			logger.fatal(e, e);
		}
	}
	
	
	public static ExecutorDispatcher getInstance()
	{
		return instance;
	}
	
}
