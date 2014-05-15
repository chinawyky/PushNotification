/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ptn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Messages.java
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2010-9-1
 *
 */
public class PtnProperties 
{
	private Map<String, String> map = null;
	
	/**
	 * @param map2
	 */
	@SuppressWarnings("unchecked")
	public PtnProperties(Map map) 
	{
        if (map == null)
        {
            this.map = new HashMap();
        }
        else
        {
            this.map = map;
        }
	}

	public void put(String key, String value)
	{
		map.put(key, value);
	}
	
	public String get(String key)
	{
	    return get(map, key);
	}
	
	public static String get(Map<String, String> map, String key)
	{
	    if (key == null)
	    {
	        return null;
	    }
	    
        String value = map.get(key);
        if (value != null)
        {
            return value;
        }
        
        
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext())
        {
            String regex = iterator.next();            
            if ("".equals(key.replaceFirst(regex, "")))
            {
                return map.get(regex);
            }
        }
        return null;
	}
	
	public String toString()
	{
		return map.toString();
	}
    
//    public static void main(String[] args)
//    {
//        Map<String, String> map = new java.util.HashMap<String, String> ();
//        map.put("4083063896", "att_beta");
//        map.put("409.......", "sprint_beta");
//        map.put("...567....", "nextel_beta");
//        
//        PtnProperties prop = new PtnProperties(map);
//        System.out.println(prop.get("4083063896"));
//        System.out.println(prop.get("4093063896"));
//        System.out.println(prop.get("4075673896"));
//        System.out.println(prop.get("4063063896"));
//        
//        
//    }
}
