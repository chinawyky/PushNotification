/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * ServiceUrlParserFactory.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Jul 21, 2011
 *
 */
public class ServiceUrlParserFactory
{
    private static final Logger logger = Logger.getLogger(ServiceUrlParserFactory.class);
    
    public static final String CLASS_ACE = "com.telenav.cserver.framework.management.jmx.config.AceServiceUrlParser";
    public static final String CLASS_NAVSTAR = "com.telenav.cserver.framework.management.jmx.config.NavStarServiceUrlParser";
    public static final String CLASS_PROPERTIES = "com.telenav.cserver.framework.management.jmx.config.PropertiesServiceUrlParser";
    public static final String CLASS_XML_WEBSERVICE = "com.telenav.cserver.framework.management.jmx.config.WebServiceUrlParser";
    public static final String CLASS_MAP_PROXY_CONFIG = "com.telenav.cserver.framework.management.jmx.config.MapProxyConfigParser";
    
    private static final ServiceUrlParser aceParser = new AceServiceUrlParser();
    private static final ServiceUrlParser propertiesParser = new PropertiesServiceUrlParser();
    private static final ServiceUrlParser websericeParser = new WebServiceUrlParser();
    private static final ServiceUrlParser navStarParser = new NavStarServiceUrlParser();
    private static final ServiceUrlParser mapProxyConfigParser = new MapProxyConfigParser();
    
    private static Map<String, ServiceUrlParser> parsers = new HashMap<String,ServiceUrlParser>();
    
    static{
        parsers.put(CLASS_ACE, aceParser);
        parsers.put(CLASS_PROPERTIES, propertiesParser);
        parsers.put(CLASS_XML_WEBSERVICE, websericeParser);
        parsers.put(CLASS_NAVSTAR, navStarParser);
        parsers.put(CLASS_MAP_PROXY_CONFIG, mapProxyConfigParser);
    }
    
    public static ServiceUrlParser getParser(String parserClass)
    {
        ServiceUrlParser parser = parsers.get(parserClass);
        if( parser == null )
        {
            try
            {
                Class clazz = Class.forName(parserClass);
                parser = (ServiceUrlParser)clazz.newInstance();
                
                // to protect not to cache too much
                if( parsers.size() < 20 )
                    parsers.put(parserClass, parser);
            }
            catch(Exception ex)
            {
                logger.fatal("#getParser",ex);
                return null;
            }
           
        }
        return parser;
    }
    
}
