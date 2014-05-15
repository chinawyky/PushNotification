/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.service.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * CServerFilter, dispatch all poi/login cserver request to cserver server
 * because after cserver/browser server decouple, for 7.0 and 7.1 client
 * they will send cserver request to browser server
 * 
 * @author jzhu@telenav.cn
 * @version 1.0 2012-2-08
 * 
 */
public class CServerFilter implements Filter
{
    protected final Log logger = LogFactory.getLog(getClass());
    
    @SuppressWarnings("unused")
    private FilterConfig filterConfig = null;


    public void destroy()
    {
        this.filterConfig = null;
    }

    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        forward(request, response, chain);
    }
    
    
    private void forward(ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        
        String contextPath = req.getContextPath();
        
        String cserverContextPath = filterConfig.getInitParameter(contextPath);
        
        logger.debug("contextPath="+contextPath+"; dispatch to"+cserverContextPath);
        
        //for other browser servers than poi_service/login_startup_service, don't need dispatch
        if (cserverContextPath == null)
        {
            chain.doFilter(request, response);
            return;
        }
        
        String servletPath = req.getServletPath();
        
        ServletContext sc = filterConfig.getServletContext().getContext(cserverContextPath);
        if (sc == null)
        {
            logger.fatal("cannot find " + cserverContextPath + " servlet context! please check tomcat conf");
            chain.doFilter(request, response);
            return;
        }

        sc.getRequestDispatcher(servletPath).forward(request, response);
        
    }
    
    
}
