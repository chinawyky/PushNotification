/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.cserver.service.chunkhandler.ChunkReadListener;

/**
 * BasicTransportor.java
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-21
 * 
 */
public class BasicTransportor implements Transportor {
	Transportor transportor;

	int flag = TransportationFlag.BASIC_FLAG;

	public BasicTransportor(Transportor transportor) {
		this.transportor = transportor;
		
	}

	/**
	 * get the transportor type
	 * 
	 * @return
	 */
	public String getType() {
		return "Basic";
	}
	
	public void read(ChunkReadListener listener) throws TransportorException{}

	/**
	 * read the request
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public byte[] read() throws TransportorException {
		return this.transportor.read();
	}

	/**
	 * get the InputStream
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public InputStream getInputStream() throws TransportorException {
		return this.transportor.getInputStream();
	}

	/**
	 * write the response bytes
	 * 
	 * @param responseBytes
	 * @throws TransportorException
	 */
	public int write(byte[] responseBytes) throws TransportorException 
	{
		byte[] flagBytes = ServletUtil.convertNumberToBytes(flag);
		
		byte[] lenBytes = ServletUtil.convertNumberToBytes(responseBytes.length);
		
		int len = flagBytes.length + lenBytes.length + responseBytes.length;
		
		transportor.write(flagBytes);		
		
		transportor.write(lenBytes);
		
		transportor.write(responseBytes);
		
		return len;
	}

	
	/**
	 * get the OutputStream
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public OutputStream getOutputStream() throws TransportorException {
		return this.transportor.getOutputStream();
	}

	/**
	 * @return Returns the remote address.
	 */
	public String getRemoteAddress() {
		return this.transportor.getRemoteAddress();
	}

	/**
	 * close the transportator
	 * 
	 * @throws TransportorException
	 */
	public void flush() throws TransportorException {
		this.transportor.flush();
	}

	/**
	 * close the transportator
	 * 
	 * @throws TransportorException
	 */
	public void close() throws TransportorException {
		this.transportor.close();
	}
}
