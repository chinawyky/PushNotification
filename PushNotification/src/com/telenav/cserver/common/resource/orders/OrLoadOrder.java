/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * the order for loading
 * 
 * @author jzhu
 * @version 2.0 2010-11-24 09:46
 */
public class OrLoadOrder extends LoadOrder
{	
    private List<LoadOrder> loadOrderList = new ArrayList<LoadOrder>();
    
    public void setLoadOrderList(List<LoadOrder> loadOrderList)
    {
        this.loadOrderList = loadOrderList;
    }

    /**
     * get the first not NUll or "" attribute value from loadOrderList
     * @param profile
     * @return
     */
    public String getAttributeValue(UserProfile profile, TnContext tnContext)
    {
        for(LoadOrder order:loadOrderList)
        {
            String attrValue = order.getAttributeValue(profile, tnContext);
            if (StringUtils.isNotBlank(attrValue))
            {
            	return attrValue;
            }
        }
        return "";
    }
    
    
    public List<String> getAttributeValueList(UserProfile profile, TnContext tnContext)
    {
        List<String> list = new ArrayList<String>();
        
        for(LoadOrder order:loadOrderList)
        {
            list.addAll(order.getAttributeValueList(profile, tnContext));
        }
        return list;
    }
}
