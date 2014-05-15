/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.telenav.cserver.framework.management.jmx.BackendServerConfiguration;

/**
 * XmlServiceUrlParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Jul 21, 2011
 *
 */
public abstract class XmlServiceUrlParser implements ServiceUrlParser
{

    private Logger logger = Logger.getLogger(XmlServiceUrlParser.class);
    
    @Override
    public void parse(BackendServerConfiguration config, String configPath, String[] serverUrlKeys)
    {   
        String serviceUrl = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configPath);
        if( inputStream == null )
        {
            String errorMsg = "backendServer["+config.getName()+"]can't find configuration file["+configPath+"]";
            logger.fatal(errorMsg);
            config.setServiceUrl(errorMsg);
            return;
        }
       
        factory.setValidating(false);
        
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId)
                       throws SAXException, java.io.IOException
                {
                  if (publicId.equals("-//SPRING/DTD BEAN/EN"))
                    // this deactivates the open office DTD
                    return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
                  else return null;
                }
            });
            Document doc = builder.parse(inputStream);
            
            Element root = doc.getDocumentElement();
            serviceUrl = parseDocument(root,config,serverUrlKeys);
                    
        }
        catch(Exception ex)
        {
            serviceUrl = "exception happens when parsing ["+configPath+"]";
            logger.fatal("#XmlServiceUrlParser",ex);
        }
        finally
        {
            if(inputStream == null )
            {
                try{
                    inputStream.close();
                }catch(Exception ignore)
                {
                    logger.fatal("exception when close inputStream!");
                }
            }
        }
        
        config.setServiceUrl(serviceUrl);
    }
    
    protected abstract String parseDocument(Element root, BackendServerConfiguration config, String[] serverUrlKeys);

}
