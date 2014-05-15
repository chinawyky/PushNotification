/*
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.fstree;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author huishen
 * @since 2013-02-22
 *
 */
public class FSTreeNode
{
    private String name;
    private List<FSTreeNode> children = new ArrayList<FSTreeNode>();
    /**This value is only used for testing.*/
    private boolean bTested;
    
    public boolean notTested()
    {
        return !isTested();
    }
    public boolean isTested()
    {
        return bTested;
    }

    public void setTested(boolean isTested)
    {
        this.bTested = isTested;
    }

    public FSTreeNode(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public List<FSTreeNode> getChildren()
    {
        return children;
    }
    public void setChildren(List<FSTreeNode> children)
    {
        this.children = children;
    }
    public void addChild(FSTreeNode child)
    {
        if (child != null)
        {
            children.add(child);
        }
    }
    public boolean hasChildren()
    {
        return children != null && children.size() > 0;
    }
    
    public boolean noChildren()
    {
        return !hasChildren();
    }
    public FSTreeNode containChild(String name)
    {
        FSTreeNode result = null;
        if (hasChildren() && StringUtils.isNotBlank(name))
        {
            for (FSTreeNode child : children)
            {
                if (StringUtils.equals(name, child.getName()))
                {
                    result = child;
                    break;
                }
            }
        }
        return result;
    }
}
