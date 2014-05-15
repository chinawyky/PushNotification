/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

/**
 * ProxyDebugLogInterceptor, it is used to log the request/response info
 * 
 * @author kwwang
 * 
 */
public class ProxyDebugLogInterceptor implements MethodInterceptor
{
    private Logger logger = Logger.getLogger(ProxyDebugLogInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        Object result = null;
        try
        {
            for (Object obj : invocation.getArguments())
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("argument->" + ReflectionToStringBuilder.toString(obj, RecursionMultilineToStringStyle.getInstance()));
                }
            }
            result = invocation.proceed();
        }
        finally
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("response->" + ReflectionToStringBuilder.toString(result, RecursionMultilineToStringStyle.getInstance()));
            }
        }

        return result;
    }

}
