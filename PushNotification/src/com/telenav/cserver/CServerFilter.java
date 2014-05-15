/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * CServerFilter.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Dec 10, 2010
 *
 */
public class CServerFilter implements Filter
{

    @Override
    public void destroy()
    {
        //no implementation
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        request.getRequestDispatcher("telenav-server").forward(request, response);
    }
    
    

    @Override
    public void init(FilterConfig arg0) throws ServletException
    {
    	//no implementation
        
    }

}
