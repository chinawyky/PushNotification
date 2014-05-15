/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

import org.apache.axis2.context.ConfigurationContext;

import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceManager;

/**
 * AbstractStubProxy class defines the flow and restrict how to write a common stub proxy.
 * 
 * @author kwwang
 * 
 * @param <T>
 */
public abstract class AbstractStubProxy<T> extends AbstractProxy
{
    private ConfigurationContext context;
    /**
     * Need subclass to implement, different stubs can be created.
     * 
     * @param ws
     * @return
     * @throws Exception
     */
    protected abstract T createStub(WebServiceConfigInterface ws) throws Exception;
    
    /**
     * Can't use DCL here, since the properties in ConfigurationContext are not volitale. but we need to share the context for multiple api in each proxy
     * @param ws
     * @return
     * @throws Exception
     */
    protected synchronized ConfigurationContext createContext(WebServiceConfigInterface ws) throws Exception
    {
        if (context == null)
            context = WebServiceManager.createNewContext(ws.getWebServiceItem());
        return context;
    }

    protected WebServiceConfigInterface getWebServiceConfigInterface()
    {
        return WebServiceConfiguration.getInstance().getServiceConfig(getProxyConfType());
    }

}
