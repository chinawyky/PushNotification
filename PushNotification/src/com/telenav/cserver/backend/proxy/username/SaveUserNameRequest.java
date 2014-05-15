/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.username;

/**
 * AddUserNameRequest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Mar 9, 2011
 *
 */
public class SaveUserNameRequest
{
    private long userId;
    private String userName;
    
    public SaveUserNameRequest(long userId, String userName)
    {
        this.userId = userId;
        this.userName = userName;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("userId=").append(userId)
          .append(",userName=").append(userName);
        return sb.toString();
    }
    
    
}
