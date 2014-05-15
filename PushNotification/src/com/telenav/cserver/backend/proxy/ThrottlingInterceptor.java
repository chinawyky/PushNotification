/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.Assert;

import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * ThrottlingInterceptor
 * @author kwwang
 *
 */
public class ThrottlingInterceptor implements MethodInterceptor
{
    private boolean throttlingSwitch;

    private ThrottlingConfManager throttlingManager = new ThrottlingConfManager();

    public ThrottlingInterceptor(boolean flag)
    {
        this.throttlingSwitch = flag;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
       /* TnContext tc = findTnContextFromArguments(invocation.getArguments());
        Assert.notNull(tc,
            "proxy method must has an argument that is tnContext.This is required if you want to apply Throttling annotation to a method.");
        */
        Object result = null;

        // if the switch is off, just invoke proxy operation and return.
        if (!throttlingSwitch)
            return result = invocation.proceed();

        // if the switch is on, go thro the throttling logic
        Object target = invocation.getThis();
        String proxyName = target.getClass().getName();
        ThrottlingConf conf=target.getClass().getAnnotation(ThrottlingConf.class);
        try
        {
            throttlingManager.incrementThrottling(conf);
            // real invocation of method
            result = invocation.proceed();
        }
        finally
        {
            throttlingManager.decrementThrottling(conf);
        }

        return result;
    }

    protected boolean isThrottling(MethodInvocation invocation)
    {
        Method m = invocation.getMethod();
        return m.isAnnotationPresent(Throttling.class);
    }
    
    protected TnContext findTnContextFromArguments(Object[] arguments)
    {
        TnContext foundTc = null;
        for (Object obj : arguments)
        {
            if (obj == null)
                continue;
            if (obj instanceof TnContext)
            {
                foundTc = (TnContext) obj;
                break;
            }
        }
        return foundTc;
    }

}
