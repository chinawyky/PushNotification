/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * UserProfile.java
 * 
 * This class is to hold the user profile, 
 * including but not limit to carrier/platform/version/device/product/identity.
 * 
 * It is an upgrade of UserInfoWrapper.java in TN 5.x
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-9
 *
 */
public class UserProfile 
{
	
	public final static String DEFAULT_ACCOUNT_TYPE = "normal";
	
	public final static String DEFAULT_LOCALE="en_US";
	
	public final static int PTN_SOURCE_FROM_TELENAV_CSERVER=0;
	public final static int PTN_SOURCE_FROM_SIM_CARD=1;
	public final static int PTN_SOURCE_FROM_USER_INPUT=2;
	
	/**
	 * user input
	 */
	private String min;
	private String userId;
	private String password;
	private String eqPin;
	private String locale;
	private String region;
	private String ssoToken;
	private String guideTone;
	private String deviceID;
	private String macID;
	private String openUDID;
	private String vendorId;

	private String credentialType;
	private String credentialID;
	private String credentialValue;
	private String credentialPassword;
	
	/**
	 * client specific
	 */
	private String carrier;
	private String product;
	private String platform;
	private String device;
	private String version;
	private String buildNumber;
	private String gpsType;
	private String mapDpi;
	private String OSVersion;
	
	private String channelInfo;//for cn market use
	/**
     * @return the channelInfo
     */
    public String getChannelInfo()
    {
        return channelInfo;
    }
    /**
     * @param channelInfo the channelInfo to set
     */
    public void setChannelInfo(String channelInfo)
    {
        this.channelInfo = channelInfo;
    }

    //this value would be read from client device, might not be reliable.
	private String deviceCarrier = "";
	private String programCode; 
	/**
	 * user prefs
	 */
	private String audioFormat;
	private String imageType;
	private String audioLevel;
	
	private int ptnSource;
	
	public int getPtnSource()
    {
        return ptnSource;
    }
    public void setPtnSource(int ptnSource)
    {
        this.ptnSource = ptnSource;
    }

    /**
	 * others
	 */
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	
	public static final String ARRT_RESOLUTION = "ARRT_RESOLUTION";

