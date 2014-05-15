/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.reporting.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.reporting.IReportingRequest;
import com.telenav.cserver.framework.reporting.Reporter;
import com.telenav.cserver.framework.reporting.ReportingResponse;
import com.telenav.j2me.datatypes.GpsData;

/**
 * ServerMISReportor.java 
 * Field Delimeter can be ",".
 * @author jzhu@telenav.cn
 * @version 1.0 2010-1-8
 * 
 */
public class ServerMISReportor  implements Reporter
{
    public static final long TIMESTAMP = 0;
    public static final long HOST_NAME = 1;
    public static final long EXEC_TIMESPAN = 2;
    public static final long COMPLETED_FLAG = 3;
    public static final long SERVLET_NAME = 4;
    public static final long LOGTYPE_ID = 5;
    public static final long PTN = 6;
    public static final long ACTION_ID = 7;
    public static final long CUSTOM00 = 8;
    public static final long CUSTOM01 = 9;
    public static final long CUSTOM02 = 10;
    public static final long CUSTOM03 = 11;
    public static final long CUSTOM04 = 12;
    public static final long CUSTOM05 = 13;
    public static final long CUSTOM06 = 14;
    public static final long CUSTOM07 = 15;
    public static final long CUSTOM08 = 16;
    public static final long CUSTOM09 = 17;
    public static final long CUSTOM10 = 18;
    public static final long CUSTOM11 = 19;
    public static final long CUSTOM12 = 20;
    public static final long CUSTOM13 = 21;
    public static final long CUSTOM14 = 22;
    public static final long CUSTOM15 = 23;
    public static final long CUSTOM16 = 24;
    public static final long CUSTOM17 = 25;
    public static final long CUSTOM18 = 26;
    public static final long CUSTOM19 = 27;
    public static final long CUSTOM20 = 28;
    
    //public static final int ATTR_SIZE = 28;
    public static final int ATTR_SIZE = 48;//for cmcc add more usage mis log
    
    public static final String DYNAMICROUTE_LOGTYPE = "10";
    public static final String CHECKDEVIATION_LOGTYPE = "17";
    public static final String GETSTATIC_LOGTYPE = "12";
    public static final String MOVINGALERT_LOGTYPE = "450";
    public static final String TRAFFICSUMMARY_LOGTYPE = "451";
    public static final String VALIDATE_ADDRESS_LOGTYPE = "100";
    public static final String VALIDATE_AIRPORT_LOGTYPE = "101";
    public static final String GET_POI_LOGTYPE = "201";
    public static final String ONE_BOX_LOGTYPE = "206";
    public static final String AVOIDSEGMENT_LOGTYPE = "21";
    public static final String SELECTEDRETOUTE_LOGTYPE = "22";
    public static final String STATICMINDELAYRETOUTE_LOGTYPE = "23";
    public static final String STATICAVOIDSEGMENT_LOGTYPE = "24";
    public static final String GETEXTRAEDGE_LOGTYPE = "13";
    public static final String GETEXTRAINFO_LOGTYPE = "14";
    public static final String GETMAP_LOGTYPE = "18";
    public static final String GETCOMMUTEALERTDETAILS_LOGTYPE = "28";
    public static final String GETTRAFFICMINDELAY_LOGTYPE = "19";
    public static final String GETTRAFFICAVOIDINCIDENT_LOGTYPE = "20";
    
    public static final String DECIMATEMULTIROUTE_LOGTYPE = "460";
    public static final String SYNCPURCHASED_LOGTYPE = "43";
    public static final String LOGIN_LOGTYPE = "5";
    
    
    public static final String ROUTING_SERVLET_NAME="/nav_map_service/telenav-server";
    public static final String COMMON_SERVLET_NAME="/common_service/telenav-server";
    public static final String RESOURCE_SERVLET_NAME="/resource_service/telenav-server";
    public static final String COMMUTE_SERVLET_NAME = "Commute";
    public static final String LOGIN_STARTUP_SERVLET_NAME = "login_startup_csvr_service";
    public static final String MOVIE_SERVLET_NAME = "movie";
    public static final String POI_SERVLET_NAME = "poi_csvr_service";
    
    
    public static final String COMPLETE_SUCCEED ="1";
    public static final String COMPLETE_FAIL ="0";
    
    public static final String TRAFFIC_INCIDENT_FLOW = "1";
    public static final String TRAFFIC_INCIDENT_ONLY = "2";
    public static final String TRAFFIC_FLOW_ONLY = "3";
    public static final String TRAFFIC_NONE = "4";
    public static final String TRAFFIC_UNKNOWN = "5";
    
