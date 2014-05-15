/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Category;


/**
 * @author jzhu@telenav.cn
 * @version 2011-4-27
 */
public class DeviceCarrierMapping
{
    static Category logger = Category.getInstance(DeviceCarrierMapping.class);
    private static final Properties deviceCarrierMapping = initialMapping();
    
    private static final String configFile = "device_carrier_mapping.properties";

    /**
     * @return
     */
    private static Properties initialMapping()
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
    
    
    public static String getCarrier(String deviceCarrier, String defaultCarrier)
    {
        if (deviceCarrier == null)
            deviceCarrier = "";
        
        String carrier = deviceCarrierMapping.getProperty(deviceCarrier);
        return carrier==null?defaultCarrier:carrier;
    }
    
    public static String getCarrier(String deviceCarrier)
    {
        return getCarrier(deviceCarrier, null);
    }
    
}
