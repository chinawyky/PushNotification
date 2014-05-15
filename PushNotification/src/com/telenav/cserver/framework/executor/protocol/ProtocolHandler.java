/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol;

/**
 * ProtocolHandler.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 *
 */
public interface ProtocolHandler 
{
	/**
	 * @return the requestParser
	 */
	public ProtocolRequestParser getRequestParser();
	/**
	 * @return the responseFormatter
	 */
	public ProtocolResponseFormatter getResponseFormatter();




}
