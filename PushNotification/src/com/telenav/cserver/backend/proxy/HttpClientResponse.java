/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

/**
 * AbstractHttpClientResponse.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Apr 13, 2011
 *
 */
public abstract class HttpClientResponse
{
    
    public static final int STATUS_SUCESS = 200;
    
    private int statusCode;

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }
    
    public boolean isSuccess()
    {
    	return statusCode == STATUS_SUCESS;
    }
}
