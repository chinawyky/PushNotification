/**
 * (c) Copyright 2008 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.external_service;

import javax.servlet.http.HttpServlet;


/**
 * MarkDownServlet 
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2008-5-15
 *
 */
public class MarkDownServlet extends HttpServlet
{
//	TODO: re-design configuration later 
//	protected static TVCategory logger = (TVCategory) TVCategory
//			.getInstance(MarkDownServlet.class);
//
//	/**
//	 * doGet() just calls doPost()
//	 */
//	public void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException
//	{
//		doPost(request, response);
//	}
//
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, java.io.IOException
//	{
//		// get the remote host and check whether it is in the white list
//		// if in, continue, else, reject this request with error message
//		String errorMsg = "IP isn't in white list";
//		if (IpWhiteList.isInWhiteList(request.getRemoteAddr(), request
//				.getServerName()) != true)
//		{
//			byte[] respBuff = generateResult(errorMsg);
//			NavBean.sendResponse(response, respBuff);
//			logger.debug("IP isn't in white list>>" + request.getRemoteAddr());
//			return;
//		}
//
//		/**
//		 * service type, "billing"
//		 */
//		String service = request.getParameter("service");
//		String result = "ok";
//		
//		boolean returnStatus = ExternalServiceManager.markDown(service); 
//		result = returnStatus ? "ok" : "fail";
//
//		byte[] respBuff = generateResult(result);
//		NavBean.sendResponse(response, respBuff);
//
//	}
//
//	private byte[] generateResult(String status)
//	{
//		return status.getBytes();
//	}
}
