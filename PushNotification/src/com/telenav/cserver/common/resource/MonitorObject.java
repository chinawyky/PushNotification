/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MonitorData.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Nov 9, 2011
 *
 */
public class MonitorObject
{
    private String name;
    private Map<?,?> data = new LinkedHashMap();
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public Map<?, ?> getData()
    {
        return data;
    }
    public void setData(Map<?, ?> data)
    {
        this.data = data;
    }
    
    
}
