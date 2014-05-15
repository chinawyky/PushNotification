/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.dns;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.telenav.cserver.common.resource.orders.VersionLoadOrder;
import com.telenav.cserver.framework.UserProfile;

/**
 * DNSPropertiesCache.java
 * @author njiang
 * @version 1.0 2012-8-30
 */
public class DNSPropertiesCache {
	
	private static final Logger LOGGER = Logger.getLogger(DNSPropertiesCache.class);
	
	//save different type properties file value. Like: service_locator, web_service and etc.
	private static final Map<String, Map<String, Properties>> PROPMAP = new HashMap<String, Map<String, Properties>>();
	
	private static final ReadWriteLock RWLOCK = new ReentrantReadWriteLock();
	
	private static final Lock READLOCK = RWLOCK.readLock();
	
	private static final Lock WRITELOCK = RWLOCK.writeLock();
	
	private static DNSPropertiesCache instance = new DNSPropertiesCache();
	
	private static final String UNDERLINE = "_";
	
	private static final String DEFAULT = "default";
	
	//properties file's folder name
	private static final String DNS = "cserver_dns";
	
//	private static final String FILEPATH = ClassLoader.getSystemResource(DNS).getPath();
	private static final String FILEPATH = Thread.currentThread().getContextClassLoader().getResource(DNS).getPath();
	
//	private static Pattern propPattern = Pattern.compile(".*?DNS_?(.*?)\\.properties");
	
	static{
		initPropertiesFile();
	}

	public Properties getPropertiesFromCache(final UserProfile profile, final String type){
		String key = "";
		Properties prop = null;
		if (profile != null && profile.getProgramCode() != null
			&& profile.getPlatform() != null && profile.getVersion() != null)
		{
			key = profile.getProgramCode() + UNDERLINE
					+ profile.getPlatform() + UNDERLINE
					+ profile.getVersion().replaceAll("\\.", UNDERLINE);

			prop = getPropertiesWithKey(key, profile, type);
			if (prop == null)
			{
				VersionLoadOrder versionLoadOrder = new VersionLoadOrder();
				key = profile.getProgramCode() + UNDERLINE
						 + profile.getPlatform() + UNDERLINE
						 + versionLoadOrder.getMajorVersion(profile);
				prop = getPropertiesWithKey(key, profile, type);
			}

			if (prop != null)
			{
				return prop;
			}		
		}
		return getProp(DEFAULT, type);
	}
	
	private Properties getPropertiesWithKey(final String key, final UserProfile profile, final String type)
	{
		Properties returnProp = null;		
		Properties prop = getProp(key + UNDERLINE + profile.getDevice(), type);
		if (prop != null) {
			returnProp = prop;
		} else {
			prop = getProp(key + UNDERLINE + DEFAULT, type);
			if(prop != null) {
				returnProp = prop;
			}
		}
		return returnProp;
		
	}
	
	public static void initPropertiesFile(){
		File path = new File(FILEPATH);
		String type = "";
		Map<String, Map<String, Properties>> propMapCandidate = new HashMap<String, Map<String, Properties>>();
		Map<String, Properties> map = null;
		
		for(File folder : path.listFiles()){
			if(folder.isDirectory()){
				type = folder.getName();
				map = loadPropFileToCache(FILEPATH, type);
				propMapCandidate.put(type, map);
			}
		}
		
		try
		{
			WRITELOCK.lock();
			PROPMAP.clear();
			PROPMAP.putAll(propMapCandidate);
		}finally
		{
			WRITELOCK.unlock();
		}		
		
	}
	
	private static Map<String, Properties> loadPropFileToCache(final String path, final String type){
		File filePath = new File(path + File.separator + type);
		
		String fileName = "";
		String key = "";
		Properties prop = null;
		Map<String, Properties> map = new HashMap<String, Properties>();
		FileInputStream stream = null;
		int beginIndex;
		int endIndex;
		
		for(File propFile : filePath.listFiles()){
			fileName = propFile.getName();
			if(propFile.isFile() && fileName.contains(type)){
				prop = new Properties();
				try {
					stream = new FileInputStream(propFile);
					prop.load(stream);
				} catch (IOException e) {
					LOGGER.fatal("Load file fail: ", e);
				} finally{
					try {
						if(stream != null){
							stream.close();
						}
					} catch (IOException e) {
						LOGGER.fatal("Close file fail: ", e);
					}
				}
				
				key = DEFAULT;
				beginIndex = (type + "_DNS_").length();
				endIndex = fileName.indexOf(".properties");
				if(endIndex > beginIndex){
					key = fileName.substring(beginIndex, endIndex);
				}
					
				map.put(key, prop);
			}
		}
		
		return map;
		
	}
	
	private Properties getProp(final String key, final String type){
		try
		{
			READLOCK.lock();
			Properties prop = null;
			if(PROPMAP.containsKey(type) && PROPMAP.get(type).containsKey(key)){
				prop = (Properties)PROPMAP.get(type).get(key);
				return prop;
			}
			return prop;
		}finally
		{
			READLOCK.unlock();
		}
	}
	
	public static DNSPropertiesCache getInstance(){
		return instance;
	}

}
