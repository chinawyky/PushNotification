/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.telenav.cserver.common.resource.ResourceLoader;

/**
 * XML config Loader, to load attributes and return as org.w3c.dom.Element Object
 * 
 * @author yqchen
 * @version 1.0 2007-2-6 9:42:59
 */
public class XmlResourceLoader implements ResourceLoader
{
	private final Category LOGGER = Category.getInstance(getClass());
	
	/**
	 * load resource 
	 * 
	 * @param path
	 * @param objectName
	 * @return Object, Map for properties, Element for XML
	 */
	public Object loadResource(String path, String objectName)
	{
		
		if(!path.endsWith(".xml"))
		{
			//append file suffix automatically
			path = path.concat(".xml");
		}
		
		if(LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Loading resource:" + path );
		}		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream is = null;
		try
		{
			URL url = classLoader.getResource(path);
			if(url == null)
			{
				LOGGER.warn("File not found:" + path);
				return null;
			}
			
			if (LOGGER.isDebugEnabled())
	        {
	            LOGGER.debug("AbsoluteFilePath:" + path);
	        }
			
			String absoluteFilePath = url.getPath();
			
			try {
				FileInputStream fis = new FileInputStream(absoluteFilePath);
				is = new BufferedInputStream(fis);
			} catch (FileNotFoundException e) {			
				LOGGER.fatal("FileNotFoundException for " + absoluteFilePath);
			}
			
			
			Document doc = factory.newDocumentBuilder().parse(is);

			Element root = doc.getDocumentElement();

			return root;
		} catch (Exception e)
		{			
			LOGGER.warn("config file not existes or invalid format:" + path 
					+ ", " + e.getMessage());
		}
		finally
		{
			if(is != null)
        	{
	            try
	            {
	                is.close();
	            }
	            catch (Exception e)
	            {
	            	LOGGER.fatal("InputStream close exception ", e);
	            }
        	}
		}

		return null;

	}
}