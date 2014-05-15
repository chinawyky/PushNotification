/**
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.hanlder;

import java.util.Arrays;
import java.util.Map;

import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.holder.impl.DsmRuleHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * DSMResourceHandler
 * @author kwwang
 *
 */
public class DSMResourceHandler implements ResourceHandler {
	
	public static final String RESOURCE_TYPE="dsm";
	private final DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder(RESOURCE_TYPE);
	
	@Override
	public TxNode handle(UserProfile userProfile, TnContext tc) {
		TxNode dsmNode = null;
        if (dsmHolder != null)
        {
            @SuppressWarnings("unchecked")
			Map<String, String> map = dsmHolder.getDsmResponses(userProfile, null);
            
            if (map != null)
            {
            	dsmNode = new TxNode();
            	dsmNode.addMsg(RESOURCE_TYPE);
            	TxNode resourceNode = new TxNode(); 
	            Object[] keys = map.keySet().toArray();
	            Arrays.sort(keys);
	            StringBuffer sb = new StringBuffer();
	            for(int j = 0; j < keys.length; j ++)
	            {
	            	sb.append("(").append(keys[j]).append(":").append(map.get(keys[j])).append(")");
	            }
	            resourceNode.addMsg(sb.toString());
	            dsmNode.addChild(resourceNode);
            }
  
        }
		return dsmNode;
	}

}
