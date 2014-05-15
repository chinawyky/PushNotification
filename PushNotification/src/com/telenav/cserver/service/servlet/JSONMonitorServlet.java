/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.service.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.framework.management.jmx.BackendServerConfiguration;
import com.telenav.cserver.framework.management.jmx.DetectResult;
import com.telenav.cserver.framework.management.jmx.IBackendServerMonitor;


/**
 * MonitorServlet.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Sep 2, 2011
 *
 */
public class JSONMonitorServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        MonitorResponse response = new MonitorResponse();
        response.status = MonitorResponse.SUCCESS;
        
        IBackendServerMonitor backendMonitor = ResourceFactory.getInstance().getBackendServerMonitor();
        OutputStream writer = resp.getOutputStream();

        if( backendMonitor == null ){
            response.status = MonitorResponse.FAIL;
            response.errorMsg = "can't find backend monitor bean.";
            writer.write(response.toJSON().getBytes());
            writer.flush();
            return;
        }
       
        String action = req.getParameter("action");
        if( "fetchBackendServers".equals(action) )
        {
            List<BackendServerConfiguration> backendServices = backendMonitor.fetchBackendServers();
            response.reponse = list2JSON(backendServices);
        }
        else if( "fetchCServerBuildVersion".equals(action) )
        {
            String version = backendMonitor.fetchCServerBuildVersion();
            response.reponse = "\"" + (version == null ? "" : version) + "\"";
        }
        else if( "monitor".equals(action) )
        {
            String backendServiceName = req.getParameter("backendService");
            BackendServerConfiguration backendServiceCong = new BackendServerConfiguration();
            backendServiceCong.setName(backendServiceName);
            DetectResult result = backendMonitor.monitor(backendServiceCong);
           
            response.reponse = result.toJSON();
        }
        else if( "monitorAll".equals(action) ){
            List<DetectResult> results = backendMonitor.monitorAll();
            response.reponse = list2JSON(results);
        }
        else
        {
            response.status = MonitorResponse.FAIL;
            response.errorMsg = "unsupported operation!";
        }
       
        writer.write(response.toJSON().getBytes());
        writer.flush();
    }
    
    private static class MonitorResponse{
        public static final int SUCCESS = 0;
        public static final int FAIL = 1;
        int status = 0;  //0 is success
        String errorMsg = "";
        String reponse;
        
        public String toJSON(){
            StringBuffer jsonResponse = new StringBuffer("{");
            jsonResponse.append("\"status\": ").append("\"").append(status).append("\", ")
                        .append("\"errorMsg\": ").append("\"").append(errorMsg).append("\", ")
                        .append("\"response\": ").append(reponse)
                        .append("}");
            return jsonResponse.toString();
        }
    }
    
    private String list2JSON(List<?> list){
        StringBuffer json = new StringBuffer("[");
        try{
            for(Object obj : list){
                
                String objJSON = (String)obj.getClass().getMethod("toJSON", new Class[]{}).invoke(obj, new Object[]{});
                json.append(objJSON).append(",");
            }
            
            if( json.charAt(json.length()-1) == ',' ){
                json.deleteCharAt(json.length()-1);
            }
            json.append("]");
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
        return json.toString();
    }
    
    
}
