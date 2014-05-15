/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.reporting.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.reporting.IReportingRequest;
import com.telenav.cserver.framework.reporting.Reporter;
import com.telenav.cserver.framework.reporting.ReportingResponse;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * MISReportor.java 
 * 
 * http://spaces.telenav.com:8080/display/tn/MIS+Reporting 
 * 
 * Line Delimeter can be \r or \r\n, as long as it¡¯s consistent.
 * Field Delimeter can be ascii(1).
 * Ordering of fields is not required for key-value pairs.
 * 
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2009-6-19
 * 
 */
public class MISReportor implements Reporter
{
	/**
	 * The logger for this class.
	 */
	private static final Logger recorder = Logger.getLogger(MISReportor.class);
	
	/**
	 * field delimiter
	 */
	private static String delimiter = Character.toString((char)1);
	
	
	/**
	 * version for this logging
	 */
	private static String logVersion = "1";
	
	/**
	 * report 
	 * 
	 * @param request
	 * @return
	 */
	public ReportingResponse report(IReportingRequest request)
	{
		List<Map<Long, String>> misLogEvents = request.getMisLogEvents();
		for (Iterator iterator = misLogEvents.iterator(); iterator.hasNext();) 
		{
			Map<Long, String> eventAttributeMap = (Map<Long, String>) iterator.next();	
			StringBuilder recordString = new StringBuilder();
			//1. version
			recordString.append("logVersion=" + logVersion).append(delimiter);
			//2. timestamp
			// recordString.append("client_timestamp=" + request.getClientTime()).append(delimiter);  // We are not logging client_timestamp as part of mandatory nodes, since we do not get them anyway.
			recordString.append("server_timestamp=" + request.getServerTime()).append(delimiter);
			
			//3. user profile
			UserProfile userProfile = request.getUserProfile(); 
			if(userProfile != null)
			{
				recordString.append("ptn=" + userProfile.getMin()).append(delimiter);
				recordString.append("carrier=" + userProfile.getProgramCode()).append(delimiter);//The value here should be ProgramCode while mis team is unwilling to change the key.
				recordString.append("version=" + userProfile.getVersion()).append(delimiter);
				recordString.append("platform=" + userProfile.getPlatform()).append(delimiter);
				recordString.append("product_type=" + userProfile.getProduct()).append(delimiter);
				recordString.append("device=" + userProfile.getDevice()).append(delimiter);
				recordString.append("buildNumber=" + userProfile.getBuildNumber()).append(delimiter);
				recordString.append("locale=" + userProfile.getLocale()).append(delimiter);
				recordString.append("region=" + userProfile.getRegion()).append(delimiter);
			}
			//4. map/poi data
			TnContext tnContext = request.getTnContext();
			if(tnContext != null)
			{
				recordString.append("map_provider=" + tnContext.getProperty(TnContext.PROP_MAP_DATASET)).append(delimiter);
				recordString.append("poi_provider=" + tnContext.getProperty(TnContext.PROP_POI_DATASET)).append(delimiter);
					
			}
			//5. business specific data
			addBusinessSpecificData(eventAttributeMap, recordString);
			
			//record into log4j
			recorder.info(recordString.toString());
		}
		ReportingResponse response = new ReportingResponse();
		return response;
	}

	/**
	 * @param request
	 * @param recordString
	 */
	/*
	 * private void addBusinessSpecificDataOld(ReportingRequest request,
			StringBuilder recordString) {
		Map<String,String> attributes = request.getAttributes();
		if(attributes != null)
		{
			
			Set<Map.Entry<String, String>> entrySet = attributes.entrySet();
			Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
			while(iterator.hasNext())
			{
				Map.Entry<String, String> entry = iterator.next();
				recordString.append(entry.getKey() + "=" + entry.getValue()).append(delimiter);
				
			}
		}
	}*/
	
	
	private void addBusinessSpecificData(Map<Long, String> eventAttributeMap,StringBuilder recordString) 
	{
		Iterator<Map.Entry<Long, String>> iterator = eventAttributeMap.entrySet().iterator();
		while(iterator.hasNext())
		{
			Map.Entry<Long, String> entry = iterator.next();
			recordString.append(entry.getKey() + "=" + entry.getValue()).append(delimiter);	
		}
	}
}
