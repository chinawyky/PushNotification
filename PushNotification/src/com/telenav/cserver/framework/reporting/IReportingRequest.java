/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.reporting;

import java.util.List;
import java.util.Map;

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
public interface IReportingRequest 
{

	
	/**
	 * @return the tnContext
	 */
	public TnContext getTnContext();
	
	/**
	 * @param tnContext the tnContext to set
	 */
	public void setTnContext(TnContext tnContext);
	
	/**
	 * @return the type
	 */
	public ReportType getType();
	
	/**
	 * @param type the type to set
	 */
	public void setType(ReportType type);
	
	/**
	 * @return the userProfile
	 */
	public UserProfile getUserProfile();
	
	/**
	 * @param userProfile the userProfile to set
	 */
	public void setUserProfile(UserProfile userProfile);
	
	
	public void setGpsList(List<GpsData> gpsList);
	
	public List<GpsData>  getGpsList();

	/**
	 * @return the clientTime
	 */
	public long getClientTime();

	/**
	 * @param clientTime the clientTime to set
	 */
	public void setClientTime(long clientTime);
	/**
	 * @return the serverTime
	 */
	public long getServerTime();

	/**
	 * @param serverTime the serverTime to set
	 */
	public void setServerTime(long serverTime);
	
	
	public Map<Integer,String> getOverwrittenMisLogKeyValueMap();
	
	public void addOverwrittenMisLogKeyValue(Integer misLogIndex,String misLogValue);
	
    /**
     * get mis log event
     * @return
     */
	public List<Map<Long, String>> getMisLogEvents();
    

    /**
     * @param misLogEvents
     */
	public void setMisLogEvents(List<Map<Long, String>> misLogEvents);
    
    
    public void addServerMisLogField(long key, String field);
    
    
    /**
     * whether the log is writen 
     * @return
     */
    public boolean isWriten();
    
    
    /**
     * set writen flag 
     * @param isWriten
     */
    public void setWriten(boolean isWriten);
    
}
