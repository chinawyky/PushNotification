/*
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @author huishen
 * @since 2013-02-22
 *
 */
public class FileNameLoadOrder extends LoadOrder 
{
    public FileNameLoadOrder()
	{
    	setType("file");
	}
	

    private String fileName;
    
	public FileNameLoadOrder(String fileName)
	{
		setType("file");
	    this.fileName = fileName;
	}
	
	public String getFileName()
    {
        return fileName;
    }
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    
	public String getAttributeValue(UserProfile profile, TnContext tnContext)
	{
		return this.fileName;
	}
	
}