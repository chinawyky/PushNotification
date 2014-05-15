/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx;

import java.io.Serializable;

import com.telenav.cserver.framework.util.JSONUtil;

/**
 * DetectResult, the monitor result wrapper.
 * @author kwwang
 *
 */
public class DetectResult implements Serializable {
    
    private static final long serialVersionUID = 0x49ce06619ec14952L;
    
	public boolean isSuccess = false;

	public long errCode = -1001l;

	public String msg = "";

	public String name = "";
	
	public double testTime = 0;  //unit is ms
	
	public DetectResult(){}

	public DetectResult(String name) {
		this.name = name;
	}

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public String toJSON(){
        StringBuffer resultJson = new StringBuffer("{");
        resultJson.append("\"name\": ").append("\"").append(JSONUtil.filterSpecial(this.getName())).append("\", ")
                  .append("\"msg\":" ).append("\"").append(JSONUtil.filterSpecial(this.msg)).append("\", ")
                  .append("\"isSuccess\": ").append("\"").append(this.isSuccess ? "true" : "false").append("\", ")
                  .append("\"testTime\": ").append("\"").append(testTime).append("\" ")
                  .append("}");
        
        return resultJson.toString();
    }
    
    
}
