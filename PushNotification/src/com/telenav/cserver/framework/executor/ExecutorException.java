/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import com.telenav.cserver.framework.ServerException;

/**
 * Executor Exception
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public class ExecutorException extends ServerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExecutorException() {
		super();
		
	}

	public ExecutorException(int code, String message, Throwable cause) {
		super(code, message, cause);
		
	}

	public ExecutorException(int code, String message) {
		super(code, message);
		
	}

	public ExecutorException(int code, Throwable cause) {
		super(code, cause);
		
	}

	public ExecutorException(int code) {
		super(code);
		
	}

	public ExecutorException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public ExecutorException(String message) {
		super(message);
		
	}

	public ExecutorException(Throwable cause) {
		super(cause);
		
	}

}
