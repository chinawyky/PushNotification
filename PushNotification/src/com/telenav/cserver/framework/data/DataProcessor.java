/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.data;

/**
 * DataProcessor.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-7-23
 *
 */
public interface DataProcessor 
{
	/**
	 * 
	 * @param originalData
	 * @return
	 */
	public byte[] process(byte[] originalData);
	
	/**
	 * get the transportor type
	 * 
	 * @return
	 */
	public String getType();
}
