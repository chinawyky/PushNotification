/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.config;

/**
 * 
 * web service config for image service<br>
 * add a mapTileVersion field
 *
 * @author mmwang
 * @version 1.0 2010-7-20
 * 
 */
public class ImageServiceConfig extends SimpleServiceConfig
{
	
	private String mapTileVersion;

	/**
	 * @return the mapTileVersion
	 */
	public String getMapTileVersion()
	{
		return mapTileVersion;
	}

	/**
	 * @param mapTileVersion the mapTileVersion to set
	 */
	public void setMapTileVersion(String mapTileVersion)
	{
		this.mapTileVersion = mapTileVersion;
	}
}
