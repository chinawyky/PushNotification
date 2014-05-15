/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * ProtocolResponseFormatter.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 *
 */
public interface ProtocolResponseFormatter 
{
	/**
	 * format the responses into TxNode 
	 * 
	 * @param responses
	 * @return
	 * @throws ExecutorException
	 */
	public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException;

}
