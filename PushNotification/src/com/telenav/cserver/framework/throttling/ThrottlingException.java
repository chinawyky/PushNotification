/**
 * (c) Copyright 2008 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.throttling;

import com.telenav.cserver.framework.ServerException;
import com.telenav.cserver.framework.ErrorCode;

/**
 * ThrottlingException
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2008-7-24
 *
 */
public class ThrottlingException extends ServerException
{

	public ThrottlingException()
	{
		super(ErrorCode.THROTTLING_ERROR);
	}

	public ThrottlingException(String message, Throwable cause)
	{
		super(ErrorCode.THROTTLING_ERROR, message, cause);
	}

	public ThrottlingException(String message)
	{
		super(ErrorCode.THROTTLING_ERROR, message);
	}

	
}
