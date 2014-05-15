/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.reporting;

/**
 * ReportingResponse.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-6-4
 *
 */
public class ReportingResponse 
{
	private int statusCode;
	private String errorMessage;
	
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	

	public String toString()
	{
		return "statusCode:" + statusCode + ",errorMessage:" + errorMessage;
	}
}
