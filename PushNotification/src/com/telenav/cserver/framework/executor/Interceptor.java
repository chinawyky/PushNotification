/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

/**
 * Interceptor.java
 *
 * @author sowmit@telenav.com
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public interface Interceptor 
{
	public static enum InterceptorType {
		PRE, POST
	}

	public static enum InterceptResult {
		ERROR, PROCEED, HALT
	}

	public InterceptResult intercept(ExecutorRequest request,
			ExecutorResponse response, ExecutorContext context);
}
