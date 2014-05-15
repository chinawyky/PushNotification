/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation.reader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.telenav.cserver.framework.transportation.ServletUtil;

/**
 * BasicReader.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-21
 *
 */
public class BasicReader implements Reader
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
		System.out.println("len: " + len);
		byte[] bytes = new byte[len];
		is.read(bytes, 0 , len);
		return bytes;
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
//		BasicReader nr = new BasicReader();
//		byte[] read = nr.read(bais);
//		
//		for(byte b : read)
//		{
//			System.out.print(b + ",");
//		}
//	}
	

}
