/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.username;

/**
 * SaveRegisterIdResponse
 * @author kwwang
 *
 */
public class SaveRegisterInfoResponse
{
    private String status;

    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getStatus()
    {
        return status;
    }


    public boolean isSuccess()
    {
        return "0".equals(status);
    }
}
