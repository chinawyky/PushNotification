/*
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.service.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.fstree.ClientPathCollector;
import com.telenav.cserver.common.resource.fstree.TreeConstructor;
import com.telenav.cserver.framework.transportation.ServletUtil;

/**
 * @author huishen
 * @since 2013-03-126
 *
 */
public class RetrieveConfContentServlet extends HttpServlet
{     
    private static Logger logger = Logger.getLogger(RetrieveConfContentServlet.class);
    static final String LINE_SEPARATOR = System.getProperty("line.separator");   
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        doPost(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        try
        {
            String client = req.getParameter("client");
            String needContent = req.getParameter("needContent");
            List<String> paths;
            StringBuffer sb = new StringBuffer();
            
            if (client.equalsIgnoreCase("untest"))
            {
                paths = TreeConstructor.listUnTestedFiles(TreeConstructor.getTree());
            }
            else 
            {
                paths = ClientPathCollector.getClientPath(client, ResourceHolderManager.getAllResourceHolder(), new Boolean(needContent));
            }

            for (String temp : paths)
            {
                sb.append(temp).append(LINE_SEPARATOR);
            }
            ServletUtil.sendResponse(res, sb.toString().getBytes());
        } 
        catch (Exception e)
        {
            logger.fatal("Exception occur ");
        }
        
    }
}
