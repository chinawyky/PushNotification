/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx;

import java.io.Serializable;

import com.telenav.cserver.framework.util.JSONUtil;

/**
 * BackendServerConfiguration.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 Jun 23, 2011
 * 
 */
public class BackendServerConfiguration implements Serializable
{
    private static final long serialVersionUID = 0x39ce06619ec14952L;

    private String name;

    private String description;

    private String serviceUrl;

    String loadConfMethod;

    String monitorMethod;

    public BackendServerConfiguration()
    {
        loadConfMethod = null;
        monitorMethod = null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getServiceUrl()
    {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl)
    {
        this.serviceUrl = serviceUrl;
    }

    String getLoadConfMethod()
    {
        return loadConfMethod;
    }

    void setLoadConfMethod(String initMethod)
    {
        loadConfMethod = initMethod;
    }

    String getMonitorMethod()
    {
        return monitorMethod;
    }

    void setMonitorMethod(String monitorMethod)
    {
        this.monitorMethod = monitorMethod;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append((new StringBuilder()).append("name=").append(name).append(",").toString());
        sb.append((new StringBuilder()).append("description=").append(description).append(",").toString());
        sb.append((new StringBuilder()).append("serviceUrl=").append(serviceUrl).append(",").toString());
        sb.append((new StringBuilder()).append("loadConfMethod=").append(loadConfMethod).append(",").toString());
        sb.append((new StringBuilder()).append("monitorMethod=").append(monitorMethod).toString());
        return sb.toString();
    }
    
    public String toJSON(){
        StringBuffer serviceJson = new StringBuffer("{");
        serviceJson.append("\"name\": ").append("\"").append(JSONUtil.filterSpecial(this.getName())).append("\", ")
                   .append("\"description\": ").append("\"").append(JSONUtil.filterSpecial(this.getDescription())).append("\", ")
                   .append("\"serviceUrl\": ").append("\"").append(JSONUtil.filterSpecial(this.getServiceUrl())).append("\" ")
                   .append("}");
        return serviceJson.toString();
    }
    
}
