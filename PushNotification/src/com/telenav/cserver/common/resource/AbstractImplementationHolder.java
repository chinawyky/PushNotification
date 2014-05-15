/**
 * (c) Copyright 2008 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.telenav.cserver.common.resource.constant.ResourceConst;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * abstract holder for class implementation management
 * 
 * The resouce file is combined of key and value pair, i.e.
 * orders=carrier,version,locale
 * 1=Cingular_WM_5.2.??_*_en-US:com.telenav.j2me.server.manager.impl.USBillingUserManager
 * 2=Cingular_WM_5.5.*_*_*:com.telenav.j2me.server.manager.impl.USBillingUserManager
 * Attributes are seperated by "_".
 * 
 * We introduce wildcard here, currently we have '*', '?'.
 * '*' -> to represent any dedicated attribute, whole word.
 * 		  We don't support multiple asterisks, so it can be only allowed at the begining or end. 
 * '?' -> to represent any character
 *  
 * 
 * The matching will be processed as ordered, also, locale attribute should be
 * wrote as "en-US" instead of "en_US" to avoid confliction with key string ,
 * version is similar to locale, i.e, "5.2.09_free" -> "5.2.09-free"
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2008-3-25
 *
 */
public abstract class AbstractImplementationHolder extends AbstractResourceHolder
{
	/**
	 * key for orders
	 */
	public final static String KEY_WILDCARD_ORDERS = "orders";
	
	/**
	 * key for rule numbers 
	 */
	public final static String KEY_RULE_NUMBER = "RULE_NUMBER";
	
	/**
	 * keyword for rules in ResourceContent
	 */
	public final static String KEY_LIST = "LIST";
	
	/**
	 * orders with wildcard
	 */
	protected LoadOrders wildcardOrders;
	
    /**
     * create a object
     * 
     * @param keyObject
     * @param argument
     *            argument to create the object
     * @return
     */
    public ResourceContent createObject(String keyObject,
            UserProfile profile, TnContext tnContext)
    {    	
        Map map = (Map) ResourceFactory.createResource(this, profile, tnContext);
        
        if(map == null)
        {
        	return null;
        }
        
        String orders = (String)map.get(KEY_WILDCARD_ORDERS);
        String ruleNumber = (String)map.get(KEY_RULE_NUMBER);
        
        wildcardOrders = new LoadOrders();
        wildcardOrders.addOrderString(orders);
        
        //remove this attribute, since it is just a flag.
        map.remove(KEY_WILDCARD_ORDERS);
        map.remove(KEY_RULE_NUMBER);
        
        ResourceContent content = new ResourceContent();    
        
        ArrayList list = new ArrayList();
       
        int ruleSize = map.size();
        if(ruleNumber != null)
        {
        	try
        	{
        		ruleSize = Integer.parseInt(ruleNumber);
        	}
        	catch(NumberFormatException e)
        	{
        		logger.warn(e, e);
        	}
        }
        for(int i =0; i < ruleSize; i ++)
        {
            String valueString = (String)map.get(Integer.toString(i));
            if(valueString == null)
            {
            	logger.warn("Please check configuration file, items should be ordered.");
            	continue;
            }
            
            int k = valueString.indexOf(':');
            if(k < 0)
            {
            	continue;
            }
            String key = valueString.substring(0, k);
            String value = valueString.substring(k + 1);
            
            //parse key as String[]
            //"Cingular_WM_5.2.??_*" -> new String[]{"Cingular", "WM", "5.2.??", "*"}
            StringTokenizer token = new StringTokenizer(key, "_");
            String[] keyStrings = new String[token.countTokens()];
            int index = 0;
    		while(token.hasMoreTokens())
    		{
    			keyStrings[index ++] = token.nextToken().toUpperCase();
    		}

    		MatchObject mo = new MatchObject();
    		mo.keyStrings = keyStrings;
    		mo.value = value;
    		list.add(mo);
        }
        content.setProperty(KEY_LIST, list);
        
        if(logger.isDebugEnabled())
        {
            logger.debug("==========" + getClass().getName() + " map:" + map);
            logger.debug("==========" + getClass().getName() + " wildcardOrders:" + orders);
        }
        return content;
    }
    
    /**
     * class name <-> instance
     */
    private HashMap class2Instance = new HashMap();
    
    
    /**
     * create new instance by given className
     * subclass can overwrite this method to use special method to create a new instance
     * 
     * @param className
     * @return
     */
    protected Object newInstance(String className)
    {    	
    	if(logger.isDebugEnabled())
    	{
    		logger.debug("newInstance:" + className);
    	}
    	
    	try
		{
			Class<?> clazz = Class.forName(className);
			return clazz.newInstance();
		} catch (ClassNotFoundException e)
		{
			logger.info(ResourceConst.NO_GETINSTANCE_IN_CLASS + className);
		} catch (InstantiationException e)
		{
			logger.info(ResourceConst.NO_GETINSTANCE_IN_CLASS + className);
		} catch (IllegalAccessException e)
		{
			logger.info(ResourceConst.NO_GETINSTANCE_IN_CLASS + className);
		}
    	
		return null;
    }
    
    /**
     * return implementation class name
     * 
     * @param wrapper
     * @return
     */
    protected String getMatchedString(UserProfile profile, TnContext tnContext)
    {
    	ResourceContent content = getResourceContent(profile, tnContext);
    	if(content == null)
    	{
    		return null;
    	}
    	List list = (List)content.getProps().get(KEY_LIST);
    	
    	return WildcardMatchUtil.match(wildcardOrders, profile, tnContext, list);
    }
    
    /**
     * return implementation instance
     * 
     * @param wrapper
     * @return
     */
    protected Object getImplementationInstance(UserProfile profile, TnContext tnContext)
    {
    	String className = getMatchedString(profile, tnContext);
    	if(className == null)
    	{
    		return null;
    	}
    	Object instance = class2Instance.get(className);
    	if(instance == null)
    	{
    		synchronized (class2Instance)
    		{
    			instance = class2Instance.get(className);
    	    	if(instance == null)
    	    	{
    	    		instance = newInstance(className);
    	    		class2Instance.put(className, instance);
    	    	}
    		}
    	}
    	return instance;
    }
}
