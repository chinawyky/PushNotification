/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx.config;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.telenav.cserver.framework.management.jmx.BackendServerConfiguration;

/**
 * AceServiceUrlParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Jul 20, 2011
 *
 */
public class AceServiceUrlParser extends XmlServiceUrlParser
{
    private static final Logger LOGGER = Logger.getLogger(AceServiceUrlParser.class);
    
    private final String DEFAULT_CONFIGPATH = "config/aceClient.xml";
    
    public AceServiceUrlParser() {}
    
    
    @Override
    protected String parseDocument(Element root, BackendServerConfiguration config, String[] serverUrlKeys)
    {
        StringBuilder sb = new StringBuilder();
        NodeList serviceNodes = root.getElementsByTagName("Service");
        
        for (int i = 0; i < serviceNodes.getLength(); i++)
        {
            Element serviceNode = (Element)serviceNodes.item(i);
            String name = serviceNode.getAttribute("name");
            String url = serviceNode.getAttribute("url");
            
            sb.append(name+":"+url);
            if( i != serviceNodes.getLength() -1 )
                sb.append("; ");
        }
                
        return sb.toString();
    }


    @Override
    public void parse(BackendServerConfiguration config, String configPath, String[] serverUrlKeys)
    {
        if( configPath == null || "".equals(configPath.trim()) )
        {           
            LOGGER.debug("configPath is provided, use the default config path["+DEFAULT_CONFIGPATH+"]");
            configPath = DEFAULT_CONFIGPATH;
        }
        super.parse(config, configPath, serverUrlKeys);
    }

}
