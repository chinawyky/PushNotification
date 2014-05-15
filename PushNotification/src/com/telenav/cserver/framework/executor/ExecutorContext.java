/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import java.util.HashMap;
import java.util.Map;

import com.telenav.cserver.framework.executor.protocol.ProtocolHandler;
import com.telenav.cserver.framework.transportation.Transportor;
import com.telenav.kernel.util.datatypes.TnContext;


/**
 * Executor Context
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public class ExecutorContext 
{

    public static final String REQUEST_IP = "REQUEST_IP";
    
	private Transportor transportor;
	
	private ProtocolHandler protocolHandler;
	
	private TnContext tnContext = new TnContext();

	private long timestamp;
	
	private String serverUrl;
	
	/**
	 * others
	 */
	private Map<String, Object> attributes = new HashMap<String, Object>();

	
	/**
	 * get attribute from the map
	 * 
	 * @param key
	 * @return
	 */
	public String getAttribute(String key) {
		return (String) attributes.get(key);
	}
	
	public Object getObject(String key) {
        return attributes.get(key);
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

	public Transportor getTransportor()
	{
		return transportor;
	}
	
	public void setTransportor(Transportor transportor)
	{
		this.transportor = transportor;
	}
	
	
	 /**
     * @return Returns the timestamp.
     */
    public long getTimestamp()
    {
    	return timestamp;
    }

    /**
     * @param timestamp
     *            The timestamp to set.
     */
    public void setTimestamp(long timestamp)
    {
    	this.timestamp = timestamp;
    }

	/**
	 * @return the protocolHandler
	 */
	public ProtocolHandler getProtocolHandler() {
		return protocolHandler;
	}

	/**
	 * @param protocolHandler the protocolHandler to set
	 */
	public void setProtocolHandler(ProtocolHandler protocolHandler) {
		this.protocolHandler = protocolHandler;
	}

	/**
	 * @return the url
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * @param url the url to set
	 */
	public void setServerUrl(String url) {
		this.serverUrl = url;
	}
}
