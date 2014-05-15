/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

/**
 * ThrottlingSwitch
 * @author kwwang
 *
 */
public interface ThrottlingSwitch
{
    void setSwitch(boolean on);
    
    boolean getSwitch();
}
