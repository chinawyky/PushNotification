/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.resource.datatypes;



/**
 * BrowserServiceItem
 * 
 * @author kwwang
 * 
 */
public class BrowserServiceItem extends ServiceItem{
	private String baseUrl;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	
	public ServiceItem deepCopy()
	{
		BrowserServiceItem item =(BrowserServiceItem)super.deepCopy();
		item.baseUrl=this.baseUrl;
	    return item;
	}
	
	@Override
	protected ServiceItem createServiceItem() {
		return new BrowserServiceItem();
	}
}
