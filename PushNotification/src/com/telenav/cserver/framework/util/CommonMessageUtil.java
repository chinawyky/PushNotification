/*
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Category;

/**
 * 
 * @author huishen
 * @version 2012-9-19
 */
public class CommonMessageUtil 
{
    static Category logger = Category.getInstance(CommonMessageUtil.class);
    private static final Properties messages = initialMessage();
    
    private static final String configFile = "CommonMessage.properties";

    /**
     * @return
     */
    private static Properties initialMessage()
    {
        Properties prop = new Properties();
        InputStream is = null;
        try
        {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile);
            if(is == null)
            {
                logger.warn("File not found:" + configFile);
                return prop;
            }
            prop.load(is);

        }
        catch(Exception e)
        {
            logger.fatal("initialMapping", e);
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    logger.fatal("close inputstream", e);
                }
            }
        }
        
        return prop;
    }
    
    
    public static String getMessage(String key)
    {
        return messages.getProperty(key);
    }
}
