/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl;

import java.util.List;

import com.telenav.cserver.framework.executor.Interceptor;

/**
 * InterceptorEntry.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public class InterceptorConfigurationItem 
{
	String type;
	List<Interceptor> preInterceptors;
	List<Interceptor> postInterceptors;
	
	/**
	 * @return the postInterceptors
	 */
	public List<Interceptor> getPostInterceptors() {
		return postInterceptors;
	}
	/**
	 * @param postInterceptors the postInterceptors to set
	 */
	public void setPostInterceptors(List<Interceptor> postInterceptors) {
		this.postInterceptors = postInterceptors;
	}
	/**
	 * @return the preInterceptors
	 */
	public List<Interceptor> getPreInterceptors() {
		return preInterceptors;
	}
	/**
	 * @param preInterceptors the preInterceptors to set
	 */
	public void setPreInterceptors(List<Interceptor> preInterceptors) {
		this.preInterceptors = preInterceptors;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
