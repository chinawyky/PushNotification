/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

/**
 * TransportorFlag.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-22
 *
 */
public interface TransportationFlag 
{
	/**
	 * basic transportation
	 */
	public final static int BASIC_FLAG = 999999990;
	
	/**
	 * gzip transportation
	 */
	public final static int GZIP_FLAG = BASIC_FLAG + 1;
	
}
