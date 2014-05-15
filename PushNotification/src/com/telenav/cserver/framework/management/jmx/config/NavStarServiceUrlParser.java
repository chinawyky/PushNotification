/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx.config;

import com.telenav.cserver.framework.management.jmx.BackendServerConfiguration;
import com.telenav.cserver.framework.management.jmx.config.PropertiesServiceUrlParser;

/**
 * NavStarServiceUrlParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Jul 21, 2011
 *
 */
public class NavStarServiceUrlParser extends PropertiesServiceUrlParser
{

    @Override
    protected String combine(String[] values)
    {
        return "http://"+values[0]+"/"+values[1];
    }

    @Override
    public void parse(BackendServerConfiguration config, String filePath, String[] serverUrlKeys)
    {
        String[] theServerUrlKeys = {"host","webservice"};
        super.parse(config, "navstarproxy.properties", theServerUrlKeys);
    }
    
    
    
}
