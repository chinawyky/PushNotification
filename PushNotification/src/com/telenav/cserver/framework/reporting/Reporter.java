/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.reporting;

/**
 * Reporter interface, the implementation could be based on MIS, or other purposes.
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-6-4
 *
 */
public interface Reporter 
{
	/**
	 * report 
	 * 
	 * @param request
	 * @return
	 */
	public ReportingResponse report(IReportingRequest request);
}
