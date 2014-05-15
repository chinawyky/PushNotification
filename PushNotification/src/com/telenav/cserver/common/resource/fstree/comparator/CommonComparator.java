/*
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.fstree.comparator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.telenav.cserver.common.resource.fstree.FSTreeNode;

/**
 * @author huishen
 * @since 2013-02-25
 *
 */
final class CommonComparator implements IComparator
{
    
    /* (non-Javadoc)
     * @see com.telenav.cserver.common.resource.datatype.IComparator#isEqual(java.lang.String, java.lang.String)
     */
    @Override
    public FSTreeNode getEqualChild(FSTreeNode target, String key)
    {
        FSTreeNode result = null;
        
        List<FSTreeNode> children = target.getChildren();
        for (FSTreeNode child : children)
        {
          //The category name fits with current LoadOrder.
            if (StringUtils.equals(child.getName(), key))
            {
                child.setTested(true);
                result = child;
                break;
            }
        }
        return result;
    }

}
