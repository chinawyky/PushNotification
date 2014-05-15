/**
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.hanlder;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.resource.common.ServiceLocatorHolder;
import com.telenav.cserver.resource.datatypes.BrowserServiceItem;
import com.telenav.cserver.resource.datatypes.ServiceItem;
import com.telenav.cserver.resource.datatypes.ServiceMapping;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * ServiceLocatorResourceHandler
 * @author kwwang
 *
 */
public class ServiceLocatorResourceHandler implements ResourceHandler {

	public static final String RESOURCE_TYPE = "servicelocator";
	
	public static final String LINE_SEPARATOR = "\n";
	
	public static final String KEY_VALUE_CONNECTOR = "=";
	
	public static final String BROSWER_SERVICE_ITEM_TYPE = "BrowserServiceItem";
	
	public static final String COMMON_SERVICE_ITEM_TYPE = "ServiceItem";
	
	private final ServiceLocatorHolder serviceLocatorHolder = (ServiceLocatorHolder)ResourceHolderManager.getResourceHolder(HolderType.SERVICE_LOCATOR_TYPE);
	
	@Override
	public TxNode handle(UserProfile userProfile, TnContext tc) {
		TxNode serviceLocatorNode = null;
		if(serviceLocatorHolder != null)
		{
			ResourceContent rs=serviceLocatorHolder.getResourceContent(userProfile, tc);
			ServiceMapping serviceMapping = (ServiceMapping)rs.getObject();
			if(serviceMapping != null)
			{
				serviceLocatorNode=handleServiceMapping(serviceMapping);
			}
		}
		return serviceLocatorNode;
	}
	
	
	protected TxNode handleServiceMapping(ServiceMapping serviceMapping)
	{
		TxNode serviceLocatorNode = new TxNode();
		serviceLocatorNode.addMsg(RESOURCE_TYPE);
		serviceLocatorNode.addMsg(serviceMapping.getVersion());
		serviceLocatorNode.addMsg(serviceMapping.getActionVersion());
		
		List<ServiceItem> serviceMappingItems = serviceMapping.getServiceMapping();
		Collections.sort(serviceMappingItems,new ServiceLocatorItemComparator());
		for(ServiceItem serviceItem:serviceMappingItems)
		{
			if(serviceItem instanceof BrowserServiceItem)
			{
				BrowserServiceItem browserServiceItem = (BrowserServiceItem) serviceItem;
				TxNode browserServiceItemNode = new TxNode();
				browserServiceItemNode.addMsg(BROSWER_SERVICE_ITEM_TYPE);
				browserServiceItemNode.addMsg(browserServiceItem.getType());
				browserServiceItemNode.addMsg(browserServiceItem.getServiceDomainName());
				browserServiceItemNode.addMsg(browserServiceItem.getBaseUrl());
				browserServiceItemNode.addMsg(combineServiceItemActions(browserServiceItem.getActions()));
				browserServiceItemNode.addMsg(combineServiceItemUrlMap(browserServiceItem.getUrlMap()));
				serviceLocatorNode.addChild(browserServiceItemNode);
			}
			else
			{
				TxNode serviceItemNode = new TxNode();
				serviceItemNode.addMsg(COMMON_SERVICE_ITEM_TYPE);
				serviceItemNode.addMsg(serviceItem.getType());
				serviceItemNode.addMsg(serviceItem.getServiceDomainName());
				serviceItemNode.addMsg(combineServiceItemActions(serviceItem.getActions()));
				serviceItemNode.addMsg(combineServiceItemUrlMap(serviceItem.getUrlMap()));
				serviceLocatorNode.addChild(serviceItemNode);
			}
		}
		return serviceLocatorNode;
	}
	
	protected String combineServiceItemUrlMap(Map<String,String> urlMap)
	{
		StringBuilder builder = new StringBuilder();
		TreeMap<String,String> orderedUrlMap = new TreeMap<String,String>(urlMap);
		Iterator<String> keyIterator = orderedUrlMap.keySet().iterator();
		while(keyIterator.hasNext())
		{
			String key = keyIterator.next();
			builder.append(key).append(KEY_VALUE_CONNECTOR).append(orderedUrlMap.get(key)).append(LINE_SEPARATOR);
		}
		return builder.toString();
	}
	
	protected String combineServiceItemActions(List<String> serviceLocatorActions)
	{
		StringBuilder builder = new StringBuilder();
		Collections.sort(serviceLocatorActions);
		for(String action:serviceLocatorActions)
		{
			builder.append(action).append(LINE_SEPARATOR);
		}
		return builder.toString();
	}
	
	
	class ServiceLocatorItemComparator implements Comparator<ServiceItem> {
		@Override
		public int compare(ServiceItem o1, ServiceItem o2) {
			int result=0;
			if(o1 == null && o2 != null)
			{
				result = -1;
			}
			else if (o1 != null && o2 == null)
			{
				result = 1;
			}
			else if (o1 != null && o2 != null)
			{
				result = serviceItemTypeCompare(o1.getType(),o2.getType());
			}
			return result;
		}
		
		protected int serviceItemTypeCompare(String serviceItemType1, String serviceItemType2)
		{
			int result = 0;
			if(StringUtils.isBlank(serviceItemType1) && StringUtils.isNotBlank(serviceItemType2))
			{
				result = -1;
			}
			else if(StringUtils.isBlank(serviceItemType2) && StringUtils.isNotBlank(serviceItemType1))
			{
				result = 1;
			}
			else if(StringUtils.isNotBlank(serviceItemType1) && StringUtils.isNotBlank(serviceItemType2))
			{
				result = serviceItemType1.compareTo(serviceItemType2);
			}
			return result;
		}
	}

}
