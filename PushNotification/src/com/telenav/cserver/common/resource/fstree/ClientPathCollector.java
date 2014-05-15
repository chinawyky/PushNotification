/*
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.fstree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import com.telenav.cserver.common.resource.AbstractResourceHolder;
import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * Only for test.
 * Invoke by RetrieveConfContentServlet to retrieve all the confs.
 * @author huishen
 * @since 2013-03-14
 *
 */
public class ClientPathCollector
{   
    private static List<String> getClientPath(UserProfile userProfile, TnContext tnContext, Collection<ResourceHolder> holders, boolean needContent)
    {
        List<String> result = new ArrayList<String>();
        
        AbstractResourceHolder resourceHolder;
        ResourceContent resourceContent;
        String path;
        
        for (Iterator<ResourceHolder> holderIterator = holders.iterator(); holderIterator.hasNext();)
        {
            resourceHolder = (AbstractResourceHolder) holderIterator.next();
            try
            {
                resourceContent = (ResourceContent) resourceHolder.getResourceContent(userProfile, tnContext);
                path = resourceHolder.getFilePath(userProfile, tnContext);
                if (needContent)
                {
                    if (path.endsWith("NOTFOUND"))
                    {
                        result.add(resourceHolder.getName() + ":NOTFOUND");
                    }
                    else 
                    {
                        result.add(resourceHolder.getName() + ":FOLLOWING");
                        result.addAll(getContentList(resourceContent));
                    }
                }
                else 
                {
                    result.add(resourceHolder.getName() + path);
                }
                
            } catch (Exception e)
            {
                result.add(resourceHolder.getName() + ": " + e.toString());
            }
        }
        return result;
    }
    
    private static List<String> getContentList(ResourceContent resourceContent) throws SecurityException, IllegalArgumentException, IllegalAccessException
    {
        Entry entry = null;
        Object value;
        List<String> result = new ArrayList<String>(); 
        Field field;
        String string;
        int index;
        
        value = resourceContent.getObject();
        if (value != null)
        {
            result.add("object: " + value.toString());
        }

        Map<String, Object> values = resourceContent.getProps();
        Iterator<Entry<String, Object>> entryIterator = values.entrySet().iterator();
        
        while (entryIterator.hasNext())
        {
            entry = entryIterator.next();
            value = entry.getValue();
            string = value.toString();
            index = string.lastIndexOf('@');
            if (index == -1)
            {
                result.add(string);
            }
            else 
            {
                result.add(string.substring(0, index));
            }
            
            try
            {
                field = value.getClass().getDeclaredField("map");
                field.setAccessible(true);
                result.add(toString((Map)field.get(value)));
            } 
            catch (NoSuchFieldException e)
            {
                result.add("No map.");
            }
            
        }
        return result;
    }
    
    public static List<String> getClientPath(String client, Collection<ResourceHolder> holders, boolean needContent)
    {
        String[] infos = client.split(";");
        UserProfile userProfile = new UserProfile();
        userProfile.setProgramCode(infos[0]);
        userProfile.setPlatform(infos[1]);
        userProfile.setVersion(infos[2]);
        userProfile.setProduct(infos[3]);
        userProfile.setDevice(infos[4]);
        userProfile.setAttribute(UserProfile.ARRT_RESOLUTION, infos[5]);
        userProfile.setLocale(infos[6]);
        userProfile.setRegion(infos[7]);
        userProfile.setCarrier(userProfile.getProgramCode());
        return getClientPath(userProfile, new TnContext(), holders, needContent);
    }
    
    private static String toString(Map<String, String> values)
    {
        StringBuffer sb = new StringBuffer();
        if (values != null)
        {
            Iterator<Entry<String, String>> iterator = values.entrySet().iterator();
            Entry entry = null;
            while (iterator.hasNext())
            {
                entry = iterator.next();
                sb.append(entry.getKey()+" = "+entry.getValue()+",\n");
            }
        }
        String result = sb.toString();
        return result;
    }
}