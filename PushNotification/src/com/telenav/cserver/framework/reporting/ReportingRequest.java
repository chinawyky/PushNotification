/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.j2me.datatypes.GpsData;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * ReportingRequest.javax
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-6-4
 *
 */
public class ReportingRequest implements IReportingRequest
{
	private ReportType type;
	private UserProfile userProfile;
	private TnContext tnContext;
	
	private long clientTime = System.currentTimeMillis();
	private long serverTime = System.currentTimeMillis();
	
	// private Map<String,String> attributes;
	private List<Map<Long,String> >  misLogEvents = null;
    
    private boolean isWriten  = false;
    private List<GpsData> gpsList;
    
    private Map<Integer,String> overwrittenMisLogKeyValueMap = new HashMap<Integer, String>();
    
	
//	/**
//	 * @return the attributes
//	 */
//	public Map<String, String> getAttributes() {
//		return attributes;
//	}
//	
//	/**
//	 * @param attributes the attributes to set
//	 */
//	public void setAttributes(Map<String, String> attributes) {
//		this.attributes = attributes;
//	}
    
    public ReportingRequest()
    {
        
    }
	
    public ReportingRequest(ReportType type, UserProfile userProfile, TnContext tnContext)
    {
        this.type = type;
        this.userProfile = userProfile;
        this.tnContext = tnContext;
        this.misLogEvents = new ArrayList<Map<Long,String> >();
        misLogEvents.add(new HashMap<Long, String>());
    }
    
    public ReportingRequest(ReportType type, UserProfile userProfile, List<GpsData> gpsList, TnContext tnContext)
    {
        this.type = type;
        this.userProfile = userProfile;
        this.tnContext = tnContext;
        this.misLogEvents = new ArrayList<Map<Long,String> >();
        misLogEvents.add(new HashMap<Long, String>());
        this.gpsList = gpsList;
    }
    
	/**
	 * @return the tnContext
	 */
	public TnContext getTnContext() {
		return tnContext;
	}
	
	/**
	 * @param tnContext the tnContext to set
	 */
	public void setTnContext(TnContext tnContext) {
		this.tnContext = tnContext;
	}
	
	/**
	 * @return the type
	 */
	public ReportType getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(ReportType type) {
		this.type = type;
	}
	
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
	 * @return the clientTime
	 */
	public long getClientTime() {
		return clientTime;
	}

	/**
	 * @param clientTime the clientTime to set
	 */
	public void setClientTime(long clientTime) {
		this.clientTime = clientTime;
	}

	/**
	 * @return the serverTime
	 */
	public long getServerTime() {
		return serverTime;
	}

	/**
	 * @param serverTime the serverTime to set
	 */
	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}

	public String toString()
	{
		return "type:" + type
				+ "\r\n" + "userProfile:" + userProfile
				+ "\r\n" + "tnContext:" + tnContext
				+ "\r\n" + "misLogEvents:" + misLogEvents;
	}

	public List<Map<Long, String>> getMisLogEvents() {
		return misLogEvents;
	}

	public void setMisLogEvents(List<Map<Long, String>> misLogEvents) {
		this.misLogEvents = misLogEvents;
	}
    
    public synchronized void addServerMisLogField(long key, String field)
    {
        Map<Long, String> serverMISLogEvents = misLogEvents.get(0);
        serverMISLogEvents.put(key, field);
    }
    
    public boolean isWriten()
    {
        return isWriten;
    }
    
    public void setWriten(boolean isWriten)
    {
        this.isWriten = isWriten;
    }

    @Override
    public List<GpsData> getGpsList()
    {
        return this.gpsList;
    }

    @Override
    public void setGpsList(List<GpsData> gpsList)
    {
        this.gpsList = gpsList;
    }

	@Override
	public Map<Integer, String> getOverwrittenMisLogKeyValueMap() {
		return overwrittenMisLogKeyValueMap;
	}

	@Override
	public void addOverwrittenMisLogKeyValue(Integer misLogIndex,
			String misLogValue) {
		overwrittenMisLogKeyValueMap.put(misLogIndex, misLogValue);
	}
}
