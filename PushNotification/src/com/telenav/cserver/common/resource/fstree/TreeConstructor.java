/*
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.fstree;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Category;

import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.framework.trump.TrumpRunnable;

/**
 * @author huishen
 * @since 2013-03-01
 */
public class TreeConstructor
{
    public static final String ROOT_PATH = "device";
    
    public static final String ZIP_FOLDER_PATH = TrumpRunnable.getZipFolderPath();

    private static String[] resourcePath = new String[] { ROOT_PATH, ZIP_FOLDER_PATH + "/" + ROOT_PATH };

    private static final String COMMON_BAR = "common_bar";

    private static final String BAR = "bar";

    private static final String PRELOAD_CONFIG = "preload_config";

    private static final String SDP_COMMON = "sdp_common.xml";

    private static FSTreeNode root;

    private static final ReentrantReadWriteLock RWLOCK = new ReentrantReadWriteLock();

    protected static final Category LOGGER = Category.getInstance(TreeConstructor.class);

    private static boolean initialed;

    public static void init() throws IOException
    {
        init(resourcePath);
    }
    
    private TreeConstructor()
    {}

    public static void init(final String[] paths)
    {
        try
        {
            LOGGER.debug("Begin to init the tree with path: " + paths);
            resourcePath = paths;

            final FSTreeNode rootToUse = new FSTreeNode(ROOT_PATH);
            Enumeration<URL> tempPaths;
            URL url;
            File father;
            for (String path : paths)
            {
                tempPaths = Thread.currentThread().getContextClassLoader().getResources(path);
                while (tempPaths.hasMoreElements())
                {
                    url = (URL) tempPaths.nextElement();
                    LOGGER.debug("Path: " + url.getPath());
                    father = new File(url.getPath());
                    construct(father, rootToUse);
                }
            }

            RWLOCK.writeLock().lock();
            root = rootToUse;
            RWLOCK.writeLock().unlock();

            initialed = true;
            LOGGER.debug("Finish to init the tree.");
        }
        catch (IOException e)
        {
            LOGGER.fatal("Fail to init the tree." + e.toString());
        }

    }

    private static void construct(final File father, final FSTreeNode fatherNode)
    {
        FSTreeNode childNode;
        final File[] fileList = father.listFiles();

        if (!ArrayUtils.isEmpty(fileList))
        {
            for (File file : fileList)
            {
                // Do not construct sub-tree for preload_config
                if (PRELOAD_CONFIG.equals(file.getName()) || SDP_COMMON.equals(file.getName()))
                {
                    continue;
                }

                childNode = fatherNode.containChild(file.getName());

                if (childNode == null)
                {
                    childNode = new FSTreeNode(file.getName());
                    fatherNode.addChild(childNode);
                }

                // Do not construct sub-tree for common_bar / bar folder / preload_config
                if (!(COMMON_BAR.equals(file.getName()) || BAR.equals(file.getName())))
                {
                    construct(file, childNode);
                }
            }
        }
    }

    public static boolean isInit()
    {
        return initialed;
    }

    public static List<String> listAll(final FSTreeNode father)
    {
        final List<String> result = new ArrayList<String>();
        final List<FSTreeNode> childrenNodes = father.getChildren();
        List<String> temp;

        for (FSTreeNode child : childrenNodes)
        {
            if (child.noChildren())
            {
                result.add(child.getName());
            }

            if (child.hasChildren())
            {
                temp = listAll(child);
                temp = appendList(child.getName() + ResourceFactory.SEPARATOR, temp);
                result.addAll(temp);
            }
        }
        return result;
    }

    public static List<String> listUnTestedFiles(final FSTreeNode father, final String fileName)
    {
        final List<String> result = new ArrayList<String>();
        final List<FSTreeNode> childrenNodes = father.getChildren();
        List<String> temp;

        for (FSTreeNode child : childrenNodes)
        {
            if (child.noChildren() && child.notTested())
            {
                if (StringUtils.isEmpty(fileName) || child.getName().startsWith(fileName))
                {
                    result.add(child.getName());
                }
            }

            if (child.hasChildren())
            {
                temp = listUnTestedFiles(child, fileName);
                temp = appendList(child.getName() + ResourceFactory.SEPARATOR, temp);
                result.addAll(temp);
            }
        }
        return result;
    }

    public static List<String> listUnTestedFiles(final FSTreeNode father)
    {
        return listUnTestedFiles(father, null);
    }

    private static List<String> appendList(final String prefix, final List<String> list)
    {
        if (list != null && !list.isEmpty())
        {
            String temp;
            for (int i = 0; i < list.size(); i++)
            {
                temp = list.get(i);
                list.set(i, prefix + temp);
            }
        }
        return list;
    }

    public static FSTreeNode getTree()
    {
        try
        {
            RWLOCK.readLock().lock();
            return root;
        }
        finally
        {
            RWLOCK.readLock().unlock();
        }

    }
}
