/**
 * (c) Copyright 2008 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telenav.cserver.common.resource.ptn.PtnProperties;

/**
 * for cache the /device/ptn_resouce/ptn.properties
 * @author jzhu@telenav.cn
 *
 */
public class GeneralPtnPropertyManager
{
    private static final Logger LOGGER = Logger.getLogger(GeneralPtnPropertyManager.class);
    
    private static final String PTN_RESOURCE_ROOT_PATH = "/device/ptn_resource/";
    private static final String FILE_NAME = "/device/ptn_resource/ptn.properties";
    
    private static Properties ptnProperties = initGeneralPtnProperty();

    private static Properties initGeneralPtnProperty()
    {
        Properties prop = new Properties();
        InputStream is = null;
        try
        {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(FILE_NAME);
            if(is == null)
            {
                LOGGER.warn("File not found:" + FILE_NAME);
                return prop;
            }
            prop.load(is);
        }
        catch(IOException e)
        {
            LOGGER.warn("GeneralPtnPropertyManager.getGeneralPtnProperty()" + e.getMessage());
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
                	LOGGER.fatal("Input Stream close exception: ", e);

                }
            }
        }
        
        return prop;
    }
    
    
    public static void clear()
    {
        ptnProperties = initGeneralPtnProperty();
    }
    
    
    public static Map getGeneralPtnMap()
    {
        return (Map)ptnProperties;
    }
    
    
    /**
     * get the ptn resource path 
     * @param ptn
     * @return
     */
    public static String getPtnResourcePath(String ptn)
    {
        String ptnFolder = PtnProperties.get((Map)ptnProperties, ptn);  
            
        if (ptnFolder == null)
        {
            return "";
        }
        else
        {
            return PTN_RESOURCE_ROOT_PATH + ptnFolder + "/";
        }
    }
    
}
