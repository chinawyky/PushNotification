/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx;

import java.util.List;


/**
 * IBackendServerMonitor interface, which define all the backend server monitor
 * api
 * 
 * @author kwwang
 * 
 */
public interface IBackendServerMonitor {
    
    public List<BackendServerConfiguration> fetchBackendServers();
    
    public DetectResult monitor(BackendServerConfiguration backendServer);
    
    public List<DetectResult> monitorAll();
    
    public String fetchCServerBuildVersion();
}
