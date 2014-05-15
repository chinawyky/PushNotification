/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.txnode;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;

/**
 * DefaultTxNodeParser.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-13
 *
 */
public class DefaultTxNodeParser implements ProtocolRequestParser {

	/**
	 * parse the protocol bytes into ActionRequest
	 * 
	 * @param bytes
	 * @return
	 * @throws ExecutorException
	 */
	public ExecutorRequest[] parse(Object object) throws ExecutorException
	{
		ExecutorRequest[] requests = new ExecutorRequest[1];
		ExecutorRequest request = new ExecutorRequest();
		
		requests[0] = request;
		return requests;
	}

}
