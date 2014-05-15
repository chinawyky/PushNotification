/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import java.util.HashMap;
import java.util.Map;

/**
 * Executor Response
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public class ExecutorResponse 
{
	public final static int STATUS_OK = 0;
	public final static int STATUS_WRITE_FINISHED = 1;
	public final static int STATUS_FAIL = 2;
	public final static int STATUS_INVALID_IDENTITY = 3;
	public final static int STATUS_EXCEPTION = 4;
	
	private String executorType;
	private int status = STATUS_OK;
	private String message = "" ;

	private int maxPayloadSize = -1;
	private int payloadSize;
	private String ssoToken;
	
	/**
	 * others
	 */
	protected Map<String, Object> attributes = new HashMap<String, Object>();

	/**
	 * @return the maxPayloadSize
	 */
	public int getMaxPayloadSize() {
		return maxPayloadSize;
	}

	/**
	 * @param maxPayloadSize the maxPayloadSize to set
	 */
	public void setMaxPayloadSize(int maxPayloadSize) {
		this.maxPayloadSize = maxPayloadSize;
	}

	
	/**
	 * @return the payloadSize
	 */
	public int getPayloadSize() {
		return payloadSize;
	}

	/**
	 * @param payloadSize the payloadSize to set
	 */
	public void setPayloadSize(int payloadSize) {
		this.payloadSize = payloadSize;
	}
	
	public void addPayloadSize(int addedPayloadSize) throws ExecutorResponseSizeExceedException
	{
		if(this.maxPayloadSize > 0 && (this.payloadSize + addedPayloadSize) > this.maxPayloadSize)
		{
			throw new ExecutorResponseSizeExceedException("exceeding maxPayloadSize:" + maxPayloadSize);
		}

		this.payloadSize += addedPayloadSize;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * @return the actionType
	 */
	public String getExecutorType() {
		return executorType;
	}

	/**
	 * @param actionType the actionType to set
	 */
	public void setExecutorType(String actionType) {
		this.executorType = actionType;
	}
	
	/**
	 * get attribute from the map
	 * 
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key) {
		return  attributes.get(key);
	}

	/**
	 * set attribute
	 * 
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	/**
	 * @param message
	 */
	public void setErrorMessage(String message) {
		this.message = message;		
	}

	/**
	 * @return the message
	 */
	public String getErrorMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	/**
	 * @return the ssoToken
	 */
	public String getSsoToken() {
		return ssoToken;
	}

	/**
	 * @param ssoToken the ssoToken to set
	 */
	public void setSsoToken(String ssoToken) {
		this.ssoToken = ssoToken;
	}

	public String toString()
	{
		return "type:" + this.getExecutorType() 
			 + ",status:" + this.getStatus()
			 + ",message:"  + this.getErrorMessage()
			 + "\r\n,MaxPayloadSize:"  + this.getMaxPayloadSize()
			 + ",PayloadSize:"  + this.getPayloadSize()
			 + "\r\n,ssoToken:"  + this.getSsoToken()
		     + "\r\n,attributes:"  + attributes;
	}
	
}
