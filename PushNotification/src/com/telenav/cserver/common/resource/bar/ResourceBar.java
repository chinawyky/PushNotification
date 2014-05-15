/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.bar;


/**
 * ResourceBar.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-7
 *
 */
public class ResourceBar 
{
	private String type;
	private String version;
	private byte[] data;
	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
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
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String toString()
	{
		return "type:" + type + ",version:" + version 
					          + ",binary size:" + (data == null? 0: data.length);
	}
	
}