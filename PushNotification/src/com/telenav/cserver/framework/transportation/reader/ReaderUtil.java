/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation.reader;

import java.io.IOException;
import java.io.InputStream;

/**
 * ReaderUtil.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-21
 *
 */
public class ReaderUtil 
{
	public static byte[] read(InputStream is) throws IOException 
	{
		if(is == null)
		{
			throw new IOException("Null InputStream");
		}
		int len = 0;
		for(int i = 0; i < 4; i ++)
		{
			int b = is.read();
            if(b == -1)
            {
            	System.out.println("network error!");
                return null;
            }
            len += b << i * 8;
		}
		Reader reader = ReaderFactory.getReader(len);
		if(reader == null)
		{
			throw new IOException("Unexpected flag:" + len);
		}
		return reader.read(is);
	}
}
