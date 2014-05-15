/**
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.hanlder;

import java.util.Iterator;

import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.common.resource.device.DeviceProperties;
import com.telenav.cserver.common.resource.device.DevicePropertiesHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * DeviceResourceHandler
 * @author kwwang
 *
 */
public class DeviceResourceHandler implements ResourceHandler {

	public static final String RESOURCE_TYPE="device";
	
	private final DevicePropertiesHolder deviceHolder=(DevicePropertiesHolder)ResourceHolderManager.getResourceHolder(HolderType.DEVICE_TYPE);
	
	@Override
	public TxNode handle(UserProfile userProfile, TnContext tc) {
		
		TxNode deviceNode = null;
		if(deviceHolder != null)
		{
			DeviceProperties dp = deviceHolder.getDeviceProperties(userProfile, tc);
			if(dp != null)
			{
				deviceNode = new TxNode();
				deviceNode.addMsg(RESOURCE_TYPE);
				TxNode devicePropertiesNode = new TxNode();
				deviceNode.addChild(devicePropertiesNode);
				if(dp.getAllDeviceAtrributes() != null)
				{
					Iterator keyIterator = dp.getAllDeviceAtrributes().keySet().iterator();
					while(keyIterator.hasNext())
					{
						String key = (String)keyIterator.next();
						devicePropertiesNode.addMsg(key+"="+dp.getString(key));
					}
				}
			}
		}
		return deviceNode;
	}

}
