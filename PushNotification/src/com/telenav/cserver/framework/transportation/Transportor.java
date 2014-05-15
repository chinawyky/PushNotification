/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.cserver.service.chunkhandler.ChunkReadListener;

/**
 * Transportor.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-10
 *
 */
public interface Transportor 
{
	/**
	 * get the transportor type
	 * 
	 * @return
	 */
	public String getType();	
	
	public void read(ChunkReadListener listener) throws TransportorException;
	/**
	 * read the request
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public byte[] read() throws TransportorException;

	/**
	 * get the InputStream
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public InputStream getInputStream() throws TransportorException;
	
	/**
	 * write the response bytes
	 * 
	 * @param responseBytes
	 * @return return the response length
	 * @throws TransportorException
	 */
	public int write(byte[] responseBytes) throws TransportorException;
	
	/**
	 * get the OutputStream
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public OutputStream getOutputStream() throws TransportorException;
	
	/**
     * @return Returns the remote address.
     */
    public String getRemoteAddress();

    /**
     * close the transportator
     * 
     * @throws TransportorException
     */
    public void flush() throws TransportorException;
    
    /**
     * close the transportator
     * 
     * @throws TransportorException
     */
    public void close() throws TransportorException;

    
//    public FlagByte getFlag();
//    
//    public void setFlag(FlagByte flag);
    
}
