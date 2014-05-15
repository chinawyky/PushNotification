/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Messages.java
 * 
 * Attention! We don't check "map == null" in the constructor nor declare NullPointerException in our function define, and we use map.get("key") function directly.
 * This may throws NullPointerException when you forget to add messages.propertie for your device.
 * So please make sure to add the messages.properties file in the related device configuration folder.
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-7
 *
 */
public class Messages 
{
    private static final Logger LOGGER=Logger.getLogger(Messages.class);
    private static final String DOT = ".";
       
	private final Map<String, String> map;
	
	public Map<String,String> getMessages()
	{
		return map;
	}
	
	/**
	 * @param map2
	 */
	@SuppressWarnings("unchecked")
	public Messages(Map map) 
	{
		this.map = map;
	}

	public void put(String key, String value)
	{
		map.put(key, value);
	}
	
	public String get(String key)
	{
		return map.get(key);
	}
	
	public String toString()
	{
		return map == null ? "" : map.toString();
	}
	
	public String getBy(Enum e)
	{
	    return map.get(e.toString().toLowerCase());
	}
	
	public String getByEnumWithPrefix(Enum e,String prefix)
    {
        return map.get(prefix+DOT+e.toString().toLowerCase());
    }
	
    public Map<String, Object> getBatchStringWithPrefix(String prefix)
    {
        Map<String, Object> strs = new HashMap<String, Object>();
        Iterator<String> keys = map.keySet().iterator();
        while (keys.hasNext())
        {
            String key = keys.next();
            if (!key.startsWith(prefix))
            {
                continue;
            }
            
            Object value = map.get(key);
            
            String storeKey=key.substring(prefix.length() + 1);
            
            if (isListKey(key))
            {
                String realKey = removeSuffixedNumFrom(key).substring(prefix.length() + 1);
                if (strs.containsKey(realKey))
                {
                    List list = (List) strs.get(realKey);
                    list.add(value);
                }
                else
                {
                    List list = new ArrayList();
                    list.add(value);
                    strs.put(realKey, list);
                }
            }
            //in this case, list comes first
            else if(!isListKey(key)&&strs.get(storeKey) instanceof List)
            {
                //abandon the new value
                LOGGER.fatal("abandon value "+ value+" since there is already same key with list value there, original value->"+strs.get(storeKey));
            }
            else
            {
                strs.put(storeKey, value);
            }

        }
        return strs;
    }
	
	public String removeSuffixedNumFrom(String key)
	{
	    String realKey="";
	    if(isListKey(key))
	    {
	        realKey=key.substring(0,key.lastIndexOf(DOT));
	    }
	    return realKey;
	}
	
	/**
	 * The listkey should be like "login.feedback.option.1" with a number suffixed
	 * @param key
	 * @return
	 */
	public boolean isListKey(String key)
	{
	    boolean isListKey=false;
	    if(key==null)
	    {
	        return isListKey;
	    }
	    
	    int digitIdx=key.lastIndexOf(DOT);
	    String keySuffix=null;
	    if(digitIdx!=-1)
	    {
	        keySuffix=key.substring(digitIdx+1);
	    }
	    
	    isListKey=StringUtils.isNotEmpty(keySuffix)&&StringUtils.isNumeric(keySuffix);
	    return isListKey;
	}
}
