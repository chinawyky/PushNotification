/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.telenav.cserver.common.resource.constant.ResourceConst;
import com.telenav.j2me.datatypes.NewBizCategory;

/**
 * HtmlDisplayFormat.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-10-28
 *
 */
class HTMLDisplayFormat implements DisplayFormat
{
    private final static String TABLE_START = "<table border=1 align=center>";

    private final static String TABLE_END = "</table>";

    private final static String LINE_START = "<tr>";

    private final static String LINE_END = "</tr>";

    private final static String COLUMN_START = "<td align=right>";

    private final static String COLUMN_END = "</td>";
    
    private final static String HOLDER_NAME_THIS = "holderName";
    
    private final static String KEY = "key";
    
    private final static String VIEW = "view";
    
    private final static String CACHED_CLIENTS = "cachedClients";
    
    private final static String IS_NOT_FOUND = " is not found!";
    
    private final ResourceCacheManagement cacheManagement;
    
    public HTMLDisplayFormat(ResourceCacheManagement cacheManagement)
    {
        this.cacheManagement = cacheManagement;
    }

    @Override
    public String statistic()
    {
    	List<Object> list = new ArrayList<Object>();
    	StringBuffer sb = getStringBuffer(list);
    	
        addLine(sb, list);
        Iterator<ResourceHolder> holders = cacheManagement.getHolderSet().iterator();
        while (holders.hasNext())
        {
            ResourceHolder holder = holders.next();
            list.clear();
            list.add(holder.getName());
            list.add(cacheManagement.getSimpleClassName(holder.getClass().getName()));
            list.add(cacheManagement.getCountOfCacheObject(holder));
            list.add(cacheManagement.getSizeOfObject().humanReadable(cacheManagement.getCacheSize(holder)));
            list.add(addDetailsButton(holder));
            // list.add(addEvictButton(holder));
            addLine(sb, list);
        }

        sb.append(TABLE_END);
        return sb.toString();
    }

    public String details()
    {
    	List<Object> list = new ArrayList<Object>();
    	StringBuffer sb = getStringBuffer(list);
        
        list.add(HOLDER_DETAILS);
        addLine(sb, list);
        Iterator<ResourceHolder> holders = cacheManagement.getHolderSet().iterator();
        while (holders.hasNext())
        {
            addHolderInfo(sb, holders.next());
        }
        
        List<IMonitor> monitors = cacheManagement.getMonitors();
        for(IMonitor monitor : monitors){
            List<MonitorObject> monitorObjects = monitor.getMonitorObjects();
            for(MonitorObject monitorObject : monitorObjects){
                addMonitorObject(sb, monitorObject);
            }

        }
        
        sb.append(TABLE_END);
        return sb.toString();
    }
    
    public StringBuffer getStringBuffer(List<Object> list)
    {
    	StringBuffer sb = new StringBuffer();
        sb.append(TABLE_START);
        addStatisticString(sb);

        list.clear();
        addLine(sb, list);

        list.clear();
        list.add(HOLDER);
        addLine(sb, list);

        list.clear();
        list.add(HOLDER_NAME);
        list.add(HOLDER_TYPE);
        list.add(COUNT_OF_CACHE_OBJECT);
        list.add(MEMORY_OF_HOLDER);
        
        return sb;
    }
    

    @Override
    public String details(String holderName)
    {
        StringBuffer sb = new StringBuffer();
        int found = cacheManagement.getCounterOfHolder(holderName);
        if (found == 1)
        {
            sb.append(TABLE_START);
            List<Object> list = new ArrayList<Object>();
            list.add(HOLDER_NAME);
            list.add(HOLDER_TYPE);
            list.add(COUNT_OF_CACHE_OBJECT);
            list.add(MEMORY_OF_HOLDER);
            list.add(HOLDER_DETAILS);
            addLine(sb, list);
            ResourceHolder holder = cacheManagement.getHolder(holderName);
            addHolderInfo(sb, holder);
            sb.append(TABLE_END);
        }
        else if (found == 0)
        {
            sb.append(NO_HOLDER).append(holderName);
        }
        // more than one holder
        else
        {
            sb.append(MORE_THAN_ONE_HOLDER).append(holderName);
        }
        return sb.toString();
    }
    
