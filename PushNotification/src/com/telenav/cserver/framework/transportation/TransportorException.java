/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

import com.telenav.cserver.framework.ServerException;

/**
 * TransportorException.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 *
 */
public class TransportorException extends ServerException {

	public TransportorException() {
		super();
		
	}

	public TransportorException(int code, String message, Throwable cause) {
		super(code, message, cause);
		
	}

	public TransportorException(int code, String message) {
		super(code, message);
		
	}

	public TransportorException(int code, Throwable cause) {
		super(code, cause);
		
	}

	public TransportorException(int code) {
		super(code);
		
	}

	public TransportorException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public TransportorException(String message) {
		super(message);
		
	}

	public TransportorException(Throwable cause) {
		super(cause);
		
	}

}
