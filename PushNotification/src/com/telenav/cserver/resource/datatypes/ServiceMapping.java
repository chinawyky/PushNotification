/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.resource.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * ServiceMapping.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-27
 *
 */
public class ServiceMapping 
{
	private String version;
	private List<ServiceItem> serviceMapping = new ArrayList<ServiceItem>();

	private String actionVersion;
	
	
	public ServiceMapping deepCopy()
	{
	    ServiceMapping mapping = new ServiceMapping();
	    mapping.setVersion(this.getVersion());
	    mapping.setActionVersion(this.getActionVersion());
	    
	    for(ServiceItem thisItem:this.serviceMapping)
	    {
	        if(thisItem instanceof BrowserServiceItem)
	        {
	        	mapping.serviceMapping.add(((BrowserServiceItem)thisItem).deepCopy());
	        }
	        else
	        {
	        	mapping.serviceMapping.add(thisItem.deepCopy());
	        }
	    }
	    
	    return mapping;
	    
	}
	
	public String getActionVersion() {
		return actionVersion;
	}

	public void setActionVersion(String actionVersion) {
		this.actionVersion = actionVersion;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the serviceMapping
	 */
	public List<ServiceItem> getServiceMapping() {
		return serviceMapping;
	}

	/**
	 * @param serviceMapping the serviceMapping to set
	 */
	public void setServiceMapping(List<ServiceItem> serviceMapping) {
		this.serviceMapping = serviceMapping;
	}
	
	/**
	 * add service item
	 * 
	 * @param item
	 */
	public void addServiceItem(ServiceItem item)
	{
		if(serviceMapping == null)
		{
			serviceMapping = new ArrayList<ServiceItem>();
		}
		serviceMapping.add(item);
	}
	
	public String toString(){
		return "version=" + version +",actionVersion=" + actionVersion+ ",serviceMapping=" + serviceMapping;
	}
}

