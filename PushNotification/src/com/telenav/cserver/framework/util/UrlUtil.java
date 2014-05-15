/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import com.telenav.cserver.framework.UserProfile;

/**
 * UrlUtil.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Oct 20, 2011
 *
 */
public class UrlUtil
{
    public static String[] getHostAndPort(String url){
        String afterSlashSlash = url.substring(url.indexOf("//")+2);
        String host = "";
        String port = "";
        if( afterSlashSlash.indexOf(":") != -1 ){
            host = afterSlashSlash.substring(0,afterSlashSlash.indexOf(":"));
            if( afterSlashSlash.indexOf("/") != -1 ){
                port = afterSlashSlash.substring(afterSlashSlash.indexOf(":")+1,afterSlashSlash.indexOf("/"));
            }else{
                port = afterSlashSlash.substring(afterSlashSlash.indexOf(":")+1);
            }
        }else if( afterSlashSlash.indexOf("/") != -1 ){
            host = afterSlashSlash.substring(0,afterSlashSlash.indexOf("/"));
            port = "80";
        }else{
            host = afterSlashSlash.substring(0);
            port = "80";
        }
        return new String[]{host,port};
    }

    
    public static String appendClientInfo(String url, UserProfile clientInfo)
    {
        if (clientInfo == null)
            return url;
        
        //do not need append client info for 6x
        if (clientInfo.getVersion().startsWith("6"))
            return url;
            
        if (url!= null)
        {
            if (url.endsWith(".do"))
            {
                url = url + "?" + JSONUtil.getClientJson(clientInfo);
            }
            else if (url.indexOf(".do?") > 0)
            {
                url = url + "&" + JSONUtil.getClientJson(clientInfo);
            }
        }
        return url;
    }
    
    public static String getUrlString(String s)
    {
       String temp =s;
       if(temp!=null){
	       temp = temp.replaceAll("%", "%25");
	       temp = temp.replaceAll("&", "%26");
	       temp = temp.replaceAll("=", "%3D");
	       temp = temp.replaceAll(" ", "%20");
	       temp = temp.replaceAll("#", "%23");
       }
       return temp;
    }
}