    private static final String hostName = getHostName();
    
    
    /**
     * The logger for this class.
     */
    private static final Logger recorder = Logger.getLogger(ServerMISReportor.class);
    
    /**
     * field delimiter
     */
    private static String delimiter = ",";
    
    /**
     * report 
     * 
     * @param request
     * @return
     */
    public synchronized ReportingResponse report(IReportingRequest request)
    {
        ReportingResponse response = new ReportingResponse();
        
        if (request.isWriten())
        {
            //TODO log to debug log
            return response;
        }
        
        String data = joinFields(request);
        
        recorder.info(data);
        
        request.setWriten(true);
        
        return response;
    }
    
    
    protected String filterSpecialChars(String str)
    {
        String result = "";
        if (str != null)
        {
            result = str.replaceAll("\"|,|\n|\r|'", " ").trim();
        }

        return result;
    }
    

    private String[] initialAttrArray(IReportingRequest request)
    {
        String[] attrs = new String[ATTR_SIZE];
        for(int i=0; i<attrs.length; i++)
        {
            attrs[i] = "";
        }
        
        attrs[(int)COMPLETED_FLAG] = COMPLETE_FAIL;//default is fail
        attrs[(int)HOST_NAME] = hostName;
        
        UserProfile userProfile = request.getUserProfile();
        if (userProfile != null)
        {
            attrs[(int)PTN] = userProfile.getMin();
            
            if( userProfile.getMin() == null || "".equals(userProfile.getMin().trim()) ){
                CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
                cli.addData(CliConstants.LABEL_ERROR, "min["+userProfile.getMin()+"] is empty!");
            }
            
            attrs[(int)CUSTOM12] = userProfile.getProduct();
            attrs[(int)CUSTOM13] = userProfile.getDevice();
            attrs[(int)CUSTOM14] = userProfile.getPlatform();
            //attrs[(int)CUSTOM15] = userProfile.getLocale();
            
            attrs[(int)CUSTOM17] = StringUtils.isEmpty(userProfile.getDeviceCarrier()) ? userProfile.getCarrier() : userProfile.getDeviceCarrier();
            attrs[(int)CUSTOM18] = userProfile.getOriginalVersion();
            attrs[(int)CUSTOM19] = userProfile.getUserId();
            attrs[(int)CUSTOM20] = userProfile.getProgramCode();
        }
        
        List<GpsData> gpsList = request.getGpsList();
        if( gpsList != null && gpsList.size() != 0 )
        {
            int length = gpsList.size();
            for( int i=0; i<length; i++)
            {
                GpsData gps = gpsList.get(i);
                if( gps != null )
                {
                    attrs[(int)CUSTOM09] = ""+gps.lat;
                    attrs[(int)CUSTOM10] = ""+gps.lon;
                    break;
                }
            }
        }
        
        return attrs;
    }
    
    private static String getHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
            return "unknown";
        }
    }
    
    
    /*
     * Concatenates all the strings in fields[] and returns as one string,
     * separated by FIELD_SEPARATOR
     */
    private String joinFields(IReportingRequest request)
    {
        String[] attrs = initialAttrArray(request);
        
        Map<Long, String> attr = request.getMisLogEvents().get(0);
        
        Iterator<Long> it = attr.keySet().iterator();
        Long idx;
        while (it.hasNext())
        {
            idx = it.next();
            attrs[idx.intValue()] = attr.get(idx);
        }
        
        attrs[(int)TIMESTAMP] = String.valueOf(System.currentTimeMillis());
        attrs[(int)EXEC_TIMESPAN] = String.valueOf(System.currentTimeMillis() - request.getServerTime());

        //overwrite the fields if any
        Map<Integer,String> overwrittenMisLogKeyValueMap = request.getOverwrittenMisLogKeyValueMap();
        Iterator<Integer> overwrittenMisLogKeyValueMapIterator = overwrittenMisLogKeyValueMap.keySet().iterator();
        while(overwrittenMisLogKeyValueMapIterator.hasNext())
        {
        	Integer misLogIndex = overwrittenMisLogKeyValueMapIterator.next();
        	attrs[misLogIndex] = overwrittenMisLogKeyValueMap.get(misLogIndex);
        }
        
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<attrs.length-1; i++)
        {
            sb.append(filterSpecialChars(attrs[i])).append(delimiter);
        }
        sb.append(attrs[attrs.length-1]);
        
        return sb.toString();
    }

}