	/**
	 * @return the buildNumber
	 */
	public String getBuildNumber() 
	{
		return buildNumber;
	}
	/**
	 * @param buildNumber the buildNumber to set
	 */
	public void setBuildNumber(String buildNumber) 
	{
		this.buildNumber = buildNumber;
	}
	/**
	 * @return the carrier
	 */
	public String getCarrier() 
	{
		return carrier;
	}
	/**
	 * @param carrier the carrier to set
	 */
	public void setCarrier(String carrier) 
	{
		this.carrier = carrier;
	}
	/**
	 * @return the device
	 */
	public String getDevice() 
	{
		return device;
	}
	/**
	 * @param device the device to set
	 */
	public void setDevice(String device) 
	{
		this.device = device;
	}
	/**
	 * @return the eqPin
	 */
	public String getEqPin() 
	{
		return eqPin;
	}
	/**
	 * @param eqPin the eqPin to set
	 */
	public void setEqPin(String eqPin) 
	{
		this.eqPin = eqPin;
	}
	/**
	 * @return the gpsType
	 */
	public String getGpsType() 
	{
		return gpsType;
	}
	/**
	 * @param gpsType the gpsType to set
	 */
	public void setGpsType(String gpsType) 
	{
		this.gpsType = gpsType;
	}
	/**
	 * @return the locale
	 */
	public String getLocale() 
	{
		if(locale==null||(locale!=null&&locale.trim().length()==0))
			return DEFAULT_LOCALE;
		return locale;
	}
	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) 
	{
		this.locale = locale;
	}
	/**
	 * @return the min
	 */
	public String getMin() 
	{
		return min;
	}
	/**
	 * @param min the min to set
	 */
	public void setMin(String min) 
	{
		this.min = min;
	}
	/**
	 * @return the password
	 */
	public String getPassword() 
	{
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) 
	{
		this.password = password;
	}
	/**
	 * @return the platform
	 */
	public String getPlatform() 
	{
		return platform;
	}
	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) 
	{
		this.platform = platform;
	}
	/**
	 * @return the product
	 */
	public String getProduct() 
	{
		return product;
	}
	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) 
	{
		this.product = product;
	}
	/**
	 * @return the region
	 */
	public String getRegion() 
	{
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) 
	{
		this.region = region;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() 
	{
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) 
	{
		this.userId = userId;
	}
	/**
	 * @return the version
	 */
	public String getVersion() 
	{
	    if(version != null && version.startsWith("7.1."))
	    {
	        return "7.1";
	    }else if(version != null && version.startsWith("7_1_"))
	    {
	        return "7_1";
	    }
		return version;
	}
	
	public String getOriginalVersion() 
	{
        return version;
    }
	
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) 
	{
		this.version = version;
	}
	
	
	/**
	 * @return the ssoToken
	 */
	public String getSsoToken() 
	{
		return ssoToken;
	}
	/**
	 * @param ssoToken the ssoToken to set
	 */
	public void setSsoToken(String ssoToken) 
	{
		this.ssoToken = ssoToken;
	}	
	
	/**
	 * @return the guideTone
	 */
	public String getGuideTone() 
	{
		return guideTone;
	}
	
	/**
	 * @param guideTone the guideTone to set
	 */
	public void setGuideTone(String guideTone) 
	{
		this.guideTone = guideTone;
	}
	
	public Object getAttribute(String key) 
	{
		return attributes.get(key);
	}

	public void setAttribute(String key, Object value) 
	{
		attributes.put(key, value);
	}

	/**
	 * @param audioFormat the audioFormat to set
	 */
	public void setAudioFormat(String audioFormat)
	{
		this.audioFormat = audioFormat;
	}
	/**
	 * @return the audioFormat
	 */
	public String getAudioFormat()
	{
		return audioFormat;
	}
	/**
	 * @param imageType the imageType to set
	 */
	public void setImageType(String imageType)
	{
		this.imageType = imageType;
	}
	/**
	 * @return the imageType
	 */
	public String getImageType()
	{
		return imageType;
	}
	/**
	 * @param audioLevel the audioLevel to set
	 */
	public void setAudioLevel(String audioLevel)
	{
		this.audioLevel = audioLevel;
	}
	/**
	 * @return the audioLevel
	 */
	public String getAudioLevel()
	{
		return audioLevel;
	}
	
	String dataProcessType;
	
	/**
	 * @return
	 */
	public String getDataProcessType() 
	{
		return dataProcessType;
	}
	
	String screenWidth;
    
    /**
     * @return
     */
    public String getScreenWidth() 
    {
        return screenWidth;
    }
    
    
	/**
	 * @param screenWidth the screenWidth to set
	 */
	public void setScreenWidth(String screenWidth) 
	{
		this.screenWidth = screenWidth;
	}
    
    
    String screenHeight;
    
    /**
     * @param screenWidth the screenWidth to set
     */
    public void setScreenHeight(String screenHeight) 
    {
        this.screenHeight = screenHeight;
    }
    
    /**
     * @return
     */
    public String getScreenHeight() 
    {
        return screenHeight;
    }
    
    /**
     * @param transportorType the transportorType to set
     */
    public void setDataProcessType(String transportorType) 
    {
        this.dataProcessType = transportorType;
    }
    

	public String getMapDpi() 
	{
		return mapDpi;
	}
	
    /*
     * map dpi is the value to control using big font or not
     */
	public void setMapDpi(String mapDpi)
	{
		this.mapDpi = mapDpi;
	}
    
	public String getOSVersion()
    {
        return OSVersion;
    }
    public void setOSVersion(String version)
    {
        OSVersion = version;
    }
    
    public String getDeviceCarrier()
    {
        return deviceCarrier;
    }
    public void setDeviceCarrier(String deviceCarrier)
    {
        this.deviceCarrier = deviceCarrier;
    }
    public String getResolution()
    {
        String resolution = (String)attributes.get(ARRT_RESOLUTION);
        if (resolution == null)
        {
            genResolution();
        }
        resolution = (String)attributes.get(ARRT_RESOLUTION);
        return resolution;
    }
    
    public String getProgramCode()
    {
        return programCode;
    }
    public void setProgramCode(String programCode)
    {
        this.programCode = programCode;
    }
    
    public String getDeviceID() 
    {
		return deviceID;
	}
	public void setDeviceID(String deviceID) 
	{
		this.deviceID = deviceID;
	}
	
	public String getMacID() 
	{
		return macID;
	}
	public void setMacID(String macID) 
	{
		this.macID = macID;
	}

	public String getOpenUDID() 
	{
		return openUDID;
	}
	public void setOpenUDID(String openUDID) 
	{
		this.openUDID = openUDID;
	}	

	public String getVendorId() 
	{
		return vendorId;
	}

	public void setVendorId(String vendorId) 
	{
		this.vendorId = vendorId;
	}
	
	public String getCredentialType() 
	{
		return credentialType;
	}
	public void setCredentialType(String credentialType) 
	{
		this.credentialType = credentialType;
	}
	public String getCredentialID() 
	{
		return credentialID;
	}
	public void setCredentialID(String credentialID) 
	{
		this.credentialID = credentialID;
	}
	public String getCredentialValue() 
	{
		return credentialValue;
	}
	public void setCredentialValue(String credentialValue) 
	{
		this.credentialValue = credentialValue;
	}
	public String getCredentialPassword() 
	{
		return credentialPassword;
	}
	public void setCredentialPassword(String credentialPassword) 
	{
		this.credentialPassword = credentialPassword;
	}
    
    //get the resolution of the device
	//treat its return as default device name
	//if the device is dual screen, return width x height(one screen)_height x width(other screen), otherwise, return width x height
	//eg. 1280x752_800x1232 for dual screen, 480x800 for single screen
    private synchronized void genResolution()
    {
        if (attributes.get(ARRT_RESOLUTION) != null)
            return;
        
        String resolution = "";
        
        String screenWidth = getScreenWidth();// for dual screen, get the screen width and height for one screen(the format of the value is width-height), for single screen, get the screen width
        String screenHeight = getScreenHeight();//for dual screen, get the screen width and height for the other one screen, for single screen, get the screen height
        
        if (screenWidth != null && screenHeight != null)
        {
        	StringBuffer sb = new StringBuffer();
			if (screenWidth.contains("-")) {
				sb.append(screenWidth.replaceAll("-", "x"));
				sb.append("_");
			} else {
				sb.append(screenWidth);
				sb.append("x");
			}
			if (screenHeight.contains("-"))
				sb.append(screenHeight.replaceAll("-", "x"));
			else
				sb.append(screenHeight);
//            String[] widths = screenWidth.split("-");
//            String[] heights = screenHeight.split("-");
//            int count = Math.min(widths.length, heights.length);
//            for(int j=0; j<count; j++)
//            {
//                sb.append(widths[j]).append("x").append(heights[j]);
//                sb.append("_");
//            }
//            if (sb.toString().endsWith("_"))
//                sb.deleteCharAt(sb.lastIndexOf("_"));
            
            resolution = sb.toString();
        }
        
        attributes.put(ARRT_RESOLUTION, resolution);
        
    }
    
    public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("min=" + min).append(",password=" + password)
							   .append(",userid=" + userId)
							   .append(",credentialType=" + credentialType)
							   .append(",credentialID=" + credentialID)
							   .append(",credentialValue=" + credentialValue)
							   .append(",credentialPassword=" + credentialPassword)
							   .append(",locale=" + locale)
							   .append(",region=" + region)
							   .append(",guideTone=" + guideTone)
							   .append(",ssoToken=" + ssoToken)
							   .append("\r\ncarrier=" + carrier)
							   .append(",version=" + version)
							   .append(",platform=" + platform)
							   .append(",device=" + device)
							   .append(",product=" + product)
							   .append(",buildNumber=" + buildNumber)
							   .append(",gpsType=" + gpsType)
							   .append(",mapDpi="+mapDpi)
							   .append(",OSVersion="+OSVersion)
							   .append(",audioFormat=" + audioFormat)
							   .append(",imageType=" + imageType)
							   .append(",audioLevel=" + audioLevel)
							   .append(",transportorType=" + dataProcessType)
                               .append(",screenWidth=" + screenWidth)
                               .append(",screenHeight=" + screenHeight)
                               .append(",deviceCarrier=" + deviceCarrier)
		                       .append(",programCode=" + programCode);
		return sb.toString();
	}
	

}
