/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx.config;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.telenav.cserver.framework.management.jmx.BackendServerConfiguration;

/**
 * WebServiceUrlParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Jul 20, 2011
 *
 */
public class WebServiceUrlParser extends XmlServiceUrlParser
{
    private Logger logger = Logger.getLogger(WebServiceUrlParser.class);
    
    private final String DEFAULT_CONFIGPATH = "config/web-service.xml";
    
    public WebServiceUrlParser() {}
    
    @Override
    protected String parseDocument(Element root, BackendServerConfiguration config, String[] serverUrlKeys)
    {
        String serviceName = serverUrlKeys[0];
        String serviceUrl = "";
        NodeList beans = root.getChildNodes();
        
        String refBeanName = null;
        
        for (int i = 0; i < beans.getLength(); i++)
        {
            Node node = beans.item(i);
            if( !(node instanceof Element) )
                continue;
            Element serviceNode = (Element)beans.item(i);
            String id = serviceNode.getAttribute("id");
            
            if("webservice-configuration".equals(id))
            {
                NodeList propertiesNodes = serviceNode.getElementsByTagName("property");
                for (int j = 0; j < propertiesNodes.getLength(); j++)
                {
                   Element propertyNode = (Element)propertiesNodes.item(0);
                   String propertyName = propertyNode.getAttribute("name");
                   if( "serviceConfigMap".equals(propertyName) )
                   {
                       Element mapNode = (Element)propertyNode.getElementsByTagName("map").item(0);
                       NodeList entryNodes = mapNode.getElementsByTagName("entry");
                       
                       StringBuilder sb = new StringBuilder();
                       for(int k=0; k<entryNodes.getLength(); k++)
                       {
                            Element entryNode = (Element)entryNodes.item(k);
                            String key = entryNode.getAttribute("key");
                            
                            if( key.equals(serviceName) )
                            {
                                refBeanName = ((Element)entryNode.getElementsByTagName("ref").item(0)).getAttribute("bean");
                                break;
                            }
                            
                       }
                       if ( refBeanName != null )
                           break;
                   }
                }
            }
            if( refBeanName != null )
            {
                break;
            }
        }
        
        if( refBeanName == null )
        {
            String errorMsg = "can't find service name["+serviceName+"] for backend server["+config.getName()+"]";
            logger.fatal(errorMsg);
            return errorMsg;
        }
        
        
        for (int i = 0; i < beans.getLength(); i++)
        {
            Node node = beans.item(i);
            if( !(node instanceof Element) )
                continue;
            Element serviceNode = (Element)beans.item(i);
            String id = serviceNode.getAttribute("id");
            if(refBeanName.equals(id))
            {
                NodeList propertiesNodes = serviceNode.getElementsByTagName("property");
                for (int j = 0; j < propertiesNodes.getLength(); j++)
                {
                   Element propertyNode = (Element)propertiesNodes.item(0);
                   String propertyName = propertyNode.getAttribute("name");
                   if( "serviceUrl".equals(propertyName) )
                   {
                       serviceUrl = ((Element)propertyNode.getElementsByTagName("value").item(0)).getTextContent();
                       break;
                   }else if( "serviceUrlMapping".equals(propertyName) )
                   {
                       Element mapNode = (Element)propertyNode.getElementsByTagName("map").item(0);
                       NodeList entryNodes = mapNode.getElementsByTagName("entry");
                       
                       StringBuilder sb = new StringBuilder();
                       for(int k=0; k<entryNodes.getLength(); k++)
                       {
                            Element entryNode = (Element)entryNodes.item(k);
                            String key = entryNode.getAttribute("key");
                            String url = ((Element)entryNode.getElementsByTagName("value").item(0)).getTextContent();
                            sb.append(key).append(": ").append(url).append(";");
                       }
                       if( sb.length() > 0 )
                           sb.deleteCharAt(sb.length()-1);
                       serviceUrl = sb.toString();
                       break;
                   }
                }
            }
            
            //check whether find serviceUrl
            if( serviceUrl != null && !"".equals(serviceUrl) )
            {
                break;
            }
        }
        
        return serviceUrl;
    }



    @Override
    public void parse(BackendServerConfiguration config, String configPath, String[] serverUrlKeys)
    {
        if( configPath == null || "".equals(configPath.trim()) )
        {
            logger.warn("backendServer["+config.getName()+"] configPath isn't provided, use the default config path["+DEFAULT_CONFIGPATH+"]");
            configPath = DEFAULT_CONFIGPATH;
        }
        
        if( serverUrlKeys == null || serverUrlKeys.length != 1 )
        {
            String errorMsg = "backendServer["+config.getName()+"] config error. length of serverUrlKeys should be 1 but "+( serverUrlKeys == null ? 0 : serverUrlKeys.length);
            logger.fatal(errorMsg);
            config.setServiceUrl(errorMsg);
            return;
        }
        
       super.parse(config, configPath, serverUrlKeys);
    }

}
