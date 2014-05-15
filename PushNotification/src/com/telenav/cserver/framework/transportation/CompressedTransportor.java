/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import com.telenav.cserver.service.chunkhandler.ChunkReadListener;

/**
 * Decorator pattern
 * CompressedTransportor.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 *
 */
public class CompressedTransportor implements Transportor
{
	Transportor transportor;
	
	int flag = TransportationFlag.GZIP_FLAG;

	OutputStream os;
	public CompressedTransportor(Transportor transportor)
	{
		this.transportor = transportor;	
		
	}
	
	
	/**
	 * get the transportor type
	 * 
	 * @return
	 */
	public String getType()
	{
		return "Compressed";
	}
	
	public void read(ChunkReadListener listener) throws TransportorException{}
	
	/**
	 * read the request
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public byte[] read() throws TransportorException
	{
		return this.transportor.read();
	}

	/**
	 * get the InputStream
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public InputStream getInputStream() throws TransportorException
	{
		return this.transportor.getInputStream();
	}
	

	/**
	 * write the response bytes
	 * 
	 * @param responseBytes
	 * @return return the response length
	 * @throws TransportorException
	 */
	public int write(byte[] responseBytes) throws TransportorException
	{
		byte[] flagBytes = ServletUtil.convertNumberToBytes(flag);
		
		
		int responseLen = 0;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		
		try {
			GZIPOutputStream gos = new GZIPOutputStream(baos);
	    	gos.write(responseBytes, 0, responseBytes.length);
	    	gos.finish();
	    	gos.close();
	    	byte[] bytesToWrite = baos.toByteArray();
	    	
	    	byte[] lenBytes = ServletUtil.convertNumberToBytes(bytesToWrite.length);
				    	
	    	responseLen += transportor.write(flagBytes);
			
	    	responseLen += transportor.write(lenBytes);
	    	
	    	responseLen += transportor.write(bytesToWrite);
		} catch (IOException e) {	
			throw new TransportorException(e);
		}	

		return responseLen;
	}	
	
	
	/**
	 * get the OutputStream
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public OutputStream getOutputStream() throws TransportorException
	{
		return os;
	}
	
	/**
     * @return Returns the remote address.
     */
    public String getRemoteAddress()
    {
    	return this.transportor.getRemoteAddress();
    }

    /**
     * close the transportator
     * 
     * @throws TransportorException
     */
    public void flush() throws TransportorException
    {
    	this.transportor.flush();
    }
    
    /**
     * close the transportator
     * 
     * @throws TransportorException
     */
    public void close() throws TransportorException
    {
    	this.transportor.close();
    }
}
