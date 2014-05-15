package com.telenav.cserver.framework.executor.protocol.protobuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.protocol.txnode.ByteArrayWrapper;
import com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory;
import com.telenav.j2me.framework.protocol.ProtoSsoToken;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;
import com.telenav.j2me.framework.util.ToStringUtils;
import com.telenav.j2me.datatypes.ProtocolBuffer;

public class ProtocolBufferResponseFormatter implements ProtocolResponseFormatter {

	private static Logger logger = Logger.getLogger(ProtocolBufferResponseFormatter.class);

	public static final String TYPE_SSO_TOKEN = "SSO_TOKEN";
	
	/**
	 * a flag to check the size of response, if exceeds the limitation, then replace the response 
	 * with a error message.
	 * The purpose is to figure out the incorrect cases in DEV/TEST stage
	 */
	private boolean checkPayloadSize = true;
	
	protected ProtocolResponseFormatter createProtocolResponseFormatter( String type )
	{
		return ExecutorDataFactory.getInstance().createProtocolResponseFormatter("Proto_" + type );
	}
	
	@Override
	public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException 
	{
        ByteArrayWrapper byteWrapper = (ByteArrayWrapper) formatTarget;
        byteWrapper.isProtocolBuffer = true;
        if(responses == null)
        {
        	byteWrapper.bytes = new byte[0];
        	return;
        }
        
        Vector<ProtocolBuffer> bufferList = new Vector<ProtocolBuffer>();
        if(responses != null && responses.length > 0)
        {
        	String ssoToken = responses[0].getSsoToken();
        	if(ssoToken != null && ssoToken.length() > 0)
        	{
        	    ProtoSsoToken.Builder ssoTokenBuilder = ProtoSsoToken.newBuilder();
        	    ssoTokenBuilder.setSsoToken(ssoToken);
        	    
        	    ProtocolBuffer protocolBuffer = new ProtocolBuffer();
                protocolBuffer.setObjType("ssoToken");
                
                try
                {
                    protocolBuffer.setBufferData(ssoTokenBuilder.build().toByteArray());
                    bufferList.add(protocolBuffer);
                    if (logger.isDebugEnabled())
                    {
                        logger.debug(ToStringUtils.toString(protocolBuffer));
                    }
                }
                catch (IOException e)
                {
                    logger.fatal("set sso token failed", e);
                }
        	}
        }
        else
        {
        	logger.fatal("[ProtocolBufferResponseFormatter] Response is null ");
        }
        for(ExecutorResponse response : responses)
        {
        	//String executeType = "Proto_" + response.getExecutorType();
        	if(logger.isDebugEnabled())
        	{
        		logger.debug("Create Response Formatter Type : " + response.getExecutorType());
        	}
        	ProtocolResponseFormatter formatter = createProtocolResponseFormatter(  response.getExecutorType() );
        	
        	ProtocolBuffer protocolBuffer = new ProtocolBuffer();
        	protocolBuffer.setObjType(response.getExecutorType());
        	formatter.format(protocolBuffer, new ExecutorResponse[]{response});
        	if(protocolBuffer.getBufferData() != null)
        	{
        		bufferList.add(protocolBuffer);
        		if(logger.isDebugEnabled())
        		{
        			logger.debug("each response : " + protocolBuffer.toString());
        		}
        	}
        }
        byte[] binData = ProtocolBufferUtil.listToByteArray(bufferList);
        byteWrapper.bytes = binData;
        
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
        	//TODO : implement payload size, do we really need down stream size limitation?
        	/*
        	 * Create object to handle exception
        	 */
        }
	}

}
