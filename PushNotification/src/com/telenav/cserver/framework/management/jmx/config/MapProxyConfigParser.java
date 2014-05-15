/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx.config;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.telenav.cserver.framework.management.jmx.BackendServerConfiguration;


/**
 * MapProxyConfigParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Oct 18, 2011
 *
 */
public class MapProxyConfigParser extends XmlServiceUrlParser
{
    
    private static final Logger logger = Logger.getLogger(MapProxyConfigParser.class);
    
    private final String DEFAULT_CONFIGPATH = "config/MapproxyConfig.xml";

    @Override
    protected String parseDocument(Element root, BackendServerConfiguration config, String[] serverUrlKeys)
    {
        String requestTypeKey = serverUrlKeys[0];
        
        StringBuilder sb = new StringBuilder();
        NodeList serverSetNodes = root.getElementsByTagName("serverSet");
        
        
        for (int i = 0; i < serverSetNodes.getLength(); i++)
        {
            Element serverSetNode = (Element)serverSetNodes.item(i);
            String name = serverSetNode.getAttribute("name");
            NodeList serverNodes = ((Element)serverSetNode.getElementsByTagName("hostMapping").item(0)).getElementsByTagName("server");
            for(int j=0; j < serverNodes.getLength(); j++){
                Element serverNode = (Element)serverNodes.item(j);
                
                String requestType = serverNode.getAttribute("requestType");
                if(  requestTypeKey.endsWith(requestType) ){
                    String url = serverNode.getAttribute("host");
                    String port = serverNode.getAttribute("port");
                    sb.append(name).append(":").append(url).append(":").append(port).append("; ");
                    break;
                }
            }
        }
        if( sb.length() > 0 ){
            sb.deleteCharAt(sb.length()-1);
            sb.deleteCharAt(sb.length()-1);
        }
                
        return sb.toString();
    }
    
    @Override
    public void parse(BackendServerConfiguration config, String configPath, String[] serverUrlKeys)
    {
        if( configPath == null || "".equals(configPath.trim()) )
        {
            if( logger.isDebugEnabled() )
                logger.debug("configPath is provided, use the default config path["+DEFAULT_CONFIGPATH+"]");
            configPath = DEFAULT_CONFIGPATH;
        }
        if( serverUrlKeys == null || serverUrlKeys.length == 0 || serverUrlKeys[0] == null)
        {
            logger.fatal("serverUrlKeys can't be null.");
            config.setServiceUrl("serverUrlKeys can't be null.");
            return;
        }
        super.parse(config, configPath, serverUrlKeys);
    }

}
