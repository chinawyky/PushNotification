/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.txnode;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.TxNode;

/**
 * TxNodeResponseFormatter.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 *
 */
public class TxNodeResponseFormatter implements ProtocolResponseFormatter 
{
	private static Logger logger = Logger.getLogger(TxNodeResponseFormatter.class);

	public static final String TYPE_SSO_TOKEN = "SSO_TOKEN";
	
	/**
	 * a flag to check the size of response, if exceeds the limitation, then replace the response 
	 * with a error message.
	 * The purpose is to figure out the incorrect cases in DEV/TEST stage
	 */
	private boolean checkPayloadSize = true;
	
	/**
	 * format the responses into TxNode 
	 * 
	 * @param responses
	 * @return
	 * @throws ExecutorException
	 */
	public void format(Object target, ExecutorResponse[] responses) throws ExecutorException 
	{
	    ByteArrayWrapper byteWrapper = (ByteArrayWrapper)target;
		if(responses == null)
		{
		    byteWrapper.bytes =  new byte[0];
		    return;
		}
		TxNode rootNode = new TxNode();
		rootNode.setVersion(TxNode.VERSION_55);
		
		if(responses != null && responses.length > 0)
		{
			String ssoToken = responses[0].getSsoToken();
			if(ssoToken != null && ssoToken.length() > 0)
			{
				//add SSO token node 
				TxNode ssoTokenNode = new TxNode();
				ssoTokenNode.addMsg(TYPE_SSO_TOKEN);
				ssoTokenNode.addMsg(ssoToken);
				rootNode.addChild(ssoTokenNode);
			}
		}
		for(ExecutorResponse response: responses)
		{
			ProtocolResponseFormatter formatter = 
				ExecutorDataFactory.getInstance().createProtocolResponseFormatter(response.getExecutorType());
			
			TxNode childNode = new TxNode();
			formatter.format(childNode, new ExecutorResponse[]{response});
			rootNode.addChild(childNode);			
		}
		
		byteWrapper.bytes = TxNode.toByteArray(rootNode);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("response:" + rootNode);
		}
		
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
                
                
				TxNode errorRootNode = new TxNode();
				errorRootNode.setVersion(TxNode.VERSION_55);
				
				TxNode childNode = new TxNode();
				childNode.addMsg(firstResponse.getExecutorType());
				childNode.addMsg("Response size is too big, exceeded:" + maxPayloadSize);
				childNode.addValue(DataConstants.TYPE_SERVER_RESPONSE);
				childNode.addValue(ExecutorResponse.STATUS_EXCEPTION);
				
				errorRootNode.addChild(childNode);
				
				
				byteWrapper.bytes = TxNode.toByteArray(errorRootNode);
				
				
				if(logger.isDebugEnabled())
				{
					logger.debug("===========================================================================");
					logger.debug("errorRootNode:" + errorRootNode);
				}
			}
		}
		
	}

}
