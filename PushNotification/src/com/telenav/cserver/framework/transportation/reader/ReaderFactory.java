/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation.reader;

import com.telenav.cserver.framework.transportation.TransportationFlag;

/**
 * ReaderFactory.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-21
 *
 */
public class ReaderFactory {

	
	/**
	 * @param len
	 * @return
	 */
	public static Reader getReader(int flag) 
	{
		if(flag == TransportationFlag.BASIC_FLAG)
		{
			return new BasicReader();
		}
		else if(flag == TransportationFlag.GZIP_FLAG)
		{
			return new GZipReader();
		}
		
		return null;
	}

}
