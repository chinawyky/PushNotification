/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;

/**
 * Abstract Executor
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-10
 *
 */
public abstract class AbstractExecutor implements Executor {

	String type;
	
	/**
	 * return Executor type
	 * 
	 * @return
	 */
	public String getExecutorType()
	{
		return type;
	}
	
	public void setExecutorType(String  type )
	{
		this.type = type;		
	}

	/**
	 * execute the action
	 * 
	 * @param request
	 * @param response
	 * @param context
	 * @throws ExecutorException
	 */
	public void execute(ExecutorRequest request, ExecutorResponse response,
			ExecutorContext context) throws ExecutorException {
		
		CliTransaction cli = getCliTransaction(request, context, CliConstants.TYPE_MODULE);
        cli.setFunctionName(getClass().getName());
    	
		try
    	{
			this.doExecute(request, response, context);
    	}
		//handle all catchable exceptions
		catch(ExecutorException se)
    	{
            cli.setStatus(se);
    		throw se;
    	}
		//handle all fatal errors
		catch(Exception t)
		{
            cli.setStatus(t);
            throw new ExecutorException(t);
		}
        finally
        {               	
        	cli.complete();
        }
	}

	
	public abstract void doExecute(ExecutorRequest request, ExecutorResponse response,
			ExecutorContext context) throws ExecutorException;
	
	/**
	 * abstract method to get a CliTransaction, 
	 * implementation classes can specific the CliTransaction
	 * 
	 * @param request
	 * @param context
	 * @param type
	 * @return
	 */
	protected CliTransaction getCliTransaction(ExecutorRequest request, ExecutorContext context, String type)
	{
		return com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(type);
	}
	
}
