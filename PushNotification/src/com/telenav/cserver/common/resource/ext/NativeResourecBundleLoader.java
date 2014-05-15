package com.telenav.cserver.common.resource.ext;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.telenav.cserver.common.resource.ResourceLoader;

public class NativeResourecBundleLoader
	implements ResourceLoader
{
	Category logger = Category.getInstance(getClass());

	/**
	 * load resource 
	 * 
	 * @param path
	 * @param objectName
	 * @return Object, Map for properties, Element for XML
	 */
	public Object loadResource(String path, String objectName)
	{	

		if(logger.isDebugEnabled())
		{
			logger.debug("Loading resource:" + path);
		}
		//TelenavResourceBundler trb = new TelenavResourceBundler(path, locale, mapDataSet);
		//return trb.getProperties();

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
        boolean getByHTTP = path.startsWith("http");
        
        
		String filePath = path + ".properties";
		
		
		InputStream is = null;
		
		if(logger.isDebugEnabled())
		{
			logger.debug("loading " + filePath);
		}
        
        if (getByHTTP)
        {
            try
            {
                HttpURLConnection connection = (HttpURLConnection) (new URL(filePath)).openConnection();
                is = new BufferedInputStream(connection.getInputStream());
            }
            catch(IOException e)
            {
                logger.warn("Exception when reading:" + path + "," + e.getMessage());
            }
        }
        else
        {
            is = cl.getResourceAsStream(filePath);
        }
        
		
		if(is == null)
		{
            logger.warn("No resource for:" + filePath);
			return null;
		}
		
		if (logger.isDebugEnabled())
        {
            logger.debug("AbsoluteFilePath:" + filePath);
        }
		
//		Properties props = new Properties();
		List itemList =  new ArrayList();
		try
		{
//			props.load(is);
//			map = ResourceUtil.getResource(props);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(is));
			String str = "";
			while((str = br.readLine())!= null)
			{
				str = str.trim();
				if (!str.startsWith("#") && str.length() > 0)
				{
					String[] stringInfo = str.trim().split("=");
					if (stringInfo.length != 2)
					{
						throw new IllegalArgumentException("The item should be like this: key=value, " + str);
					}	
					itemList.add(str);
				}
			}

		} catch (Exception e)
		{
			logger.warn("No resource for:" + path );
//			throw new RuntimeException("No resource for:" + path + ",locale:" + locale, e);
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
                logger.warn("file close failed.", e);
            }
        }
		return itemList;
		
	

	}

}
