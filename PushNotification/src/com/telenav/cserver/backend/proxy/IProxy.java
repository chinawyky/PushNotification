/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

/**
 * IProxy interface
 * 
 * @author kwwang
 * 
 */
public interface IProxy
{
    /**
     * the return conf type is corresponding to the type defined in web-services.xml, we need this type to locate the
     * specific proxy configuration
     * 
     * @return
     */
    String getProxyConfType();
}
