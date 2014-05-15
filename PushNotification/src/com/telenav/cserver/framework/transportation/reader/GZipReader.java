/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation.reader;

import java.io.*;
import java.util.zip.GZIPInputStream;

import com.telenav.cserver.framework.transportation.ServletUtil;

/**
 * BatchReader.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-21
 *
 */
public class GZipReader implements Reader
{	
	public byte[] read(InputStream is) throws IOException 
	{
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
		System.out.println("len:" + len);
		Reader reader = ReaderFactory.getReader(len);
		byte[] bytes;
		if(reader != null)
		{
			bytes = reader.read(is);
		}
		else
		{
			bytes = new byte[len];
			is.read(bytes, 0 , len);
		}
		
//		unzip
		GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(bytes));
		int ch = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while((ch = zis.read()) != -1)
		{
			baos.write(ch);
		}
		return baos.toByteArray();
		
	}
	
	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) throws Exception 
//	{
//		byte[] bytes = new byte[10022];
//		for(int i = 0; i < bytes.length; i ++)
//		{
//			bytes[i] = (byte)i;
//		}
//
//		byte[] inputBytes = new byte[bytes.length + 4];
//		byte[] lenBytes = ServletUtil.convertNumberToBytes(bytes.length);
//		System.arraycopy(lenBytes, 0, inputBytes, 0, 4);
//		System.arraycopy(bytes, 0, inputBytes, 4, bytes.length);
//		ByteArrayInputStream bais = new ByteArrayInputStream(inputBytes);
//		
//		GZipReader nr = new GZipReader();
//		byte[] read = nr.read(bais);
//		
//		for(byte b : read)
//		{
//			System.out.print(b + ",");
//		}
//	}
	

}