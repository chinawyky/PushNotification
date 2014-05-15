/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.username;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.telenav.cserver.framework.UserProfile;

/**
 * SaveRegisterIdRequest
 *
 * kwwang@telenav.cn
 * @version 1.0 Mar 9, 2011
 *
 */
public class SaveRegisterInfoRequest
{
    private UserProfile userProfile;
    private String registerId;
    
    public SaveRegisterInfoRequest(UserProfile userProfile, String registerId)
    {
        this.userProfile = userProfile;
        this.registerId = registerId;
    }

    public UserProfile getUserProfile()
    {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile)
    {
        this.userProfile = userProfile;
    }

    public String getRegisterId()
    {
        return registerId;
    }

    public void setRegisterId(String registerId)
    {
        this.registerId = registerId;
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }
    
    
}
