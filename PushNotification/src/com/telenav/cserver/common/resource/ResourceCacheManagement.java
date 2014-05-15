/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.telenav.cserver.common.cache.AtomicCounter;
import com.telenav.cserver.common.cache.Counter;
import com.telenav.cserver.common.cache.MemorySize;
import com.telenav.cserver.common.cache.SizeOfObject;
import com.telenav.cserver.common.cache.SizeOfObjectFactory;

/**
 * ResourceCacheManagement.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-1-21
 * 
 */
public final class ResourceCacheManagement
{
    private static final Logger LOGGER = Logger.getLogger(ResourceCacheManagement.class);

    // containers of all the holders
    private final Set<ResourceHolder> holderSet = new HashSet<ResourceHolder>();
    
    private final List<IMonitor> monitors = new ArrayList<IMonitor>();

    // total cache size, need atomic counter
    private Counter totalCacheSize = new AtomicCounter(0);

    //default is html display format
    private DisplayFormat displayFormat = new HTMLDisplayFormat(this);

    private SizeOfObject sizeOfObject = SizeOfObjectFactory.getInstance();

    private final static ResourceCacheManagement INSTANCE = new ResourceCacheManagement();

    public static ResourceCacheManagement getInstance()
    {
        return INSTANCE;
    }
    
    public void registerMonitorObject(IMonitor obj){
        monitors.add(obj);
    }

    private ResourceCacheManagement(){}
    
    
    public void setCacheSize(Counter cacheSize)
    {
        this.totalCacheSize = cacheSize;
    }

    public void addHolder(ResourceHolder holder)
    {
        holderSet.add(holder);
    }
    
    public Set<ResourceHolder> getHolderSet()
    {
        return holderSet;
    }
    
    public SizeOfObject getSizeOfObject()
    {
        return sizeOfObject;
    }
    
    public List<IMonitor> getMonitors(){
        return this.monitors;
    }
    
    public void setDisplayFormat(DisplayFormat displayFormat)
    {
        this.displayFormat = displayFormat;
    }
    
    public String contents(String holderName, String key)
    {
        
        return displayFormat.contents(holderName, key);
    }

    public String statistic()
    {
        return displayFormat.statistic();
    }

    public String details(String holderName)
    {
        return displayFormat.details(holderName);
    }

    /**
     * may throw iterator concurrent modify exception
     * 
     * @return
     */
    public String details()
    {
        return displayFormat.details();
    }

    public String toString()
    {
        return statistic();
    }

    
    void addCacheObject(ResourceHolder holder, Object key, Object object)
    {
        long objectSize = setSizeOfCacheObject(object);
        totalCacheSize.getAndAdd(objectSize);
    }

    long getCountOfCacheObject()
    {
        long result = 0;
        for (ResourceHolder holder : holderSet)
        {
            result += getCountOfCacheObject(holder);
        }
        return result;
    }

    long getTotalCacheSize()
    {
        if (getCountOfCacheObject() == 0)
        {
            totalCacheSize = new AtomicCounter(0);
        }
        return totalCacheSize.value();
    }

    void setSizeOfObject(SizeOfObject sizeOfObject)
    {
        this.sizeOfObject = sizeOfObject;
    }

    int getCountOfHolderType()
    {
        Set<Class> set = new HashSet<Class>();
        for (ResourceHolder holder : holderSet)
        {
            set.add(holder.getClass());
        }
        return set.size();
    }

    int getCounterOfHolder()
    {
        return holderSet.size();
    }

    void clear()
    {
        holderSet.clear();
        totalCacheSize.getAndSet(0);
    }

    String getSimpleClassName(String allName)
    {
        if (allName == null)
        {
            return null;
        }
        if (allName.lastIndexOf('.') != -1)
        {
            return allName.substring(allName.lastIndexOf('.') + 1);
        }
        return allName;

    }

    int getCounterOfHolder(String holderName)
    {
        int foundCount = 0;
        for (ResourceHolder holder : holderSet)
        {
            if (holder.getName().equals(holderName))
            {
                foundCount = foundCount + 1;
            }
        }
        return foundCount;
    }

    /**
     * return the holder of the name, if there are more than one holder, return any one
     * 
     * @param holderName
     * @return
     */
    ResourceHolder getHolder(String holderName)
    {
        for (ResourceHolder holder : holderSet)
        {
            if (holder.getName().equals(holderName))
            {
                return holder;
            }
        }
        return null;
    }

    long getCountOfCacheObject(ResourceHolder holder)
    {
        return ((AbstractResourceHolder) holder).getMap().size();
    }

    long getCacheSize(ResourceHolder holder)
    {
        long resultSize = 0;
        Iterator<Object> it = ((AbstractResourceHolder) holder).getMap().values().iterator();
        while (it.hasNext())
        {
            resultSize += getSizeOfCacheObject(it.next());
        }
        return resultSize;

    }

    long getSizeOfCacheObject(Object cacheObject)
    {
        long sizeOfCacheObject = 0;
        if (cacheObject instanceof MemorySize)
        {
            sizeOfCacheObject = ((MemorySize) cacheObject).getMemorySize();
        }
        else
        {
            sizeOfCacheObject = sizeOfObject.deepSizeOf(cacheObject);
        }
        return sizeOfCacheObject;
    }

    private long setSizeOfCacheObject(Object cacheObject)
    {
        long objectSize = sizeOfObject.deepSizeOf(cacheObject); // compute cache size
        if (cacheObject instanceof MemorySize)
        {
            ((MemorySize) cacheObject).setMemorySize(objectSize);
        }
        else
        {
        	LOGGER.warn(cacheObject + " don't implement interface MemorySize!");
        }
        return objectSize;
    }
   
}

