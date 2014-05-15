package com.telenav.cserver.common.resource;

import java.util.*;

import com.telenav.cserver.common.resource.constant.ResourceConst;

/**
 * ResourceUtil Class
 * 
 * @author shwang
 * 
 */
public class ResourceUtil
{
	private ResourceUtil()
	{

	}

	/**
	 * read properties from ResourceBundle into a Map, while the value is valid
	 * 
	 * @param rb
	 * @return Map with properties from ResourceBundle
	 */
	public static Map getResource(ResourceBundle rb)
	{
		Map map = new HashMap();
		Enumeration enumeration = rb.getKeys();
		while (enumeration.hasMoreElements())
		{
			String key = null;
			key = (String) enumeration.nextElement();
			String value = rb.getString(key);
			if (value != null && !value.equals(""))
			{
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * read properties from Properties into a Map, while the value is valid
	 * 
	 * @param properties
	 * @return Map with properties from Properties
	 */
	public static Map getResource(Properties properties)
	{
		Map map = new HashMap();
		Enumeration enumeration = properties.keys();
		while (enumeration.hasMoreElements())
		{
			String key = null;
			key = (String) enumeration.nextElement();
			String value = properties.getProperty(key);
			if (value != null && !value.equals(""))
			{
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * split a String into a String Array with given token
	 * 
	 * @param string
	 * @param token
	 * @return
	 */
	public static String[] split(String string, String token)
	{
		Vector v = new Vector();
		StringTokenizer st = new StringTokenizer(string, token);
		while (st.hasMoreTokens())
		{
			v.add(st.nextToken().trim());
		}
		String[] results = new String[v.size()];
		for (int i = 0; i < v.size(); i= i + 1)
		{
			results[i] = (String) v.get(i);
		}
		return results;
	}

	private static Map resourcePool = new HashMap();

	public static PropertyResourceBundle getResourceBundle(
			String resourceName)
	{
		return getResourceBundle(resourceName, null);
	}
	
	public static PropertyResourceBundle getResourceBundle(
			String resourceName, Locale locale)
	{

		String key = resourceName;
		if(locale == null)
		{
			locale = Locale.getDefault();
		}
		else
		{
			key = resourceName + ResourceConst.UNDERSCORE + locale;
		}
		
		PropertyResourceBundle bundle = (PropertyResourceBundle) resourcePool
				.get(key);

		if (bundle == null)
		{
			synchronized (resourcePool)
			{			
				bundle = (PropertyResourceBundle) resourcePool
						.get(resourceName);

				if (bundle == null)
				{
					bundle = (PropertyResourceBundle) PropertyResourceBundle
							.getBundle(resourceName, locale);
					resourcePool.put(key, bundle);
				}
			}
		}

		return bundle;
	}

}
