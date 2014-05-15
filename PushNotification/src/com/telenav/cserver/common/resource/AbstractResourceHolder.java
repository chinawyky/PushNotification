/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import com.telenav.cserver.common.cache.AbstractCacheModel;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * Abstract ResourceHolder
 * 
 * @author yqchen
 * @version 1.0 2007-2-6 10:47:35
 */
public abstract class AbstractResourceHolder extends AbstractCacheModel
		implements
			ResourceHolder
{
	protected String name;
	protected String type;
	protected String configPath;
	protected ResourceSet set;
	protected LoadOrders loardOrders;

    //make ResourceCacheManagement singleton to all the holders
	private static final   ResourceCacheManagement CACHE_MGMT = ResourceCacheManagement.getInstance();
	
	public static final String KEY_VERSION = "Version";
	
	public static final NullObject nil = new NullObject();
	
	protected Map<String,String> userKeyMapping=new HashMap<String, String>();

	protected Logger logger = Logger.getLogger(getClass());

	/**
	 * initiate holder, can be used for cache
	 * 
	 */
	public void init()
	{
		long start = System.currentTimeMillis();
		if (logger.isDebugEnabled())
		{
			logger.debug("Initiating ....");
		}
		doInit();

		if (logger.isDebugEnabled())
		{
			logger.debug("Initiated, cost "
					+ (System.currentTimeMillis() - start) + " ms.");
		}
	}

	/**
	 * initiate holder, can be used for cache
	 * 
	 */
	public void doInit()
	{
		//do nothing
		CACHE_MGMT.addHolder(this);
	}

	/**
	 * create a object
	 * 
	 * @param key
	 * @param argument
	 *            argument to create the object
	 * @return
	 */
	public Object createObject(Object key, Object argument)
	{
		return createObject((String) key, (UserProfile) argument, null);
	}

	/**
	 * create a object
	 * 
	 * @param key
	 * @param argument
	 *            argument to create the object
	 * @return
	 */
	public abstract ResourceContent createObject(String key,
			UserProfile profile, TnContext tnContext);

	/**
	 * get a object from cache rewrite parent's method, to offer a more
	 * convenient way for calling
	 * 
	 * @param key
	 * @param argument
	 *            argument to create the object
	 */
	public ResourceContent get(Object key, UserProfile profile)
	{
		return (ResourceContent) get(key, (Object) profile);
	}
	
	
	@Override
	public void addUserProfileKeyMapping(String userKey, String fileLoadingPath) {
		userKeyMapping.put(userKey, fileLoadingPath);
	}
	
	public Map<String,String> getUserKeyMapping()
	{
		return userKeyMapping;
	}

	public Object get(Object key,Object argument)  
	{
		Map map=this.getMap();
		
		Object value = map.get(userKeyMapping.get(key));
		if( value == null ){
		   synchronized(map)
		   {
		      value = map.get(userKeyMapping.get(key));
		      if(value == null)
		      {
					value = createObject(key, argument);
					if( value != null ){
				    	if(userKeyMapping.get(key)!=null&&!map.containsKey(userKeyMapping.get(key)))
				    	{
				    		CACHE_MGMT.addCacheObject(this, key, value);
				    	}
					}
					//The order of set up cache is very important. before put the value in map, we must  set up cache Management information
                    if(userKeyMapping.get(key)!=null&&!map.containsKey(userKeyMapping.get(key))){
                    	map.put(userKeyMapping.get(key), value);
                    }
                   
                    if(value == null){
                		map.put(userKeyMapping.get(key), nil);
                	}
		      }
		   }
		}else if(value.equals(nil)){
			return null;
		}
		
		return value;
	}

	/**
	 * get a object from cache rewrite parent's method, to offer a more
	 * convenient way for calling
	 * 
	 * @param key
	 * @param argument
	 *            argument to create the object
	 */
	public ResourceContent getResourceContent(UserProfile profile, TnContext tc)
	{
		String key = getKey(profile, tc);
		if (logger.isDebugEnabled())
        {
            logger.debug("Holder:" + this.getName() + " Resource Cachekey: " + key);
        }
		return (ResourceContent) get(key, (Object) profile);
	}

	/**
	 * generate key from UserInfoWrapper
	 * 
	 * @param wrapper
	 * @return
	 */
	public String getKey(UserProfile profile, TnContext tc)
	{
		if(profile == null)
		{
			return name;
		}
		

//		 use "name + set + suffix" as key , i.e. preference_holder_1_en_US.
		String key = name;
		if(set != null)
		{
			key = name + "_" + set.getName();
		}

        List<LoadOrder> orders = getLoadOrders().getOrders();
		key = name + ResourceFactory.getConfigSuffixForKey(orders, profile, tc);
        
		return key;
	}
	
	public String getFilePath(UserProfile userProfile, TnContext tc)
	{
	    String key = this.getKey(userProfile, tc);
	    return userKeyMapping.get(key);
	    
	}
	

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getConfigPath()
	{
		return configPath;
	}

	public void setConfigPath(String configPath)
	{
		this.configPath = configPath;
	}


	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ResourceSet getResourceSet()
	{
		return set;
	}

	public void setResourceSet(ResourceSet set)
	{
		this.set = set;
	}

	public void setResourceSet(String setName)
	{
		ResourceSet set = ResourceSetManager.getInstance().getResourceSet(
				setName);
		this.set = set;
	}
	
	public TxNode getTxNode(UserProfile profile, TnContext tc)
	{	
		ResourceContent content = getResourceContent(profile, tc);
		return content.getNode();
	}
	
	public TxNode[] getTxNodes(UserProfile profile, TnContext tc)
	{	
		ResourceContent content = getResourceContent(profile, tc);
		return content.getNodes();
	}
	
	public String getVersion(UserProfile profile, TnContext tc)
	{
		ResourceContent content = getResourceContent(profile, tc);	
		return content.getVersion();
	}
    
    public LoadOrders getLoadOrders()
    {
        return loardOrders;
    }

    public void setLoadOrders(LoadOrders loardOrders)
    {
        this.loardOrders = loardOrders;
    }
	
    static class NullObject extends Object {  
        public NullObject() {  
        	//generate a null object
        }  
    }  
}
