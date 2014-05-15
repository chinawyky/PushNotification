/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.telenav.cserver.service.chunkhandler.ChunkReadListener;

/**
 * ByteArrayTransportor.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-21
 *
 */
public class ByteArrayTransportor 
implements Transportor
{
	public final String TYPE = "ByteArray";
	
	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(ByteArrayTransportor.class);
	
	ByteArrayInputStream bais = null;
	ByteArrayOutputStream baos = null;
	
	byte[] requestBytes = null;
	public ByteArrayTransportor(byte[] requestBytes, ByteArrayOutputStream baos)
	{
		this.requestBytes = requestBytes;
		this.baos = baos;
	}
	
	/**
	 * get the transportor type
	 * 
	 * @return
	 */
	public String getType()
	{
		return TYPE;
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
		return requestBytes;
	}
	
	/**
	 * get the InputStream
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public InputStream getInputStream() throws TransportorException
	{
		return bais;
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
		int len = responseBytes.length;
		try {
		    
			baos.write(responseBytes);
		} catch (IOException e) {
			throw new TransportorException(e);
		}
		return len;
	}
	
	/**
     * close the transportator
     * 
     * @throws TransportorException
     */
    public void flush() throws TransportorException
    {
    	
    }
    
	/**
	 * get the OutputStream
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public OutputStream getOutputStream() throws TransportorException
	{
		return baos;
	}
	
	/**
     * @return Returns the remote address.
     */
    public String getRemoteAddress()
    {
    	return null;
    }
    
    /**
     * close the transportator
     * 
     * @throws TransportorException
     */
    public void close() throws TransportorException
    {
    	//do nothing, leaving the close() task to servlet container
    }

    
}