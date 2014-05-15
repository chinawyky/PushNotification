/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * ProductLoadOrder.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-6
 *
 */
public class ProductLoadOrder  extends LoadOrder 
{
	public ProductLoadOrder()
	{
		setType("product");
	}
	
	/**
	 * get the attribute value from UserProfile
	 * 
	 * @param profile
	 * @return
	 */
	public String getAttributeValue(UserProfile profile, TnContext tnContext)
	{
		return profile.getProduct();
	}
    
    public List<String> getAttributeValueList(UserProfile profile, TnContext tnContext)
    {
        List<String> list = new ArrayList<String>();
        
        String product = profile.getProduct();
        list.add(product);
        
        if(product.lastIndexOf("_") != -1)
        {
            product=product.substring(0,product.lastIndexOf("_"));
            list.add(product);
        }
        return list;
    }
	
}