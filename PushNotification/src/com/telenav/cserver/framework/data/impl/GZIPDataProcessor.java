/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.data.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.data.DataProcessor;
import com.telenav.cserver.framework.transportation.ServletUtil;
import com.telenav.cserver.framework.transportation.TransportationFlag;

/**
 * GZIPDataProcessor.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-7-23
 *
 */
public class GZIPDataProcessor implements DataProcessor 
{
	private static final int FLAG = TransportationFlag.GZIP_FLAG;
	private static final Logger LOGGER = Logger.getLogger(GZIPDataProcessor.class);

	/* (non-Javadoc)
	 * @see com.telenav.cserver.framework.data.DataProcessor#process(byte[])
	 */
	public byte[] process(byte[] originalData) 
	{
		if(originalData == null)
		{
			return originalData;
		}
		
		byte[] flagBytes = ServletUtil.convertNumberToBytes(FLAG);
		
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		
		try {
			GZIPOutputStream gos = new GZIPOutputStream(baos);
	    	gos.write(originalData, 0, originalData.length);
	    	gos.finish();
	    	gos.close();
	    	byte[] bytesToWrite = baos.toByteArray();
	    	
	    	byte[] lenBytes = ServletUtil.convertNumberToBytes(bytesToWrite.length);
			
	    	ByteArrayOutputStream resultBytes = new ByteArrayOutputStream();		
	    	resultBytes.write(flagBytes);
	    	resultBytes.write(lenBytes);
	    	resultBytes.write(bytesToWrite);
	    	
	    	return resultBytes.toByteArray();
		} catch (IOException e) {	
			LOGGER.fatal(e);			
		}	
		
		return originalData;
	}
	
	/**
	 * get the transportor type
	 * 
	 * @return
	 */
	public String getType()
	{
		return "GZIP";
	}
}
