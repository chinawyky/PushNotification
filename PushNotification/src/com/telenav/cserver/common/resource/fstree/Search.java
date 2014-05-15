/*
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.fstree;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.common.resource.fstree.comparator.ComparatorUtil;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @author huishen
 * @since 2013-02-25
 */
public class Search
{
	private Search()
	{}
	
    public static String search(FSTreeNode father, List<LoadOrder> orders, int index, UserProfile profile,
            TnContext tnContext)
    {

        String wholePath;
        String tempPath;
        FSTreeNode equalChild;

        LoadOrder currentLoadOrder = orders.get(index);

        // The last of the LoadOrder list which is the FileNameLoadOrder.
        if (index == orders.size() - 1)
        {
            String fileName = currentLoadOrder.getAttributeValue(profile, tnContext);
            equalChild = ComparatorUtil.getEqualChild(currentLoadOrder, father, fileName, profile);
            if (equalChild != null)
            {
                return father.getName() + ResourceFactory.SEPARATOR + fileName;
            }
            return StringUtils.EMPTY;
        }

        // For certain Loader
        List<String> valueList = currentLoadOrder.getAttributeValueList(profile, tnContext);
        if (valueList.isEmpty())// handle the ptn loadorder
        {
            tempPath = search(father, orders, index + 1, profile, tnContext);
            if (StringUtils.isNotBlank(tempPath))
            {
                wholePath = tempPath;// The prefix 'father.getName()' has been appended within the previous search
                                     // method.
                return wholePath;
            }
        }
        for (String tempValue : valueList)
        {
            equalChild = ComparatorUtil.getEqualChild(currentLoadOrder, father, tempValue, profile);

            // The category name fits with current LoadOrder.
            if (equalChild != null && equalChild.hasChildren())
            {
                tempPath = search(equalChild, orders, index + 1, profile, tnContext);
                if (StringUtils.isNotBlank(tempPath))
                {
                    wholePath = father.getName() + ResourceFactory.SEPARATOR + tempPath;
                    return wholePath;
                }
            }
        }

        return StringUtils.EMPTY;
    }
}