    @Override
    public String contents(String name, String key)
    {
        String result;
        if( cacheManagement.getHolder(name) == null )
        {
        	result = contentsMonitorobject(name,key);
        }else
        {
        	result = contentsHolder(name,key);
        }
        return result;
    }
    
    @Override
    public String viewCachedClient(String holderName, String key) {
    	StringBuilder builder = new StringBuilder();
    	AbstractResourceHolder holder = (AbstractResourceHolder)cacheManagement.getHolder(holderName);
    	if(holder!=null&&StringUtils.isNotBlank(key))
    	{
    		Iterator<Entry<String,String>> entryIt=holder.getUserKeyMapping().entrySet().iterator();
    		while(entryIt.hasNext())
    		{
    			Entry entry=entryIt.next();
    			if(key.equals(entry.getValue()))
    			{
    				builder.append(entry.getKey()).append("\n");
    			}
    		}
    	}
        return builder.toString();
    }
    
    private String contentsMonitorobject(String name, String key)
    {
        List<IMonitor> monitors = cacheManagement.getMonitors();
        for(IMonitor monitor : monitors){
            List<MonitorObject> monitorObjects = monitor.getMonitorObjects();
            MonitorObject monitorObject = getMonitorObject(monitorObjects,name);
            if (monitorObject == null)
            {
                return "monitor object[" + name + "] doesn't exist!";
            }

            Map<?, ?> monitorData = monitorObject.getData();

            Set keys = monitorData.keySet();
            for (Object theKey : keys)
            {
                if (theKey != null && theKey.toString().equals(key))
                {
                    Object obj = monitorData.get(theKey);
                    if (obj == null)
                    {
                    	return "null";
                    }
                    else
                    {
                    	return obj.toString();
                    }
                        
                }
            }
        }
        return new StringBuffer("can't find ").append(key).append(" = [").append(key).append("]!").toString();
    }
    
    private MonitorObject getMonitorObject(List<MonitorObject> monitorObjects, String name){
        for(MonitorObject monitorObject : monitorObjects){
            if( name != null && name.equals(monitorObject.getName())){
                return monitorObject;
            }
        }
        return null;
    }
    
    public boolean isNumber(String params){
    	
    	boolean bRtn = true;
        if( params == null)
        {
        	bRtn =  false;
        }
        for(int i=0; i<params.length(); i = i + 1)
        {
            if( !Character.isDigit(params.charAt(i)) )
            {
            	bRtn =  false;
            }
        }
        return bRtn;
        
    }

    private String contentsHolder(String holderName, String key)
    {
        String str = holderName + "[" + key + "]" + IS_NOT_FOUND;
        int found = cacheManagement.getCounterOfHolder(holderName);
        if (found == 1)
        {
            ResourceHolder holder = cacheManagement.getHolder(holderName);

            Iterator cacheObjects = ((AbstractResourceHolder) holder).getMap().entrySet().iterator();
            while (cacheObjects.hasNext())
            {
                Entry pair = (Entry) cacheObjects.next();
                if (pair.getKey().equals(key))
                {
                    Object obj = pair.getValue();
                    if (obj instanceof ResourceContent)
                    {
                        Map map = ((ResourceContent) obj).getProps();
                        if (map != null && map.size() > 0)
                        {
                            Object contents = map.entrySet().iterator().next();
                            if (contents != null)
                            {
                                return contents.toString();
                            }
                        }
                        else
                        {
                            if (((ResourceContent) obj).getObject() != null)
                            {
                                Object contentObject = ((ResourceContent) obj).getObject();
                                if( contentObject instanceof NewBizCategory ){
                                    return toString((NewBizCategory)contentObject);
                                }
                                return contentObject.toString();
                            }
                        }
                    }
                }
            }

        }
        else if (found == 0)
        {
            str = holderName + IS_NOT_FOUND;
        }
        // more than one holder
        else
        {
            str = "more than one " + holderName + IS_NOT_FOUND;
        }
        return str;
    }

    private void addLine(StringBuffer sb, List<Object> objects)
    {
        sb.append(LINE_START);
        for (Object obj : objects)
        {
            sb.append(COLUMN_START);
            sb.append(obj);
            sb.append(COLUMN_END);
        }
        sb.append(LINE_END);
    }

