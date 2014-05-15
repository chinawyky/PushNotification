/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.service.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.telenav.cserver.common.resource.ResourceCacheManagement;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.common.resource.ResourceHolder;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.ServletHTMLDisplayFormat;
import com.telenav.cserver.common.resource.dns.DNSPropertiesCache;
import com.telenav.cserver.common.resource.fstree.TreeConstructor;
import com.telenav.cserver.framework.cli.LogDeploymentInfo;
import com.telenav.cserver.framework.transportation.ServletUtil;
import com.telenav.cserver.resource.common.prompts.PromptsLoader;

/**
 * ResourceManagementServlet.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-8
 *
 */
public class ResourceManagementServlet extends HttpServlet 
{
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
	doPost(req, res);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		ResourceFactory.getInstance();
		
		String action = req.getParameter("action");
		if(action == null || action.length() == 0)
		{
			action = "view";
		}
		Collection<ResourceHolder> allHolders = ResourceHolderManager.getAllResourceHolder();
		Iterator<ResourceHolder> iterator = allHolders.iterator();

		if(action.equalsIgnoreCase("refresh"))
		{
			//Clear DNS Cache
			TreeConstructor.init();
			DNSPropertiesCache.initPropertiesFile();
			
			while(iterator.hasNext())
			{
				ResourceHolder holder = iterator.next();
				holder.clear();
			}
			
			PromptsLoader.getInstance().refresh();
			
			ServletUtil.sendResponse(res, "Refresh OK\r\n<br/>\r\n<br/>".getBytes());
		}
		if(action.equalsIgnoreCase("version"))
        {
		    String version = LogDeploymentInfo.getVersion();
            ServletUtil.sendResponse(res, (version + "\r\n<br/>\r\n<br/>").getBytes());
            return;
        }
		
		if (action.equalsIgnoreCase("printTree"))
		{
		    List<String> treeList = TreeConstructor.listAll(TreeConstructor.getTree());
		    StringBuilder sb = new StringBuilder();
		    
		    for (String temp : treeList)
		    {
		        sb.append(temp).append("\r\n<br/>");
		    }
		    ServletUtil.sendResponse(res, (sb.toString() + "\r\n<br/>\r\n<br/>").getBytes());
            return;
		}
		
		handleView(req,res);
	}
	
	private final static String KEY_OPERATION = "operation";
	private final ServletHTMLDisplayFormat display = new ServletHTMLDisplayFormat(ResourceCacheManagement.getInstance());
	private void handleView(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
        String operation = req.getParameter(KEY_OPERATION);
        String result = "";
        
	    if ("contents".equals(operation))
	    {
	        String holderName = req.getParameter("holderName");
	        String key=req.getParameter("key");
	        result = display.contents(holderName, key);
	    }
	    else if("cachedClients".equals(operation))
	    {
	        String holderName = req.getParameter("holderName");
	        String key=req.getParameter("key");
	        result = display.viewCachedClient(holderName, key);
	    }
	    else
	    {
	        result = display.details();
	    }
	    if(result == null)
	        result = "";
	    ServletUtil.sendResponse(res, result.getBytes());
	}
}
