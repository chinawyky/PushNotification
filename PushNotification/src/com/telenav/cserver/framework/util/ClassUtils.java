/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import java.net.URL;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

/**
 * Class-related operations util.
 * 
 * @author kwwang
 * @date 2009-12-21
 */
public class ClassUtils
{
    private static final Logger logger = Logger.getLogger("com.telenav.cserver.framework.util.");

    /**
     * Get correponding url from the path string
     * 
     * @author kwwang
     * @date 2009-12-21
     * @param path
     * @return
     */
    public static URL getUrl(String path)
    {
        URL url = null;
        try
        {
            url = Thread.currentThread().getContextClassLoader().getResource(path);
            if (url == null)
            {
                url = ClassUtils.class.getResource(path);
                if (url == null)
                {
                    logger.warn("Can not load file->" + path);
                }
            }
        }
        catch (Exception e)
        {
            logger.warn("Can not load file->" + path, e);
        }
        return url;
    }

    /**
     * Check if the corresponding url exists
     * 
     * @author kwwang
     * @date 2009-12-21
     * @param path
     * @return
     */
    public static boolean isExist(String path)
    {
        if (getUrl(path) == null)
        {
            logger.warn("Path : " + path + " doesn't exist.");
            return false;
        }
        return true;
    }

    /**
     * Handle the situation of primitive type
     * 
     * @param type
     * @return
     */
    public static Class getPrimitiveClass(String type)
    {
        if (type.equals("int"))
        {
            return int.class;
        }
        else if (type.equals("byte"))
        {
            return byte.class;
        }
        else if (type.equals("char"))
        {
            return char.class;
        }
        else if (type.equals("short"))
        {
            return short.class;
        }
        else if (type.equals("long"))
        {
            return long.class;
        }
        else if (type.equals("double"))
        {
            return double.class;
        }
        else if (type.equals("float"))
        {
            return float.class;
        }
        else if (type.equals("boolean"))
        {
            return boolean.class;
        }
        return null;
    }

   
    public static <T> T newObject(Class<T> clz, Map properties)
    {
        Object instance =null;
        try
        {
            instance = clz.newInstance();
            BeanUtils.populate(instance, properties);
        }
        catch (Exception e)
        {
            logger.error("newObject failed," + clz.getName() + " pros:" + properties, e);
        }
        return (T)instance;
    }

}
