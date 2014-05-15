/**
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

/**
 * 	
 * @author mmli
 * @version 1.0 2013-09-27
 * @history modified Nov 20 2013
 *
 */
public class ExcutorEncryptionException extends ExecutorException {

	private static final long serialVersionUID = 5171261415285107288L;

	// It is a good practice to call super() in a constructor, recommended by Sonar
	public ExcutorEncryptionException() {
		super();
	}

	/**
	 * @param code
	 * @param message
	 * @param cause
	 */
	public ExcutorEncryptionException(int code, String message, Throwable cause) {
		super(code, message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param code
	 * @param message
	 */
	public ExcutorEncryptionException(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param code
	 * @param cause
	 */
	public ExcutorEncryptionException(int code, Throwable cause) {
		super(code, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param code
	 */
	public ExcutorEncryptionException(int code) {
		super(code);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ExcutorEncryptionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ExcutorEncryptionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ExcutorEncryptionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
