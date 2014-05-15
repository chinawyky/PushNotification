/*
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.fstree.comparator;

import java.util.ArrayList;
import java.util.List;
import com.telenav.cserver.common.resource.fstree.FSTreeNode;
import com.telenav.cserver.common.resource.orders.ResolutionLoadOrder;

/**
 * @author huishen
 * @since 2013-02-25
 *
 */
class ResolutionComparator implements IComparator
{
    private static final String RESOLUTION_PATTERN = "\\d+x\\d+(_\\d+x\\d+)*+";
    private static final String NULLX="nullx";

    /* (non-Javadoc)
     * @see com.telenav.cserver.common.resource.datatype.IComparator#isEqual(java.lang.String, java.lang.String)
     */
    @Override
    public FSTreeNode getEqualChild(FSTreeNode target, String key)
    {       
        String keyFiltered = filterKey(key); 
        
        FSTreeNode result = null;
        
        List<FSTreeNode> children = target.getChildren();
        String name;
        List<String> names = new ArrayList<String>();
        
        for (FSTreeNode child : children)
        {
            name = child.getName();
            if (name.matches(RESOLUTION_PATTERN))
            {
                names.add(name);
            }
            if (name.equals(keyFiltered))
            {
                result = child;
                break;
            }
        }
        
        if (result == null)
        {
            //Get the resemblance resolution
            String resembleName = ResolutionLoadOrder.getResemblance(names, keyFiltered);
            for (FSTreeNode child : children)
            {
                if (child.getName().equals(resembleName))
                {
                    child.setTested(true);
                    result = child;
                    break;
                }
            }
        }
        
        return result;
    }
    
    private String filterKey(String key)
    {
    	//To fix the incorrct screenWidth pass by client as the incorrect value will cause exception when looking for resemble resolution.
    	String keyFiltered = key;
    	if (key.startsWith(NULLX))
        {
    		keyFiltered = key.replace(NULLX, "");
        }
        else if (key.charAt(0) == 'x')
        {
        	keyFiltered = key.substring(1);
        }
    	return keyFiltered;
    }
}
