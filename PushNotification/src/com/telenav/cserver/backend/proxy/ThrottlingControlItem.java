/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.throttling.ThrottlingException;

/**
 * ThrottlingControlItem
 * @author kwwang
 *
 */
public class ThrottlingControlItem
{

    public static final Logger logger = Logger.getLogger(ThrottlingControlItem.class);

    private String proxyName;

    private int maxNumberOnLine;

    private int curNumOnLine;

    public String getProxyName()
    {
        return proxyName;
    }

    public void setProxyName(String proxyName)
    {
        this.proxyName = proxyName;
    }

    public int getMaxNumberOnLine()
    {
        return maxNumberOnLine;
    }

    public void setMaxNumberOnLine(int maxNumberOnLine)
    {
        this.maxNumberOnLine = maxNumberOnLine;
    }

    public int getCurNumOnLine()
    {
        return curNumOnLine;
    }

    public void setCurNumOnLine(int curNumOnLine)
    {
        this.curNumOnLine = curNumOnLine;
    }

    public synchronized void increment() throws ThrottlingException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug(proxyName + " increment, current online number is " + curNumOnLine);
        }
        curNumOnLine = curNumOnLine + 1;
        if (curNumOnLine > maxNumberOnLine)
            throw new ThrottlingException(proxyName + " has been reached the max online number," + this.maxNumberOnLine);
    }

    public synchronized void decrement()
    {

        if (logger.isDebugEnabled())
        {
            logger.debug(proxyName + " decrement, current online number is " + curNumOnLine);
        }
        curNumOnLine = curNumOnLine - 1;
        if (curNumOnLine < 0)
        {
            logger.fatal(proxyName + " reachs a abnormal online number, " + curNumOnLine);
            curNumOnLine = 0;
        }
    }
}
