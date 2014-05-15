/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation;

import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.cserver.framework.transportation.http.ServletTransportor;
import com.telenav.cserver.service.chunkhandler.ChunkReadListener;
import com.telenav.j2me.datatypes.TxNode;

/**
 * StepWriteTransportor.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-16
 *
 */
public class StepWriteTransportor implements Transportor 
{
	ServletTransportor transportor;	
	
	private final static int CHUNKSIZE = 3000;
	
	public StepWriteTransportor(ServletTransportor transportor)
	{
	    this.transportor = transportor;  
	    //transportor.getResponse().setHeader("Transfer-Encoding", "chunked");
	}
	public void setTransportor(ServletTransportor transportor){
		this.transportor = transportor;
	}
	
	/* (non-Javadoc)
	 * @see com.telenav.cserver.framework.transportation.Transportor#close()
	 */
	public void close() throws TransportorException 
	{
		this.transportor.close();
	}
	
	public void finish() throws TransportorException 
	{
    	byte[] responseBytes = new byte[4];
		com.telenav.j2me.core.datatypes.DataUtil.write(responseBytes, 0, 0);
		write(responseBytes, false);
	}

	/* (non-Javadoc)
	 * @see com.telenav.cserver.framework.transportation.Transportor#flush()
	 */
	public void flush() throws TransportorException 
	{
		this.transportor.flush();	
	}
	
	public void flushBuffer() throws TransportorException
	{
		try
		{
			transportor.getResponse().flushBuffer();
		}
		catch(Exception ex)
		{
			throw new TransportorException(ex);
		}
	}

	/* (non-Javadoc)
	 * @see com.telenav.cserver.framework.transportation.Transportor#getInputStream()
	 */
	public InputStream getInputStream() throws TransportorException 
	{
		return this.transportor.getInputStream();
	}

	/* (non-Javadoc)
	 * @see com.telenav.cserver.framework.transportation.Transportor#getOutputStream()
	 */
	public OutputStream getOutputStream() throws TransportorException 
	{
		return this.transportor.getOutputStream();
	}

	/* (non-Javadoc)
	 * @see com.telenav.cserver.framework.transportation.Transportor#getRemoteAddress()
	 */
	public String getRemoteAddress() 
	{
		return this.transportor.getRemoteAddress();
	}

	/* (non-Javadoc)
	 * @see com.telenav.cserver.framework.transportation.Transportor#getType()
	 */
	public String getType() 
	{
		return "StepWrite";
	}
	
	public void read(ChunkReadListener listener) throws TransportorException{}

	/* (non-Javadoc)
	 * @see com.telenav.cserver.framework.transportation.Transportor#read()
	 */
	public byte[] read() throws TransportorException 
	{
		return this.transportor.read();
	}	
	
	public void writeChunk(byte[] responseBytes) throws TransportorException
	{
		byte[] result = constructStreamingHead(responseBytes);
		int offset = 0;
		if(result.length < CHUNKSIZE)
		{
			this.transportor.write(result);
			return;
		}
		while(offset < result.length)
		{
			int chunkSize = CHUNKSIZE;
			if(result.length - offset < CHUNKSIZE)
			{
				chunkSize = result.length - offset;
			}
			byte[] each = new byte[chunkSize];
			System.arraycopy(result,offset, each, 0, chunkSize);
			this.transportor.write(each);
			offset += chunkSize;
		}
	}
	
	
	public int write(byte[] responseBytes, boolean needHead) throws TransportorException
	{
		if(needHead)
		{
			return this.write(responseBytes);
		}
		return this.transportor.write(responseBytes);
	}
	
	public int write(byte[] responseBytes) throws TransportorException
	{
		byte[] result = constructStreamingHead(responseBytes);
	    return this.transportor.write(result);
	}
    
    private static byte[] constructStreamingHead(byte[] content)
	{
	    byte[] result = new byte[content.length + 8];
	    int offset = 0;
	    com.telenav.j2me.core.datatypes.DataUtil.write(result, (content.length + 4), offset);
	    offset += 4;
	    com.telenav.j2me.core.datatypes.DataUtil.write(result, 983276845, offset);
	    offset += 4;
	    System.arraycopy(content, 0, result, offset, content.length);
	    return result;
	}
	
}
