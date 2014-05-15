/*
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.fstree.comparator;

import com.telenav.cserver.common.resource.fstree.FSTreeNode;

/**
 * @author huishen
 * @since 2013-02-25
 *
 */
public interface IComparator
{
    /**
     * Compare if the target is equivalent with key under some rule.
     * @param target
     * @param key
     * @return node which is equivalent with key, otherwise return null.
     */
    FSTreeNode getEqualChild(FSTreeNode target, String key);
}
