/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.reporting;

/**
 * ReportType.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-6-4
 *
 */
public class ReportType 
{
    public static final ReportType SERVER_MIS_LOG_REPORT = new ReportType("SERVER_MIS_LOG");
    
    
	private String type;

	public ReportType(String type) 
	{
		super();
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public String toString()
	{
		return "type:" + type;
	}
    
    public boolean equals(Object anObject)
    {
        if (this == anObject)
        {
            return true;
        }
        if (anObject instanceof ReportType)
        {
            ReportType anotherReportType = (ReportType)anObject;
            if (anotherReportType.getType() == null)
            {
            	return (this.type == null);                
            }
            if (anotherReportType.getType().equals(this.type))
            {
                return true;
            }
        }
        return false;
    }
    public int hashCode()
    {
    	return 0;
    }
}
