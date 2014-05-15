/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.util.CSStringUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * VersionOrder.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-6
 *
 */
public class VersionLoadOrder extends LoadOrder 
{
	public VersionLoadOrder()
	{
		setType("version");
	}
	
	/**
	 * get the attribute value from UserProfile
	 * 
	 * @param profile
	 * @return
	 */
	public String getAttributeValue(UserProfile profile, TnContext tnContext)
	{
		if(CSStringUtil.isNotNil(profile.getVersion()))
		{
		    String version = profile.getVersion();
			return version.replaceAll("\\.", "_");
		}
		return "";
	}
	
    public List<String> getAttributeValueList(UserProfile profile, TnContext tnContext)
    {
        List<String> list = new ArrayList<String>();
        String version = getAttributeValue(profile, tnContext);
        list.add(version);
        
        String majorVersion = getMajorVersion(profile);
        if (!majorVersion.equals(version))
        {
            list.add(majorVersion);
        }
        return list;
    }
	
	/**
	 * get version value from UserProfile which ignore the subversion from client.
	 * The different with  getAttributeValue's method is that the version number need format from "_" to ".". 
	 * 
	 * @param profile
	 * @param tnContext
	 * @return
	 */
	public String getAttributeValueIgnoreSubversion(String version)
	{
		if(CSStringUtil.isNotNil(version))
		{
			return version.replaceAll("_", "\\.");
		}
		return "";
	}
	
    public String getMajorVersion(UserProfile profile)
    {
        String version = profile.getVersion();
        if (StringUtils.isNotBlank(version))
        {
            version = version.replaceAll("\\.", "_");
            String[] nums = version.split("_");
            if (nums.length > 2)
            {
                version = nums[0] + "_" + nums[1] + "_0";
            }
        }
        return version;
    }
}