    private void addCacheDetailLine(StringBuffer sb, List<Object> objects)
    {
        int length = objects.size();
        sb.append(LINE_START);
        for (int i = 0; i < length - 1; i++)
        {
            sb.append(COLUMN_START);
            sb.append(objects.get(i));
            sb.append(COLUMN_END);
        }
        /*
         * display cache objects
         */
        sb.append(COLUMN_START);
        sb.append("<table>");
        List<String> list = (List<String>) objects.get(length - 1);
        for (String str : list)
        {
            sb.append(LINE_START);
            sb.append(COLUMN_START);
            sb.append(str);
            sb.append(COLUMN_END);
            sb.append(LINE_END);
        }
        sb.append(TABLE_END);
        sb.append(COLUMN_END);

        sb.append(LINE_END);
    }
    
    
    protected String getContentsAction()
    {
        return "invokeBean?class=com.telenav.cserver.common.resource.ResourceCacheManagement;operation=contents";
    }

    private void addHolderInfo(StringBuffer sb, ResourceHolder holder)
    {
        List<Object> list = new ArrayList<Object>();
        List<String> cacheObjectsForDisplay = new ArrayList<String>();

        list.add(holder.getName());
        list.add(cacheManagement.getSimpleClassName(holder.getClass().getName()));
        list.add(cacheManagement.getCountOfCacheObject(holder));
        list.add(cacheManagement.getSizeOfObject().humanReadable(cacheManagement.getCacheSize(holder)));
        String action = getContentsAction();
        Iterator cacheObjects = ((AbstractResourceHolder) holder).getMap().entrySet().iterator();
        while (cacheObjects.hasNext())
        {
            Entry pair = (Entry) cacheObjects.next();
            Object cacheObject = pair.getValue();
            long sizeOfCacheObject = cacheManagement.getSizeOfCacheObject(cacheObject);
            Map<String, String> args = new LinkedHashMap<String, String>();
            args.put(HOLDER_NAME_THIS, holder.getName());
            args.put(KEY, pair.getKey().toString());
            
            String viewHtml=getForm(action, VIEW, args).toString();
            args.put("operation", CACHED_CLIENTS);
            String cachedClientsHtml=getForm(action, CACHED_CLIENTS, args).toString();
            
            cacheObjectsForDisplay.add(pair.getKey() + ResourceConst.EQUAL + cacheManagement.getSizeOfObject().humanReadable(sizeOfCacheObject)
                +"  ("+getCachedClientNumber(holder,String.valueOf(pair.getKey()))+")  "+ COLUMN_START + viewHtml+cachedClientsHtml + COLUMN_END);
        }

        list.add(cacheObjectsForDisplay);
        addCacheDetailLine(sb, list);
    }
    
    private long getCachedClientNumber(ResourceHolder holder,String key)
    {
    	long number = 0;
    	Map<String,String> userKeyMap=((AbstractResourceHolder)holder).getUserKeyMapping();
    	Iterator<Entry<String,String>> entryIt=userKeyMap.entrySet().iterator();
    	while(entryIt.hasNext())
    	{
    		Entry<String,String> entry=entryIt.next();
    		if(StringUtils.isNotBlank(key)&&key.equals(entry.getValue()))
    		{
    			number+=1;
    		}
    			
    	}
    	return number;
    }
    
