/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.resource.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ServiceItem.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-27
 *
 */
public class ServiceItem
{
	private String type;
	private List<String> actions = new ArrayList<String>();
	private List<String> addresses;
	
	private String serviceDomainName="";
	private Map<String,String> urlMap = new HashMap<String,String>();
	
	
	public ServiceItem deepCopy()
	{
	    ServiceItem item = createServiceItem();
	    item.setType(this.getType());
	    item.setServiceDomainName(this.getServiceDomainName());
	    
	    if (this.actions != null)
	    {
	        item.actions = new ArrayList<String>();
	        item.actions.addAll(this.actions);
	    }
	    
	    if (this.addresses != null)
        {
            item.addresses = new ArrayList<String>();
            item.addresses.addAll(this.addresses);
        }
	    
	    item.urlMap.putAll(urlMap);
	    
	    return item;
	}
	
	protected ServiceItem createServiceItem()
	{
		return new ServiceItem();
	}

	
	
	public Map<String, String> getUrlMap() {
		return urlMap;
	}
	public void setUrlMap(Map<String, String> urlMap) {
		this.urlMap = urlMap;
	}
	public String getServiceDomainName() {
		return serviceDomainName;
	}
	public void setServiceDomainName(String serviceDomainName) {
		this.serviceDomainName = serviceDomainName;
	}
	/**
	 * @return the actions
	 */
	public List<String> getActions() {
		return actions;
	}
	/**
	 * @param actions the actions to set
	 */
	public void setActions(List<String> actions) {
		this.actions = actions;
	}
	/**
	 * @return the name
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param name the name to set
	 */
	public void setType(String name) {
		this.type = name;
	}
	
	
	public String toString(){
		return "ServiceItem: name=" + type 
				+ ",actions=" + actions + ",serviceDomainName="+serviceDomainName +",urlMap=" + urlMap;
	}		
}