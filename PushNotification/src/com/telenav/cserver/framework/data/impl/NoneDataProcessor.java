/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.data.impl;

import com.telenav.cserver.framework.data.DataProcessor;

/**
 * NoneDataProcessor.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-7-23
 *
 */
public class NoneDataProcessor implements DataProcessor 
{

	/* (non-Javadoc)
	 * @see com.telenav.cserver.framework.data.DataProcessor#process(byte[])
	 */
	public byte[] process(byte[] originalData) 
	{
		return originalData;
	}
	
	/**
	 * get the transportor type
	 * 
	 * @return
	 */
	public String getType()
	{
		return "None";
	}

}
