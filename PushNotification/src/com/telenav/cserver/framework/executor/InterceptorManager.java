/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import java.util.Collection;

/**
 * InterceptorManager.java
 * 
 * @author sowmit@telenav.com
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 * 
 */
public interface InterceptorManager extends GlobalInterceptorManager
{
    public Collection<Interceptor> getAllPreInterceptors(String serviceType);

    public Collection<Interceptor> getAllPostInterceptors(String serviceType);

    public void addPreInterceptor(String serviceType, Interceptor interceptor);

    public void removePreInterceptor(String serviceType, Interceptor interceptor);

    public void addPostInterceptor(String serviceType, Interceptor interceptor);

    public void removePostInterceptor(String serviceType, Interceptor interceptor);

}
