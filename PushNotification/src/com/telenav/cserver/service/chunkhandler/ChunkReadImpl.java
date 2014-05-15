package com.telenav.cserver.service.chunkhandler;

import java.util.Enumeration;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.ServerException;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.cli.CliThreadLocalUtil;
import com.telenav.cserver.framework.cli.CliTransactionFactory;
import com.telenav.cserver.framework.data.DataProcessor;
import com.telenav.cserver.framework.data.impl.GZIPDataProcessor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.txnode.ByteArrayWrapper;
import com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory;
import com.telenav.cserver.framework.transportation.http.ServletTransportor;

public class ChunkReadImpl implements ChunkReadListener 
{
	private static Logger logger = Logger.getLogger(ChunkReadImpl.class);
	private HttpServletRequest req;
	private HttpServletResponse resp;
	DataProcessor dataProcessor;
	ExecutorContext context;
	ExecutorRequest request = null;
	ExecutorResponse response = null;
	StringBuffer sbExecutorType = new StringBuffer();
	String firstExecutorType = "";
	CliTransaction cli;
	private final CountDownLatch latch = new CountDownLatch(1);

	private ChunkProcessor chunkProcessor = null;
	boolean finished = false;

	public ChunkReadImpl(HttpServletRequest req, HttpServletResponse resp, ExecutorRequest request, 
			ExecutorResponse response, DataProcessor dataProcessor, ExecutorContext context, CliTransaction cli)
	{
	    this.req = req;
	    this.resp = resp;
	    this.request = request;
	    this.response = response;
	    this.dataProcessor = dataProcessor;
	    this.context = context;
	    this.cli = cli;
	}
	
	@Override
	/**
	 * For chunk only one request is allowed
	 */
	public boolean readChunk(byte[] chunk) throws ServerException 
	{		
		byte[] requestBuff = chunk;
		if(requestBuff == null)
		{
			logger.warn("There is an empty request coming in.");
			finished = true;
		}
		if(context.getProtocolHandler() != null)
		{
			ExecutorRequest[] requests = context.getProtocolHandler().getRequestParser().parse(chunk);
			if(requests == null)
			{
				logger.warn("Failed to parse Request");
				finished = true;
			}
			request = requests[0];
			/* 
            for(int i = 0; i < requests.length; i++)
            {
            	ExecutorRequest request = requests[i];
            	String executorType = request.getExecutorType();
            	sbExecutorType.append(executorType).append(",");
            }
            */
			String executorType = request.getExecutorType();
			sbExecutorType.append(executorType).append(",");
            if(sbExecutorType.toString().endsWith(","))
            {
            	sbExecutorType.deleteCharAt(sbExecutorType.lastIndexOf(","));
            }
            CliThreadLocalUtil.setCliThreadLocal(requests);
            cli = CliTransactionFactory.getInstance(CliConstants.TYPE_URL);
            cli.setFunctionName("service_" + sbExecutorType.toString());
            /*
            for(int i = 0; i < requests.length; i++)
            {
            	ExecutorRequest request = requests[i];
            	String executorType = request.getExecutorType();
            	if(i == 0)
            	{
            		firstExecutorType = executorType;
            		UserProfile userProfile = request.getUserProfile();
            		String dataProcessType = userProfile != null ? userProfile.getDataProcessType() : null;
            		if(dataProcessType != null && dataProcessType.equalsIgnoreCase("gzip"))
            		{
            			dataProcessor = new GZIPDataProcessor();
            		}
            	}
            	copyHttpHeadToExecutorRequest(req, request);
            	ExecutorResponse response = ExecutorDataFactory.getInstance().createExecutorResponse(executorType);
            	responses[i] = response;
            	CliThreadLocalUtil.setSingleExecutorType(request.getExecutorType());
            }
            */
            UserProfile userProfile = request.getUserProfile();
            String dataProcessType = userProfile != null ? userProfile.getDataProcessType() : null;
            if(dataProcessType != null && dataProcessType.equalsIgnoreCase("gzip"))
            {
            	dataProcessor = new GZIPDataProcessor();
            }
            copyHttpHeadToExecutorRequest(req, request);
            response = ExecutorDataFactory.getInstance().createExecutorResponse(executorType);
            CliThreadLocalUtil.setSingleExecutorType(request.getExecutorType());
        	try
        	{ 
        		if(this.chunkProcessor == null)
        		{
        			this.chunkProcessor = ChunkProcessorFactory.getInstance().createProcessor(executorType);
        			this.chunkProcessor.setCallback(new ChunkCallbackImpl());
        			this.chunkProcessor.getCallback().setListener(this);
        		}
        		if(this.chunkProcessor != null)
        		{
        			logger.debug("Start do Process");
        			chunkProcessor.process(request, response, context);
        			logger.debug("End do Process");
        		}
        		else
        		{
        			finished = true;
        			throw new ServerException("No processor defined");
        		}
        	}
        	catch(Exception t)
        	{
        		logger.error(t, t);
        		/*
        		for(int i = 0; i < requests.length; i++)
        		{
        			cliLoggingException(requests[i], requests[i].getExecutorType(), t);
        			if(responses[i].getErrorMessage() == null)
        			{
        				responses[i].setErrorMessage(t.getMessage());
        			}
        			responses[i].setStatus(ExecutorResponse.STATUS_EXCEPTION);
        		}
        		*/
        		cliLoggingException(request, request.getExecutorType(), t);
        		if(response.getErrorMessage() == null)
        		{
        			response.setErrorMessage(t.getMessage());
        		}
        		response.setStatus(ExecutorResponse.STATUS_EXCEPTION);
        		finished = true;
        	}
		}
		logger.debug("Finished : " + finished);
		return finished;
	}
	
