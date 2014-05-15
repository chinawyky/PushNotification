/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.service.servlet;

import com.telenav.cserver.framework.executor.protocol.ProtocolHandler;
import com.telenav.cserver.framework.executor.protocol.txnode.TxNodeProtocolHandler;

/**
 * TelenavServiceServlet, the entry for HTTP client
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 * 
 */
public class TelenavServiceServlet extends AbsTelenavServiceServlet
{
    

    /**
     * 
     */
    private static final long serialVersionUID = 70301105308611318L;

    @Override
    protected ProtocolHandler getHandler()
    {
        return new TxNodeProtocolHandler();
    }

}
