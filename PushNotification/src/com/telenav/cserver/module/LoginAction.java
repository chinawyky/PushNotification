package com.telenav.cserver.module;

import com.opensymphony.xwork2.Action;

/**
 * @author  yeeku.H.lee kongyeeku@163.com
 * @version  1.0
 * <br>Copyright (C), 2005-2008, yeeku.H.Lee
 * <br>This program is protected by copyright laws.
 * <br>Program Name:
 * <br>Date: 
 */

public class LoginAction implements Action
{
	private String username;
	private String password;
	private String tip;

	private MyService ms;
	public void setMs(MyService ms)
	{
		this.ms = ms;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getUsername()
	{
		 return this.username;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getPassword()
	{
		 return this.password;
	}

	public void setTip(String tip)
	{
		this.tip = tip;
	}
	public String getTip()
	{
		 return this.tip;
	}



    public String execute() throws Exception
	{
    	//String action = "telenav://map?v=2.0&cb=planner%3A%2F%2F08092009&k=AQAAASSR1YUgf%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQUm2yry0wCYjB0QetcacwXwEAAAAOAwAAABEAAAAZAAAAAQA%3D&c=cn&markers=CURRENT";
    	String action = "telenav://search?v=2.0&cb=planner%3A%2F%2F08092009&k=AQAAASSR1YUgf%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQUm2yry0wCYjB0QetcacwXwEAAAAOAwAAABEAAAAZAAAAAQA%3D&c=cn&namedAddr=CURRENT&term=coffee";
    	long userId = 10107352;
    	
    	ms.pushNotification("title", "message", action, userId);
        
        return SUCCESS;
       
	}
}