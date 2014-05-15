/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation.reader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reader.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-21
 *
 */
public interface Reader 
{
	/**
	 * read from input stream
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public byte[] read(InputStream is) throws IOException ;
}
