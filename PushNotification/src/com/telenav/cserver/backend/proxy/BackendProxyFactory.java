/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

/**
 * BackendProxyFactory, a factory that create the backend proxy
 * 
 * @author kwwang
 * 
 */
public interface BackendProxyFactory
{
    public <T> T getBackendProxy(Class<T> clz);
}
