/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.management.jmx.AbstractBackendServerMonitor;
import com.telenav.cserver.framework.management.jmx.BackendServerConfiguration;

/**
 * PropertiesServiceUrlParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Jul 20, 2011
 *
 */
public class PropertiesServiceUrlParser implements ServiceUrlParser
{
    private Logger logger = Logger.getLogger(PropertiesServiceUrlParser.class);
    
    public PropertiesServiceUrlParser() {}
    
    @Override
    public void parse(BackendServerConfiguration config, String filePath, String[] serverUrlKeys)
    {
        String[] values = this.getValues(filePath, serverUrlKeys);
        
        if( values == null )
        {
            config.setServiceUrl("parse file["+filePath+"] error");
        }
        else
        {
            config.setServiceUrl(combine(values));
        }
    }
    
    protected String combine(String[] values)
    {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<values.length; i++ )
        {
            sb.append(values[i]);
            if( i != values.length - 1 )
                sb.append("; ");
        }
        return sb.toString();
    }
    
    private String[] getValues(String filePath, String[] serverUrlKeys)
    {
        String values[] = new String[serverUrlKeys.length];
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        if(inputStream == null)
        {
            return null;
        }
        Properties properties = new Properties();
        try
        {
            properties.load(inputStream);
            inputStream.close();
        }
        catch(IOException ignore)
        {
            logger.fatal("#getServiceUrlFromPropertiesFile", ignore);
            return null;
        }
        for(int i = 0; i < serverUrlKeys.length; i++)
            values[i] = properties.getProperty(serverUrlKeys[i]);
        return values;
    }

}
