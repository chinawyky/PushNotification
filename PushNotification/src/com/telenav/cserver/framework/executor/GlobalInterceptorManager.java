/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import java.util.List;

/**
 * GlobalInterceptorManager is intended to obtain global interceptors
 * 
 * @author kwwang
 * @date 2010-2-23
 */
public interface GlobalInterceptorManager
{
    public static final String GLOBAL_EXECUTE_TYPE="Global";
    
    public List<Interceptor> getAllPreGlobalInterceptors();

    public List<Interceptor> getAllPostGlobalInterceptors();
}
