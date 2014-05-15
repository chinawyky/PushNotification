/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.j2me.datatypes.GpsData;

/**
 * Executor Request
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public class ExecutorRequest 
{
	private String executorType;

	private UserProfile userProfile;
	
	private List<GpsData> gpsData;
	
	/**
	 * others
	 */
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	/**
	 * @return the userProfile
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}

	/**
	 * @param userProfile the userProfile to set
	 */
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	/**
	 * @return the actionType
	 */
	public String getExecutorType() {
		return executorType;
	}

	/**
	 * @param actionType the actionType to set
	 */
	public void setExecutorType(String actionType) {
		this.executorType = actionType;
	}
	
	
	/**
	 * @return the gpsData
	 */
	public List<GpsData> getGpsData() {
		return gpsData;
	}

	/**
	 * @param gpsData the gpsData to set
	 */
	public void setGpsData(List<GpsData> gpsData) {
		this.gpsData = gpsData;
	}

	/**
	 * get attribute from the map
	 * 
	 * @param key
	 * @return
	 */
	public String getAttribute(String key) {
		return (String) attributes.get(key);
	}

	/**
	 * set attribute
	 * 
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	public Map<String, Object> getAttributes()
	{
		return attributes;
	}
	
	public String toString()
	{
		return "type:" + this.getExecutorType() 
			 + ",UserProfile:" + this.getUserProfile()
			 + ",attributes:"  + this.getAttributes();
	}
}
