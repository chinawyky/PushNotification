/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Category;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.cserver.common.resource.constant.ResourceConst;

/**
 * DeviceLoadOrder.java
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2010-11-24
 *
 */
public class ResolutionLoadOrder  extends LoadOrder 
{
	public ResolutionLoadOrder()
	{
		setType("resolution");
	}   
   
    private static final String RESOLUTION_CONNECTOR = "x";
    private static Category logger = Category.getInstance(ResolutionLoadOrder.class);
    
	
	/**
	 * get the attribute value from UserProfile
	 * 
	 * @param profile
	 * @return
	 */
	public String getAttributeValue(UserProfile profile, TnContext tnContext)
	{
	    return profile.getResolution();
	}
    
    public List<String> getAttributeValueList(UserProfile profile, TnContext tnContext)
    {
        List<String> list = new ArrayList<String>();
        String resolution = getAttributeValue(profile, tnContext);
        if (StringUtils.isNotBlank(resolution))
        {
            list.add(resolution);
        }
        list.add("default");
        return list;
    }
    
    //get the resemblance one
    //the resolution is HxL and the closest one is H'xL'
    //then we need satisify the following condition
    //H'<H and L'<L and H'xL' is the largest one in the list
    //e.g. if we have a list <320x480_480x320, 800x600_600x300>
    //and the resolution is 400x480_480x400
    //will return the closest one: 320x480_480x320
    public static String getResemblance(List<String> list, String resolution)
    {
        if (list == null || list.size() == 0)
        {
            return null;
        }
        
        if (list.size() == 1)
        {
            return list.get(0);
        }
        
        String result = list.get(0);
        try
        {
            String[] actuality = resolution.split(ResourceConst.UNDERSCORE)[0].split(RESOLUTION_CONNECTOR);
            double width = Double.parseDouble(actuality[0]);
            double height = Double.parseDouble(actuality[1]);

            for(int i=1; i<list.size(); i++)
            {
                String str = list.get(i);
                String[] resemblance = str.split(ResourceConst.UNDERSCORE)[0].split(RESOLUTION_CONNECTOR);
                String[] last = result.split(ResourceConst.UNDERSCORE)[0].split(RESOLUTION_CONNECTOR);
                
                double currW = Double.parseDouble(resemblance[0]);
                double currH = Double.parseDouble(resemblance[1]);
                double lastW = Double.parseDouble(last[0]);
                double lastH = Double.parseDouble(last[1]);
                
                if (lastW>width || lastH>height)
                {
                    if (currW*currH<lastW*lastH )
                    {
                        result = str;
                        continue;
                    }
                }
                else if (currW<=width && currH<=height && currW*currH>lastW*lastH)
                {
                    result = str;
                    continue;
                }
            }
        }
        catch(NumberFormatException e)
        {
            logger.warn("getResemblance", e);
        }
        
        
        return result;
        
    }

}
