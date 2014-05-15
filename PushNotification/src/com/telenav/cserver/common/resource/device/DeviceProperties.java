/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.device;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * DeviceProperties.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-6
 * no need to trim the value, for we have already trimmed them in TelenavResourceBundler.java
 *
 */
public class DeviceProperties
{
	
	private static final Logger LOGGER = Logger
            .getLogger(DeviceProperties.class);
	public Map attributes;
	
	private static final String IN_DEVICE_PROPERTIES = "in device.properties";

	private static final boolean TRUE=true;

	private static final boolean FALSE=false;

	DeviceProperties(Map attributes)
	{
		this.attributes = attributes;
	}

	public int getInt(String key)
	{
		return getInt(key, -1);
	}
	
	private boolean validAttrAndKey(String key)
	{
		boolean returnValue = false;
		if(attributes != null && attributes.containsKey(key))
		{
			returnValue = true;
		}
		return returnValue;
		
	}

	public int getInt(String key, int defValue)
	{
		if (validAttrAndKey(key))
		{
			String sRtn = (String)attributes.get(key);
			if (StringUtils.isNotEmpty(sRtn) && StringUtils.isNumeric(sRtn))		
			{				
				return Integer.parseInt(sRtn);				
			}				
		}
		LOGGER.fatal("No " + key + IN_DEVICE_PROPERTIES);
		return defValue;		
	}
	
	public boolean getBooleanDefTrue(String key)
	{
		return getBoolean(key, TRUE);
	}

	public boolean getBooleanDefFalse(String key)
	{
		return getBoolean(key, FALSE);
	}


	public boolean getBoolean(String key)
	{
		return getBoolean(key, false);
	}

	/**
     * Get device property value, if the key does not exist, we can use the def.
     * @param key
     * @param def
     * @return
	 */
   
    public boolean getBoolean(String key, boolean def)
	{
		if (validAttrAndKey(key))
		{
			String sRtn = (String)attributes.get(key);
			if (sRtn != null && sRtn.length() > 0)
			{
				return Boolean.valueOf(sRtn);
			}					
		}
		LOGGER.fatal("No " + key + IN_DEVICE_PROPERTIES);
		return def;
		
	}

    public String getString(String key)
    {
       return getString(key, "");
    }  
    
    public String getString(String key, String def)
	{
		if (validAttrAndKey(key))
		{
			String sRtn = (String)attributes.get(key);
			if (sRtn != null && sRtn.length() > 0)
			{
				return sRtn;
			}
		}
		LOGGER.fatal("No " + key + IN_DEVICE_PROPERTIES);
		return def;		
	}

    public String getGroupString(String key, String suffix)
    {
        return getGroupString(key, suffix, "");
    }

    public String getGroupString(String key, String suffix, String defaultValue)
    {
        Map<String, String> map = getStringGroup(key);
        String value = map.get(key+"."+suffix);

        if (value != null)
        {
            return value;
        }

        //if suffix is locale, then we need check if the value exist with the language
        int idx = suffix.indexOf('_');
        if (idx > 0)
        {
            value = map.get(key+"."+suffix.substring(0,idx));
            if (value != null)
            {
                return value;
			}
        }
        value = map.get(key);

        if (value == null)
        {
            return defaultValue;
        }
        else
        {
            return value;
        }
    }


    public Map<String, String> getStringGroup(String key)
    {
        Map<String, String> map = new HashMap<String, String>();      
        if (attributes != null && !attributes.isEmpty())
        {
            Iterator<String> iterator = attributes.keySet().iterator();
            while(iterator.hasNext())
            {
                String str = iterator.next();
                if (str.startsWith(key+".") || str.equals(key))
                {
                    map.put(str, (String)attributes.get(str));
                }
            }
        }
        if (map.isEmpty())
        {
            // no config or incorrect config
        	LOGGER.fatal("No key group for " + key + IN_DEVICE_PROPERTIES);
        }
        return map;
    }
    
    public Map getAllDeviceAtrributes()
    {
    	return attributes;
    }

    @Override
    public String toString()
    {
        StringBuffer stringbuf = new StringBuffer();
        if (attributes != null)
        {
            Iterator<Entry> iterator = attributes.entrySet().iterator();
            Entry entry = null;
            while (iterator.hasNext())
            {
                entry = iterator.next();
                stringbuf.append(entry.getKey()+"="+entry.getValue()+",\n");
            }
        }
        String result = stringbuf.toString();
        if( result.length() > 0 )
        {
            result = result.substring(0,result.length()-2);
        }
        return result;
    }
}
