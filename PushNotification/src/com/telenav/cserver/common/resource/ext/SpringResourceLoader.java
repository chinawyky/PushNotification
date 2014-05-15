/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import java.net.URL;

import org.apache.log4j.Logger;

import com.telenav.cserver.common.resource.ResourceLoader;
import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;

/**
 * SpringResourceLoader.java
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-7
 * 
 */
public class SpringResourceLoader implements ResourceLoader
{
    Logger logger = Logger.getLogger(getClass());

    /**
     * load resource
     * 
     * @param path
     * @param objectName
     * @return Object, Map for properties, Element for XML
     */
    public Object loadResource(String path, String objectName)
    {
        if (!path.endsWith(".xml"))
        {
            // append file suffix automatically
            path = path.concat(".xml");
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("path:" + path);
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        URL url = cl.getResource(path);
        if (url == null)
        {
            logger.warn("File not found:" + path);
            return null;
        }
        String absoluteFilePath = url.getPath();

        if (logger.isDebugEnabled())
        {
            logger.debug("absoluteFilePath:" + absoluteFilePath);
        }
        if (objectName == null)
        {
            objectName = "spring_object";
        }
        try
        {
            //utilize encode resource, translate unicode string 
            return Configurator.getObjectThrougEncodeSource(path, objectName);
        }
        catch (ConfigurationException e)
        {
            logger.fatal("SpringResourceLoader::loadResource()", e);
        }
        return null;
    }

}
