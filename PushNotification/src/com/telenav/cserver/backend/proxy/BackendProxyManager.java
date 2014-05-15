/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;

/**
 * BackendProxyManager, this class wrap the init process and client should use this singleton class to access the
 * backend proxy
 * 
 * @author kwwang
 * 
 */
public class BackendProxyManager
{
    private Logger logger = Logger.getLogger(BackendProxyManager.class);

    private static BackendProxyManager instance = new BackendProxyManager();

    private BackendProxyFactory factory;

    private BackendProxyManager()
    {
        try
        {
            factory = (BackendProxyFactory) Configurator.getObject("com/telenav/cserver/backend/proxy/proxys.xml", "proxyFactory");
        }
        catch (ConfigurationException e)
        {
            logger.fatal("BackendProxyManager init failed.", e);
        }
    }

    public static BackendProxyFactory getBackendProxyFactory()
    {
        return instance.factory;
    }

}
