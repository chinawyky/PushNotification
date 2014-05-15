package com.telenav.cserver.framework.executor.protocol.json;

import org.apache.log4j.Logger;
import org.json.me.JSONObject;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.protocol.txnode.ByteArrayWrapper;
import com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory;

public class JsonResponseFormatter implements ProtocolResponseFormatter 
{

	private static Logger logger = Logger.getLogger(JsonResponseFormatter.class);
	
	private boolean checkPayloadSize = true;
	////{\"ServerResponse\":{\"executorType1\":{...},\"executorType2\":{...},...}}
	@Override
	public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException 
	{
        ByteArrayWrapper byteWrapper = (ByteArrayWrapper) formatTarget;
        if(responses == null)
        {
        	byteWrapper.bytes = new byte[0];
        	return;
        }        
        
        JSONObject resultJSON = new JSONObject();
        try
        {
        	resultJSON.put("ServerResponse", this.constrctResultJSON(responses));
        }
        catch(Exception ex)
        {
        	logger.fatal("Failed to construc response JSON : " + ex.getMessage());
        	throw new ExecutorException("Failed to construct response JSON : " + ex.getMessage());
        }
        byteWrapper.bytes = resultJSON.toString().getBytes();
        
        if(checkPayloadSize && responses.length > 0)
        {
        	ExecutorResponse firstResponse = responses[0];
        	int maxPayloadSize = firstResponse.getMaxPayloadSize();
        	if(maxPayloadSize > 0)
        	{
        		//enlarge it by 10%
        		maxPayloadSize *= 1.1;
        	}
        	
        	if(logger.isDebugEnabled())
        	{
        		logger.debug("===========================================================================");
				logger.debug("maxPayloadSize:" + maxPayloadSize);
        	}
        	
        	if(maxPayloadSize > 0 && byteWrapper.bytes != null && byteWrapper.bytes.length > maxPayloadSize)
			{
                CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliTransaction.TYPE_MODULE);
                cli.setFunctionName("format");
                cli.addData("exceed max size", byteWrapper.bytes.length+">"+maxPayloadSize);
                cli.complete();
                
			}
        }
	}
	
	private JSONObject constrctResultJSON(ExecutorResponse[] responses) throws ExecutorException
	{
		JSONObject result = new JSONObject();
		try
		{
			for(ExecutorResponse response : responses)
			{
				ProtocolResponseFormatter formatter = 
					ExecutorDataFactory.getInstance().createProtocolResponseFormatter(response.getExecutorType());
				JSONObject object = new JSONObject();
				formatter.format(object, responses);
				if(object != null)
				{
					result.put(response.getExecutorType(), object);
				}
			}
			return result;
		}
		catch(Exception ex)
		{
			logger.fatal("Failed to construc result JSON : " + ex.getMessage());
			throw new ExecutorException("Failed to construc result JSON : " + ex.getMessage());
		}
	}

}