    private void addMonitorObject(StringBuffer sb, MonitorObject monitorObject)
    {
        List<Object> list = new ArrayList<Object>();
        List<String> cacheObjectsForDisplay = new ArrayList<String>();

        list.add(monitorObject.getName());
        list.add("Not Holder");
        list.add(monitorObject.getData().size());
        list.add(cacheManagement.getSizeOfObject().humanReadable(cacheManagement.getSizeOfCacheObject(monitorObject.getData())));
        String action = getContentsAction();
        Iterator cacheObjects = monitorObject.getData().entrySet().iterator();
        while (cacheObjects.hasNext())
        {
            Entry pair = (Entry) cacheObjects.next();
            Object cacheObject = pair.getValue();
            long sizeOfCacheObject = cacheManagement.getSizeOfCacheObject(cacheObject);
            Map<String, String> args = new LinkedHashMap<String, String>();
            args.put(HOLDER_NAME_THIS, monitorObject.getName());
            args.put(KEY, pair.getKey().toString());
            args.put("isHolder", "no");
            cacheObjectsForDisplay.add(pair.getKey() + ResourceConst.EQUAL + cacheManagement.getSizeOfObject().humanReadable(sizeOfCacheObject)
                +"  "+ COLUMN_START + getForm(action, VIEW, args) + COLUMN_END);
        }

        list.add(cacheObjectsForDisplay);
        addCacheDetailLine(sb, list);
    }
    
    
    private StringBuffer addStatisticString(StringBuffer sb)
    {
        List<Object> list = new ArrayList<Object>();
        list.clear();
        list.add(STATISTIC);
        addLine(sb, list);

        list.clear();
        list.add(COUNT_OF_HOLDER_TYPE);
        list.add(cacheManagement.getCountOfHolderType());
        addLine(sb, list);

        list.clear();
        list.add(COUNT_OF_HOLDER);
        list.add(cacheManagement.getCounterOfHolder());
        addLine(sb, list);

        list.clear();
        list.add(COUNT_OF_CACHE_OBJECT);
        list.add(cacheManagement.getCountOfCacheObject());
        addLine(sb, list);

        list.clear();
        list.add(TOTAL_MEMORY_OF_CACHE);
        list.add(cacheManagement.getSizeOfObject().humanReadable(cacheManagement.getTotalCacheSize()));
        addLine(sb, list);


        return sb;
    }


    private StringBuffer addDetailsButton(ResourceHolder holder)
    {
        StringBuffer sb = new StringBuffer();
        String action = "invokeBean?class=com.telenav.cserver.common.resource.ResourceCacheManagement;operation=details";
        Map<String,String> args = new HashMap<String, String>();
        args.put(HOLDER_NAME_THIS,holder.getName());
        sb.append(getForm(action, "details", args));
        return sb;
    }

    
    protected String getInputHiddenItem(String key, String value)
    {
        return "<input type=\"hidden\" name=\"" + key + "#java.lang.String\" value=\"" + value + "\">" + "</input>";
    }
    protected String getHttpMethod()
    {
        return "POST";
    }
    protected StringBuffer getForm(String action, String tips, Map<String,String> args)
    {
        StringBuffer sb = new StringBuffer(ResourceConst.BUFFER_SIZE);
        sb.append("<table>");
        sb.append("<form name=\"invokeBean\" action=\"" + action + "\" method=\""+getHttpMethod()+"\"/>");
        sb.append(LINE_START);
        sb.append(COLUMN_START);
        Iterator<Entry<String, String>> it = args.entrySet().iterator();
        while(it.hasNext())
        {
            Entry<String, String> entry = it.next();
            sb.append(getInputHiddenItem(entry.getKey(),entry.getValue()));
        }
        sb.append("<input  type=\"submit\" name=\"op_submit\" value=\"" + tips + "\">" + "</input>");
        sb.append(COLUMN_END);
        sb.append(LINE_END);
        sb.append("</form>");
        sb.append(TABLE_END);
        return sb;
    }
    
    private static String toString(NewBizCategory category)
    {
        return toString(category, new HashMap<NewBizCategory,String>(),0);
    }
    private static final String EMPTY_INCIDENT = "&nbsp;&nbsp;&nbsp;";
    private static final String NEW_LINE = "</br>";
    private static String toString(NewBizCategory category, Map<NewBizCategory,String> visited, int level)
    {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<level; i++)
        {
            sb.append(EMPTY_INCIDENT);
        }
        if (visited.containsKey(category))
        {
            return sb.append(visited.get(category)).toString();
        }
        visited.put(category, "<myself>");
        sb.append(category.name);
        if (category.children != null && category.children.size() != 0)
        {
            level++;
            Iterator it = category.children.iterator();
            while (it.hasNext())
            {
                sb.append(NEW_LINE).append(toString((NewBizCategory) it.next(),visited,level));
            }
        }
        visited.put(category,sb.toString());
        return sb.toString();
    }
    

}