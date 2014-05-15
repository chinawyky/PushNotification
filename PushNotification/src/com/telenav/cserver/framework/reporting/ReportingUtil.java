/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.reporting;

import com.telenav.cserver.framework.reporting.impl.MISReportor;
import com.telenav.cserver.framework.reporting.impl.ServerMISReportor;

/**
 * ReportingUtil.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-6-4
 *
 */
public class ReportingUtil 
{
	static MISReportor misReportor = new MISReportor();
    
    static ServerMISReportor serverMISReportor = new ServerMISReportor();
    
    
	
	/**
	 * report 
	 * 
	 * @param request
	 * @return
	 */
	public static ReportingResponse report(IReportingRequest request)
	{
        if (ReportType.SERVER_MIS_LOG_REPORT.equals(request.getType()))
            return serverMISReportor.report(request);
        else
            return misReportor.report(request);
	}
    
}
