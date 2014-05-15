/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.trump;

import java.util.ArrayList;
import java.util.List;

/**
 * PathCollection for holding path
 * 
 * @author kwwang
 * @date 2010-1-21
 */
public class PathCollection
{
    private List<Path> pathList = new ArrayList<Path>();

    public List<Path> getPathList()
    {
        return pathList;
    }

    public void addPath(String path)
    {
        Path pa = new Path();
        pa.path = path;
        pathList.add(pa);
    }

    public void addChild(String path)
    {
        Path found = findMatchedPath(path);
        if (found != null)
            found.childrenPath.add(path);
    }

    /**
     * To avoid the case like path is UN_MAPS/ANDROID/.. and the p.path is UN and UN_MAPS, just find the longest matched
     * substring.
     * 
     * @param path
     * @return
     */
    private Path findMatchedPath(String path)
    {
        Path found = null;
        int length = -1;
        for (Path p : pathList)
        {
            int tempLen = p.path.length();
            if (path.contains(p.path) && tempLen > length)
            {
                found = p;
                length = tempLen;
            }
        }
        return found;
    }

    class Path
    {
        String path;

        List<String> childrenPath = new ArrayList<String>();

        public String getPath()
        {
            return path;
        }

        public List<String> getChildrenPath()
        {
            return childrenPath;
        }
    }
}
