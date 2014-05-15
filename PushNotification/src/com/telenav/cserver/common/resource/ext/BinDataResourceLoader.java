/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Category;

import com.telenav.cserver.common.resource.ResourceLoader;

/**
 * ResourceLoader to load bin file
 * 
 * @author yqchen
 * @version 1.0 2007-2-13 9:43:33
 */
public class BinDataResourceLoader
	implements ResourceLoader
{
	Category LOGGER = Category.getInstance(getClass());
	
	/**
	 * load resource 
	 * 
	 * @param path
	 * @param objectName
	 * @return Object, Map for properties, Element for XML
	 */
	public Object loadResource(String path, String objectName)
	{	
	    boolean getByHTTP = path.startsWith("http");
        
		
		if(!path.endsWith(".bar"))
		{
			path = path.concat(".bar");
		}
		
		if(LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Loading resource:" + path );
		}
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream is = null;
		
		try
		{
			if (getByHTTP)
			{
				path = encodeFileName(path);
				HttpURLConnection connection = (HttpURLConnection) (new URL(path)).openConnection();
				is = new BufferedInputStream(connection.getInputStream());
			}
			else
			{
//				is = cl.getResourceAsStream(path);
			
				URL url = cl.getResource(path);
				if(url == null)
				{
					LOGGER.warn("File not found:" + path);
					return null;
				}
				
				String absoluteFilePath = url.getPath();
				
				try {
					FileInputStream fis = new FileInputStream(absoluteFilePath);
					is = new BufferedInputStream(fis);
				} catch (FileNotFoundException e) {				
					LOGGER.fatal("FileNotFoundException for " + absoluteFilePath);
				}
			}
			
			if(is == null)
			{
				LOGGER.warn("File not found:" + path);
				return null;
			}
			
			if (LOGGER.isDebugEnabled())
	        {
	            LOGGER.debug("AbsoluteFilePath:" + path);
	        }
			
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			int ch;
			while ((ch = bis.read()) != -1)
			{
                baos.write(ch);
			}
			return baos.toByteArray();
		}
		catch(IOException e)
		{
			LOGGER.fatal("Exception when reading:" + path, e);
		}
		finally
		{
			try
			{
				if (is != null)
				{
					is.close();
				}
			} catch (Exception e)
			{
				LOGGER.fatal("file close failed.", e);
			}
		}
		
		return null;
	}
	
	/**
	 * Encode file name in URL
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String encodeFileName(String url) throws IOException
	{
		if(url == null || url.length() == 0)
		{
			return url;
		}
		int lastSlash = url.lastIndexOf("/");
		String prefix = url.substring(0, lastSlash + 1);
		String origFile = url.substring(lastSlash + 1);
		String encodedFile = URLEncoder.encode(origFile,"ISO-8859-1");
		encodedFile = encodedFile.replaceAll("\\+", "%20");

		return prefix.concat(encodedFile);
	}
	
	
}