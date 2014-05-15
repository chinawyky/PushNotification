/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * ServletUtil.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-8
 *
 */
public class ServletUtil 
{
	/**
	 * convert the number into byte array
	 * //TODO maybe we can find a better class to place this method
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] convertNumberToBytes(int num)
	{
		byte[] bytes = new byte[4];
        for(int i = 0; i < 4; i++)
        {
            int b = num & 0xff;
            bytes[i] = (byte)b;
            num >>= 8;
        }
		return bytes;
	}
	
	public static int convertBytesToNumber(byte[] bytes)
	{
		
        int num = 0;
        for(int i = 0; i < 4; i++)
        {
            int b = bytes[i];
            num += b << i * 8;
        }
		return num;
	}
	
	/**
     * send bytes to http response
     * @param response
     * @param respBuff
     * @throws IOException
     */
    public static void sendResponse(HttpServletResponse response, byte[] respBuff)
    	throws IOException
    {
        System.out.println("-------- response length = "+respBuff.length);
  //      response.setContentType("application/octet-stream;charset=UTF-8");
        response.setContentLength(respBuff.length);
        
        //set to use no Cache, for T-Mobile, BELL, and potential gateways
        response.setHeader("Cache-Control", "no-cache,no-transform");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(respBuff);
        baos.writeTo(response.getOutputStream());
        baos.flush();
    }
    
}
