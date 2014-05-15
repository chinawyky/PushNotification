package com.telenav.cserver.framework.threadpool;

/**
 * 
 * @author donghengz
 *
 */
public interface Job 
{
    public void doIt(int handlerID) throws Exception;
}
