/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import com.telenav.cserver.common.cache.CacheModel;

/**
 * ResourceHolder: to hold one kind of resource, i.e. preference,audio,etc.
 * 
 * 
 * @author yqchen
 * @version 1.0 2007-2-6 10:29:24
 */
public interface ResourceHolder extends CacheModel
{
	
	public void addUserProfileKeyMapping(String userKey,String fileLoadingPath);
	/**
	 * return name to identify
	 * @return
	 */
	public String getName();
	
	/**
	 * set name
	 * 
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * type: can be xml, resource_bundle, or others
	 * 
	 * @return
	 */
	public String getType();
	
	/**
	 * set type
	 * 
	 * @param type
	 */
	public void setType(String type);
	
	
	/**
	 * return config path
	 * 
	 * @return null if not set
	 */
	public String getConfigPath();
	
	/**
	 * set config path, it will be called when there is no set 
	 * 
	 * @param configPath
	 */
	public void setConfigPath(String configPath);
	
	/**
	 * initiate holder, can be used for cache
	 *
	 */
	public void init();
	
	/**
	 * return ResourceSet
	 * 
	 * @return
	 */
	public ResourceSet getResourceSet();
	
	/**
	 * set ResourceSet
	 * 
	 * @param set
	 */
	public void setResourceSet(ResourceSet set);
	
	/**
	 * set ResourceSet through set name
	 * 
	 * @param setName
	 */
	public void setResourceSet(String setName);
	
	/**
	 * set load orders
	 * 
	 * @param loadOrders
	 */
	public void setLoadOrders(LoadOrders loadOrders);
	
	/**
	 * @return the LoadOrders
	 */
	public LoadOrders getLoadOrders();
	
}
