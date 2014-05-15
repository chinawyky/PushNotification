/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

/**
 * Executor interface, the service handler
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public interface Executor 
{
	/**
	 * execute the action
	 * 
	 * @param request
	 * @param response
	 * @param context
	 * @throws ExecutorException
	 */
	public void execute(ExecutorRequest request, 
						ExecutorResponse response, 
						ExecutorContext context)
		throws ExecutorException;
	
	/**
	 * return ActionType
	 * 
	 * @return
	 */
	public String getExecutorType();
}
