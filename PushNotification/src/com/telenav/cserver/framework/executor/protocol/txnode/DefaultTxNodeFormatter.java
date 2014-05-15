/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.txnode;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.TxNode;

/**
 * DefaultTxNodeFormatter.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-17
 *
 */
public class DefaultTxNodeFormatter implements ProtocolResponseFormatter
{
	/**
	 * format the responses into TxNode 
	 * 
	 * @param responses
	 * @return
	 * @throws ExecutorException
	 */
	public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException
	{
		TxNode node = (TxNode)formatTarget;
		
		ExecutorResponse response = responses[0];
		node.addMsg(response.getExecutorType());
		node.addMsg(response.getErrorMessage());
		node.addValue(DataConstants.TYPE_SERVER_RESPONSE);
		node.addValue(response.getStatus());		
		
	}

}
