/**
 * (c) Copyright 2008 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.throttling;

import com.telenav.kernel.util.datatypes.TnContext;

/**
 * Abstract Operator, this class is to communicate with other external services, i.e, XNAV, DB, Navmap, etc.
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2008-7-24
 *
 */
public abstract class AbstractOperator
{
    public final static String SERVICE_TEST = "TEST";
    public final static String SERVICE_WEBSERVICE_BILLING = "WS_BILLING";
	/**
	 * This is a sample method
	 * 
	 * @param request
	 * @return
	 */
	protected static void test(TnContext tnContext) throws ThrottlingException
	{
		boolean startAPICall = false;
		try
		{
			startAPICall = ThrottlingManager.startAPICall(SERVICE_TEST, tnContext);
			if(!startAPICall)
			{
				//can't call this API anymore, throws Exception
				throw new ThrottlingException();
			}			
			
		}
		finally
		{
			if(startAPICall)
			{
				ThrottlingManager.endAPICall(SERVICE_TEST, tnContext);
			}
		}
	}
}
