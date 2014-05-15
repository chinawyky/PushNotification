/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx.config;

import com.telenav.cserver.framework.management.jmx.BackendServerConfiguration;

/**
 * ServiceUrlParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Jul 20, 2011
 *
 */
public interface ServiceUrlParser
{
    
    void parse(BackendServerConfiguration config, String configPath, String[] serverUrlKeys);
}
