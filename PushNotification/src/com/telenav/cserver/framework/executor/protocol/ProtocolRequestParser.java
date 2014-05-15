/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
 * ProtocolRequestParser.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 *
 */
public interface ProtocolRequestParser {
	
	/**
	 * parse the protocol bytes into ActionRequest
	 * 
	 * @param bytes
	 * @return
	 * @throws ExecutorException
	 */
	public ExecutorRequest[] parse(Object object) throws ExecutorException;
}
