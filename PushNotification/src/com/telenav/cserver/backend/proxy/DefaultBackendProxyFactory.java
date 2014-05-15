/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;

/**
 * DefaultBackendProxyFactory
 * 
 * @author kwwang
 * 
 */
public class DefaultBackendProxyFactory implements BackendProxyFactory, ThrottlingSwitch, BeanFactoryAware
{
    private Map<String, IProxy> proxies = new HashMap<String, IProxy>();

    private boolean flag = true;

    private BeanFactory beanFactory;

    private Advisor throttlingAdvisor;

    private Advisor proxyLogAdvisor;
    
    public DefaultBackendProxyFactory()
    {
        throttlingAdvisor = makeThrottlingAdvisor();
        proxyLogAdvisor = makeProxyLogAdvisor();
    }

    public void setProxies(Map<String, IProxy> proxies)
    {
        this.proxies = proxies;
    }

    /**
     * Get proxy on demand.
     */
    public synchronized <T> T getBackendProxy(Class<T> clz)
    {
        String className = clz.getName().substring(clz.getName().lastIndexOf(".") + 1);
        String proxyName = className.substring(0, 1).toLowerCase() + className.substring(1);
        T cachedProxy = (T) proxies.get(proxyName);
        
        if (cachedProxy == null)
        {
            ListableBeanFactory listableBf = (ListableBeanFactory) beanFactory;
            IProxy proxy = (IProxy) listableBf.getBean(proxyName);
            if (proxy != null)
            {
                ProxyFactory pf = new ProxyFactory();
                pf.setTarget(proxy);
                pf.setProxyTargetClass(true);
                pf.addAdvisor(proxyLogAdvisor);
                pf.addAdvisor(throttlingAdvisor);
                // reset proxies
                proxies.put(proxyName, (IProxy) pf.getProxy());
                cachedProxy = (T) pf.getProxy();
            }
        }

        return cachedProxy;
    }

    protected Advisor makeProxyLogAdvisor()
    {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, ProxyDebugLog.class);
        advisor.setPointcut(pointcut);
        advisor.setAdvice(new ProxyDebugLogInterceptor());
        return advisor;
    }

    protected Advisor makeThrottlingAdvisor()
    {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(ThrottlingConf.class, Throttling.class);
        advisor.setPointcut(pointcut);
        advisor.setAdvice(new ThrottlingInterceptor(getSwitch()));
        return advisor;
    }

    @Override
    public boolean getSwitch()
    {
        return flag;
    }

    @Override
    public void setSwitch(boolean on)
    {
        flag = on;
    }

    @Override
    public void setBeanFactory(BeanFactory bf) throws BeansException
    {
        beanFactory = bf;
    }

}
