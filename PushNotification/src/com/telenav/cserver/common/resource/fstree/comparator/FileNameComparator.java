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
final class FileNameComparator implements IComparator
{
    
    /* (non-Javadoc)
     * @see com.telenav.cserver.common.resource.datatype.IComparator#isEqual(java.lang.String, java.lang.String)
     */
    @Override
    public FSTreeNode getEqualChild(FSTreeNode target, String key)
    {
        FSTreeNode result = null;
        String fileName;
        
        List<FSTreeNode> children = target.getChildren();
        for (FSTreeNode child : children)
        {
            fileName = this.removeFileSuffix(child.getName());
          //The category name fits with current LoadOrder.
            if (StringUtils.equals(fileName, key))
            {
                child.setTested(true);
                result = child;
                break;
            }
        }
        return result;
    }
    
    private String removeFileSuffix(String fileName)
    {
        String result = fileName;
        int index = fileName.lastIndexOf('.');
        if (index != -1)
        {
            result = fileName.substring(0, index);
        }
        return result;
    }
}