	public void finish()
	{
		this.finished = true;
	}
	
	public void handleResponse(ExecutorRequest c_request, ExecutorResponse c_response)
	{
		if(c_response != null)
		{
			if(c_response.getStatus() == ExecutorResponse.STATUS_WRITE_FINISHED)
			{
				ExecutorRequest reqForCli = null;
				if(c_request != null)
				{
					reqForCli = c_request;
				}
				this.cliLoggingClientInfo(reqForCli, "service_" + this.sbExecutorType.toString(), cli);
				cli.complete();
				return;
			}
		}
		try
		{
			ExecutorRequest reqForCli = null;
			if(c_request != null)
			{
				reqForCli = c_request;
			}
			this.cliLoggingClientInfo(reqForCli, "service_" + this.sbExecutorType.toString(), cli);
			ByteArrayWrapper wrapper = new ByteArrayWrapper();
			ExecutorResponse[] c_responses = new ExecutorResponse[1];
			c_responses[0] = c_response;
			this.context.getProtocolHandler().getResponseFormatter().format(wrapper, c_responses);
			byte[] responseBuff = wrapper.bytes;
			if(dataProcessor != null)
			{
				responseBuff = dataProcessor.process(responseBuff);
			}
			this.resp.setContentLength(responseBuff.length);
			ServletTransportor sTransportor = (ServletTransportor)this.context.getTransportor();
			sTransportor.write(responseBuff);
			sTransportor.flush();
			sTransportor.getResponse().flushBuffer();
			logger.info("response_length: " + responseBuff.length);
			logger.info("=============end=============");
			cli.addData("response_length", Integer.toString(responseBuff.length));
			cli.complete();
			awake();
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
	}
	
	public void cliLoggingException(ExecutorRequest request, String executorType, Throwable t)
	{
		CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
		cli.setFunctionName(executorType);
		cliLoggingClientInfo(request, executorType, cli);
		cli.setStatus(t);
		cli.complete();
	}
	
	public void cliLoggingClientInfo(ExecutorRequest request, String executorType, CliTransaction cli)
	{
		if(cli == null)
			return;
		UserProfile user = null;
		if(request != null)
		{
			user = request.getUserProfile();
		}
		if(user != null)
		{
			String screenWidth = user.getScreenWidth();
			String screenHeight = user.getScreenHeight();
			String resolution = (screenWidth != null ? screenWidth : "") + "x" + (screenHeight != null ? screenHeight : "");
			cli.addData(
					CliConstants.LABEL_CLIENT_INTO,
					"url="
					       + executorType
					       + "&userid="
					       + user.getUserId()
					       + "&min="
					       + user.getMin()
					       + "&carrier="
					       + user.getCarrier()
					       + "&platform="
					       + user.getPlatform()
					       + "&product="
					       + user.getProduct()
					       + "&device="
					       + user.getDevice()
					       + "&resolution="
					       + resolution
					       + "&programCode="
					       + user.getProgramCode()
					       + "&mapDpi="
					       + (user.getMapDpi() != null ? user.getMapDpi() : "")
					       + "&OSVersion="
					       + (user.getOSVersion() != null ? user.getOSVersion() : "")
					       + "&version=" + user.getVersion() + "&buildNo="
					       + user.getBuildNumber() + "&deviceCarrier=" + user.getDeviceCarrier() + "&ptnSource=" + user.getPtnSource());
		}
		else
		{
			cli.addData(CliConstants.LABEL_CLIENT_INTO, "url=" + executorType);
		}	
	}
	
	private void copyHttpHeadToExecutorRequest(HttpServletRequest req, ExecutorRequest request)
	{
		Enumeration e = req.getHeaderNames();
		while(e.hasMoreElements())
		{
			String key = (String)e.nextElement();
			String value = req.getHeader(key);
			request.setAttribute(key, value);
		}
	}

	public String getFirstExecutorType()
	{
		return firstExecutorType;
	}
	
	public StringBuffer getSbExecutorType() 
	{
		return sbExecutorType;
	}
	
    public void await() throws ServerException 
    {
    	try
    	{
    		logger.debug(" --- latch await");
    		latch.await();
    	}
    	catch(InterruptedException e)
    	{
    		throw new ServerException("No processor defined");
    	} 
    }
    
    public void awake()
    {
    	logger.debug(" --- latch countdown");
    	latch.countDown();
    }

}
