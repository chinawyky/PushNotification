/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.transportation.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.transportation.Transportor;
import com.telenav.cserver.framework.transportation.TransportorException;
import com.telenav.cserver.service.chunkhandler.ChunkReadListener;

/**
 * ServletTransportor.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 *
 */
public class ServletTransportor implements Transportor
{
	public final String TYPE = "SERVLET";
	
	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(ServletTransportor.class);
	
	HttpServletRequest request;
	HttpServletResponse response;
	
	public ServletTransportor(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
	    response.setContentType("application/octet-stream;charset=UTF-8");
	    response.setHeader("Cache-Control", "no-cache,no-transform");
	}	
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
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
	
	public static final int MAX_CHUNK = 4096 * 8; //32k
	
	private static int readBytes(InputStream is, byte[] buf, int offset, int len) throws Exception
	{
		int bytesRead = 0;
		while(bytesRead < len)
		{
			int nextChunk = len - bytesRead;
			if(nextChunk > MAX_CHUNK)
			    nextChunk = MAX_CHUNK;
			int count = is.read(buf, offset, nextChunk);
			if(count < 0)
			    break;
			bytesRead += count;
			offset += count;
		}
		return bytesRead;
	}
	
	public void read(ChunkReadListener listener) throws TransportorException
	{
		try
		{
			InputStream input = request.getInputStream();
			String encoding = request.getHeader("Transfer-Encoding");
			if(!encoding.equalsIgnoreCase("chunked"))
			{
				logger.fatal("illegal encoding");
			}
			byte[] lenBuf = new byte[4];
			while(true)
			{
				int nRet = readBytes(input, lenBuf, 0, 4);
				if(nRet != lenBuf.length)
				{
					throw new TransportorException("read http streaming :: chunk length error [nRet= " + nRet + "]");
				}
				int len = com.telenav.j2me.datatypes.DataUtil.readInt(lenBuf, 0);
				if(logger.isDebugEnabled())
				{
					logger.debug("Chunk leng : " + len);
				}
				if(len <= 0)
				{
					logger.fatal("empty chunk");
					throw new Exception("chunk size is empty");
				}
				int dataLen = len - 4;
				if(logger.isDebugEnabled())
				{
					logger.debug("datalen : " + dataLen);
				}
				//read chunk content
				byte[] data = new byte[dataLen];
				nRet = readBytes(input, data, 0 , dataLen);
				if(logger.isDebugEnabled())
				{
					logger.debug("each data length : " + data.length);
				}
				boolean isFinished = listener.readChunk(data);//TODO : do we need to check if it is end chunk
				if(isFinished)
				{
					logger.debug("Finished to parse last chunk");
					listener.await();
					return;
				}
				if(nRet != dataLen)
				{
					throw new Exception("read http streaming chunk data failed [nRet = " + nRet + "]");
				}
				if(logger.isDebugEnabled())
				{
					logger.debug("Waiting next chunk coming");
				}
			}
		}
		catch(Exception ex)
		{
			throw new TransportorException(ex);
		}
	}
	
	/**
	 * read the request
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public byte[] read() throws TransportorException
	{
		int length = request.getContentLength();
		if (length < 0) 
		{
			logger.error("Invalid length " + length + " for request.");
			return null;
//			throw new TransportorException("Invalid length " + length + " for request.");
		}
		
		InputStream input = getInputStream();
		BufferedInputStream in = null;
		byte[] requestBytes = null;
		try 
		{
			in = new BufferedInputStream(input);
			//System.out.println(in);
			requestBytes = new byte[length];

	        for (int i = 0; i < length; i++)
	        {
	            int ch = in.read();
	            requestBytes[i] = (byte) ch;
	        }
		} 
		catch(IOException e)
		{
			throw new TransportorException(e);
		}
		finally 
		{			
			if (in != null)
			{
				try 
				{
					in.close();
				} catch (IOException e) 
				{
					logger.error(e, e);
				}
			}
		}
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
		try {
			return request.getInputStream();
		} catch (IOException e) {
			throw new TransportorException(e);
		}
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
		OutputStream os = this.getOutputStream();
		try {
			os.write(responseBytes);
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
    	OutputStream os = this.getOutputStream();
		try {
			os.flush();
		} catch (IOException e) {
			throw new TransportorException(e);
		}
    }
    
	/**
	 * get the OutputStream
	 * 
	 * @return
	 * @throws TransportorException
	 */
	public OutputStream getOutputStream() throws TransportorException
	{
		try {
			return response.getOutputStream();
		} catch (IOException e) {
			throw new TransportorException(e);
		}
	}
	
	/**
     * @return Returns the remote address.
     */
    public String getRemoteAddress()
    {
    	//[DH] this API cannot get real IP address under load balancer
    	//return request.getRemoteAddr();
    	//[DH] get real external IP address
    	String ip = request.getHeader("x-forwarded-for");
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    	{
    		ip = request.getRemoteAddr();
    	}
    	return ip;
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
    
	public HttpServletRequest getRequest() 
	{
		return request;
	}

	public HttpServletResponse getResponse() 
	{
		return response;
	}
    
}
