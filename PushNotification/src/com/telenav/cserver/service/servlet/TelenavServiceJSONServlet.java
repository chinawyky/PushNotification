/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.service.servlet;

import com.telenav.cserver.framework.executor.protocol.ProtocolHandler;
import com.telenav.cserver.framework.executor.protocol.json.JsonProtocolHandler;

/**
 * TelenavServiceServlet, the entry for HTTP client
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 * 
 */
public class TelenavServiceJSONServlet extends AbsTelenavServiceServlet
{
   

    /**
     * 
     */
    private static final long serialVersionUID = -6899659560941093302L;

    @Override
    protected ProtocolHandler getHandler()
    {
        return new JsonProtocolHandler();
    }

}
