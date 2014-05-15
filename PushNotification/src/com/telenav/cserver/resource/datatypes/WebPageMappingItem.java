package com.telenav.cserver.resource.datatypes;

import java.util.Map;

public class WebPageMappingItem
{
    private String service;
    private String urlEntry;
    private Map<String, String> webpageMapping;
    public String getService()
    {
        return service;
    }
    public void setService(String service)
    {
        this.service = service;
    }
    public String getUrlEntry()
    {
        return urlEntry;
    }
    public void setUrlEntry(String urlEntry)
    {
        this.urlEntry = urlEntry;
    }
    public Map<String, String> getWebpageMapping()
    {
        return webpageMapping;
    }
    public void setWebpageMapping(Map<String, String> webpageMapping)
    {
        this.webpageMapping = webpageMapping;
    }
    
}
