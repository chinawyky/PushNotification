/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.service.servlet;

import com.telenav.cserver.framework.executor.protocol.ProtocolHandler;
import com.telenav.cserver.framework.executor.protocol.protobuf.ProtocolBufferHandler;

/**
 * TelenavServiceServlet, the entry for HTTP client
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 * 
 */
public class TelenavServicePbServlet extends AbsTelenavServiceServlet
{

    /**
     * 
     */
    private static final long serialVersionUID = 8582631438476011338L;

    @Override
    protected ProtocolHandler getHandler()
    {
        return new ProtocolBufferHandler();
    }
    

}
