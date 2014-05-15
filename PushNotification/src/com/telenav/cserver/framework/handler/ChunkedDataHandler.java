package com.telenav.cserver.framework.handler;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.data.DataProcessor;
import com.telenav.cserver.framework.data.impl.GZIPDataProcessor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolHandler;
import com.telenav.cserver.framework.executor.protocol.txnode.ByteArrayWrapper;
import com.telenav.cserver.framework.transportation.StepWriteTransportor;
import com.telenav.cserver.framework.transportation.http.ServletTransportor;
import com.telenav.j2me.datatypes.TxNode;

public abstract class ChunkedDataHandler implements DataHandler
{
	private static Logger logger = Logger.getLogger(ChunkedDataHandler.class);
	
    String handlerType = "";
	@Override
	public String getHandlerType() 
	{
		// TODO Auto-generated method stub
		return handlerType;
	}
	
	public void setHandlerType(String handlerType)
	{
		this.handlerType = handlerType; 
	}
	
	@Override
	public boolean parse(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context, boolean isContinue, CliTransaction cli) 
	{
		// TODO Auto-generated method stub
		if(handlerType.equalsIgnoreCase("EndChunk"))
		{
			doParse(req, resp, context);
			sendEndChunkResponse(req, resp, context, cli);
		}
		else if(isContinue)
		{
			boolean isSuccess = doParse(req, resp, context);
			sendChunkResponse(req, resp, context, cli);
			return isSuccess;
		}
		return isContinue;
	}
	
	public TxNode createRootNode(TxNode childNode)
	{
		TxNode rootNode = new TxNode();
    	rootNode.setVersion(TxNode.VERSION_55);
    	rootNode.addValue(System.currentTimeMillis());
       	rootNode.addChild(childNode);
       	return rootNode;
	}
	
	public void sendChunkResponse(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context, CliTransaction cli)
	{
		try
		{
			ServletTransportor trans = (ServletTransportor)context.getTransportor();
			StepWriteTransportor transpor = new StepWriteTransportor(trans);
			transpor.flushBuffer();
			ByteArrayWrapper wrapper = new ByteArrayWrapper();
			ProtocolHandler protocolHandler = context.getProtocolHandler();
			protocolHandler.getResponseFormatter().format(wrapper, new ExecutorResponse[]{resp});
			byte[] result = null;
			if(wrapper.isProtocolBuffer)
			{
				result = wrapper.bytes;
			}
			else
			{
				TxNode subNode = TxNode.fromByteArray(wrapper.bytes, 0);
				TxNode resultNode = null;
				if(subNode.childrenSize() > 1)
				{
					resultNode = createRootNode(subNode.childAt(1));
				}
				else
				{
					resultNode = createRootNode(subNode.childAt(0));
				}
				result = TxNode.toByteArray(resultNode);
				if(logger.isDebugEnabled())
				{
					logger.debug(" result : " + resultNode.toString());
				}
			}
			
			if(result != null && result.length > 0)
			{
				if(logger.isDebugEnabled())
				{
					logger.debug(" result size : " + result.length);
				}
				DataProcessor dataProcessor = null;
				UserProfile userProfile = req.getUserProfile();
				String dataProcessType = userProfile != null? userProfile.getDataProcessType(): null;
				if(dataProcessType != null && dataProcessType.equalsIgnoreCase("gzip"))
				{
					dataProcessor = new GZIPDataProcessor();
				}
				if(dataProcessor != null)
				{
					result = dataProcessor.process(result);
				}
				transpor.write(result);	
				transpor.flush();
				cli.addData("Sended Chunk"," result size : " + result.length);
			}
			else
			{
				logger.fatal("Chunk is null");
				cli.addData("Sending Chunk", "null");
			}
		}
		catch(Exception ex)
		{
			logger.error(ex, ex);
			ex.printStackTrace();
		}
	}
	
	public void sendEndChunkResponse(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context, CliTransaction cli)
	{
		try
		{
			ServletTransportor trans = (ServletTransportor)context.getTransportor();
			StepWriteTransportor transpor = new StepWriteTransportor(trans);
			transpor.flushBuffer();
			transpor.finish();
			transpor.flush();
			if(logger.isDebugEnabled())
			{
				logger.debug("Sending end chunk");
			}
			cli.addData("Sending End Chunk", "End");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	public abstract boolean doParse(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context);
}
