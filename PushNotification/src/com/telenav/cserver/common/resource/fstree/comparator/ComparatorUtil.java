/*
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.fstree.comparator;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.common.resource.fstree.FSTreeNode;
import com.telenav.cserver.framework.UserProfile;

/**
 * @author huishen
 * @since 2013-02-25
 *
 */
public final class ComparatorUtil
{
    private static final String RESOLUTION_TYPE = "device_resolution";
    private static final String FILE_TYPE = "file";
//    private static final String RESOLUTION_PATTERN = "\\d+x\\d+(_\\d+x\\d+)*+";
    private static IComparator commonComparator = new CommonComparator();
    private static IComparator resolutionComparator = new ResolutionComparator();
    private static IComparator fileComparator = new FileNameComparator();
    
    private ComparatorUtil()
    {}
    
    public static IComparator getComparator(String type, String value, UserProfile profile)
    {
        IComparator comparator = null;
        //only resolution (480x640_640x480) need the resolution comparator
        if (RESOLUTION_TYPE.equals(type) && value.equals(profile.getResolution()))
        {
            comparator = resolutionComparator;
        }
        else if (FILE_TYPE.equals(type))
        {
            comparator = fileComparator;
        }
        else 
        {
            comparator = commonComparator;
        }
        return comparator;
    }
    
    public static FSTreeNode getEqualChild(LoadOrder order, FSTreeNode target, String value, UserProfile profile)
    {
        IComparator comparator = getComparator(order.getType(), value, profile);
        return comparator.getEqualChild(target, value);
    }
}
