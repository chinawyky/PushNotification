/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author yqchen
 * @version 1.0 2007-2-6 9:56:54
 */
public class ResourceSetManager
{
	private final int MAP_SIZE = 8;
	private final Map sets = new HashMap(MAP_SIZE);
	
	/**
	 * return ResourceSet for given key
	 * 
	 * @param key
	 * @return
	 */
	public ResourceSet getResourceSet(String key)
	{
		ResourceSet set = (ResourceSet)sets.get(key);
//		if(set == null)
//		{
//			synchronized(sets)
//			{
//				set = (ResourceSet)sets.get(key);
//				if(set == null)
//				{
//					set = loadResourceSet(key);
//					sets.put(key, set);
//				}
//			}
//		}
		return set;
	}

//	/**
//	 * load resource set config
//	 * 
//	 * @param key
//	 * @return
//	 */
//	private ResourceSet loadResourceSet(String key)
//	{
//		String path = ResourceLoaderConfig.getSetConfig(key);
//		Map properties = (Map)ResourceFactory.createResource(path, 
//				ResourceFactory.TYPE_RESOURCE_BUNDLE);
//		ResourceSet set = new ResourceSet();
//		set.setMapping(properties);
//		set.setName(key);
//		return set;
//	}
	
	/**
	 * add resource set
	 */
	public void addResourceSet(String key, String path)
	{
		Map properties = (Map)ResourceFactory.createResource(path, 
				ResourceFactory.TYPE_RESOURCE_BUNDLE);
		ResourceSet set = new ResourceSet();
		set.setMapping(properties);
		set.setName(key);
		
		int k = path.lastIndexOf('.');
		if(k > -1)
		{
			String dir = path.substring(0, k);
			set.setDir(dir);
		}
		
		
		sets.put(key, set);
	}
	
	private static ResourceSetManager instance = new ResourceSetManager();

	public static ResourceSetManager getInstance()
	{
		return instance;
	}
	
}
