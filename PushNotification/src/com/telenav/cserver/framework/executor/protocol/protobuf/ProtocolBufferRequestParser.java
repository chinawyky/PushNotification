package com.telenav.cserver.framework.executor.protocol.protobuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Category;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.cli.CliThreadLocalUtil;
import com.telenav.cserver.framework.executor.ExcutorEncryptionException;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory;
import com.telenav.cserver.framework.util.DeviceCarrierMapping;
import com.telenav.cserver.framework.util.EncryptionUtil;
import com.telenav.j2me.datatypes.GpsData;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoGpsFix;
import com.telenav.j2me.framework.protocol.ProtoGpsList;
import com.telenav.j2me.framework.protocol.ProtoUserProfile;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;
import com.telenav.j2me.framework.util.ToStringUtils;

public class ProtocolBufferRequestParser implements ProtocolRequestParser {

	static Category logger = Category.getInstance(ProtocolBufferRequestParser.class);
	
	public static final String PROFILE = "profile";
	
	public static final String GPS = "gps";
	
	@Override
	public ExecutorRequest[] parse(Object object) throws ExecutorException 
	{
		byte[] bytes = (byte[])object;
	    logger.info("Client request bytes : " + bytes.length);	
		ProtocolBuffer[] pBuf = ProtocolBufferUtil.toBufArray(bytes);
		if(pBuf == null)
		{
			return null;
		}
        ExecutorRequest[] requests = null;
        UserProfile userProfile = new UserProfile();
        List<GpsData> gpsData = null;//Double check
        
        String type = null;
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < pBuf.length; i++)
        {
            type = pBuf[i].getObjType();
            
            if(!PROFILE.equalsIgnoreCase(type) && !GPS.equalsIgnoreCase(type))
            {
                builder.append(type).append(",");
            }
        }
        
        if(builder.toString().endsWith(","))
        {
            builder.deleteCharAt(builder.lastIndexOf(","));
        }
        
        CliThreadLocalUtil.setSingleExecutorType(builder.toString());
		
        List<ExecutorRequest> requestList = new ArrayList<ExecutorRequest>(1);
        for(int i = 0; i < pBuf.length; i++)
        {
        	ProtocolBuffer buffer = pBuf[i];
        	if(logger.isDebugEnabled())
        	{
        		logger.debug(" each buffer : " + buffer.toString());
        	}
        	String objType = buffer.getObjType();
        	if(logger.isDebugEnabled())
        	{
        		logger.debug("Create Request parser Type : " + objType);
        	}
            if(objType.equalsIgnoreCase(PROFILE))
            {
                userProfile = createUserProfile(buffer.getBufferData());
            }
            else if(objType.equalsIgnoreCase(GPS))
            {
            	gpsData = createGpsData(buffer.getBufferData());
            }
            else
            {
				//this is a business request data
				ProtocolRequestParser parser =  createProtocolRequestParser("Proto_" + objType);
				ExecutorRequest[] executorRequests = parser.parse(buffer);//TODO : add new function called parseProtoBuffer
				for(ExecutorRequest req: executorRequests)
				{
					req.setExecutorType(objType);
					requestList.add(req);
				}
            }
        }
        
		for(ExecutorRequest ar: requestList)
		{
			ar.setUserProfile(userProfile);
			ar.setGpsData(gpsData);
		}
		requests = new ExecutorRequest[requestList.size()];
		requestList.toArray(requests);
   
		return requests;
	}
	
	protected  ProtocolRequestParser createProtocolRequestParser( String objectType )
	{
			return ExecutorDataFactory.getInstance().createProtocolRequestParser(objectType);
	}
	
	private List<GpsData> createGpsData(byte[] binData) 
	{
		List<GpsData> gpsData = new ArrayList<GpsData>();
		ProtoGpsList pGpsList = null;
		if(binData != null && binData.length > 0)
		{
			try
			{
				pGpsList = ProtoGpsList.parseFrom(binData);
				if(logger.isDebugEnabled())
                {
                    logger.debug(ToStringUtils.toString(pGpsList));
                }
			}
			catch(IOException ex)
			{
				logger.fatal("Fail to build ProtoGpsList");
			}
		}
		if(pGpsList != null)
		{
			Vector v = pGpsList.getGpsfix();
			if(v != null && v.size() > 0)
			{
				for(int i = 0; i < v.size(); i++)
				{
                   ProtoGpsFix pGpsFix = (ProtoGpsFix)v.get(i);
                   if(pGpsFix != null)
                   {
                	   gpsData.add(convert2GpsData(pGpsFix));
                   }
				}
			}
			else
			{
				logger.fatal("gps list vector is null or size is 0");
			}
		}
		else
		{
			logger.fatal("gps list is null");
		}		
		return gpsData;
	}
	
	static public GpsData convert2GpsData(ProtoGpsFix pGpsFix)
	{
		GpsData d = new GpsData();
		d.timeTag = pGpsFix.getTimeTag();
		if (d.timeTag > 0)
		{
			d.lat = pGpsFix.getLatitude();
			d.lon = pGpsFix.getLontitude();
			d.speed = pGpsFix.getSpeed();
			d.heading = pGpsFix.getHeading();
			d.type = (byte)pGpsFix.getType();
			d.errSize = pGpsFix.getErrorSize();
			
			//convert speed and heading to velocity
			d.speedAndHeadingToVelocity();
			d.isValid = true;
		}
		else
		{
			d.isValid = false;
		}
		
		return d;
	}
	
	public UserProfile createUserProfile(byte[] binData) throws ExecutorException 
	{
		ProtoUserProfile pUserProfile = null;
    	if(binData != null && binData.length > 0)
    	{
    		try
    		{
    			pUserProfile = ProtoUserProfile.parseFrom(binData);
                if(logger.isDebugEnabled())
    			{
                    logger.debug(ToStringUtils.toString(pUserProfile));
    			}
    		}
    		catch(IOException ex)
    		{
    			logger.fatal("Failed to build UserProfile");
    		}
    	}
    	else
    	{
    		logger.fatal("Empty profile binData");
    		return null;
    	}
    	if(pUserProfile == null)
    	{
    		logger.fatal("user profile is null");
    		return null;
    	}
    	
		UserProfile userProfile = new UserProfile();
		//USER_INFO
		userProfile.setPassword(pUserProfile.getPassword());
		userProfile.setEqPin(pUserProfile.getEqpin());
		userProfile.setLocale(pUserProfile.getLocale());
		userProfile.setRegion(pUserProfile.getRegion());
		userProfile.setSsoToken(pUserProfile.getSsoToken());
		
		//TODO : need take care
		userProfile.setGuideTone(pUserProfile.getGuideTone());//getGuideToneName(pUserProfile.getGuideTone()));
		//CLIENT_VERSION
		userProfile.setProgramCode(pUserProfile.getProgramCode());
		userProfile.setPlatform(pUserProfile.getPlatform());
		
		
		String version = pUserProfile.getVersion();
        if (version != null && version.indexOf("#") > 0)
        {
            userProfile.setChannelInfo(version.substring(version.indexOf("#")+1));
            userProfile.setVersion(version.substring(0,version.indexOf("#")));    
        }else
        {
            userProfile.setVersion(version);    
        }
		userProfile.setDevice(pUserProfile.getDevice());
		userProfile.setBuildNumber(pUserProfile.getBuildNumber());
		userProfile.setGpsType(pUserProfile.getGpsType());
		userProfile.setProduct(pUserProfile.getProduct());
		userProfile.setCarrier(getCarrier(pUserProfile.getDeviceCarrier(), pUserProfile.getProgramCode()));
		userProfile.setDeviceCarrier(pUserProfile.getDeviceCarrier());
		userProfile.setDeviceID(pUserProfile.getDeviceID());
		userProfile.setMacID(pUserProfile.getMacID());
		userProfile.setVendorId(pUserProfile.getVendorId());
		
		//USER_PREFS
		userProfile.setAudioFormat(pUserProfile.getAudioFormat());
		userProfile.setImageType(pUserProfile.getImageType());
		userProfile.setAudioLevel(pUserProfile.getAudioLevel());
		userProfile.setDataProcessType(pUserProfile.getDataProcessType());
		userProfile.setScreenWidth(pUserProfile.getScreenWidth());
		userProfile.setScreenHeight(pUserProfile.getScreenHeight());
		
		//Credential
		userProfile.setCredentialType(pUserProfile.getCredentialType());
		userProfile.setCredentialValue(pUserProfile.getCredentialValue());
		userProfile.setCredentialPassword(pUserProfile.getCredentialPassword());
		
		userProfile.setOpenUDID(pUserProfile.getOpenUDID());
		
		logPtnSource(pUserProfile);
		
		handleDecryptionIssue(userProfile, pUserProfile);
		
		return userProfile;
	}
	
	/**
     * @param deviceCarrier, programCode
     * @return
     */
    private String getCarrier(String deviceCarrier, String programCode)
    {
        String carrier = DeviceCarrierMapping.getCarrier(deviceCarrier);
        if (carrier != null)
            return carrier;
        
        carrier = DeviceCarrierMapping.getCarrier(programCode);
        if (carrier != null)
            return carrier;
        else
            return programCode;
    }

    protected void logPtnSource(ProtoUserProfile pUserProfile)
	{
	    logger.fatal("(0-dim,1-sim card,2-user input) ptn "+pUserProfile.getMin()+" is from "+pUserProfile.getPtnSource());
	}
	
    /**
     * throw ExcutorEncryptionException if fail to decode encrypted PTN
     * @param userProfile
     * @param pUserProfile
     * @throws ExcutorEncryptionException
     */
	protected void handleDecryptionIssue(UserProfile userProfile,ProtoUserProfile pUserProfile) throws ExcutorEncryptionException
	{
		userProfile.setPtnSource(pUserProfile.getPtnSource());
		try
        {
		    userProfile.setUserId(EncryptionUtil.decryptByPtnSource(pUserProfile.getUserId(),pUserProfile.getPtnSource()));
	        userProfile.setMin(EncryptionUtil.decryptByPtnSource(pUserProfile.getMin(),pUserProfile.getPtnSource()));
	        userProfile.setCredentialID(EncryptionUtil.decryptByPtnSource(pUserProfile.getCredentialID(),pUserProfile.getPtnSource()));
        }
        catch (ExcutorEncryptionException e)
        {
            userProfile.setUserId(pUserProfile.getUserId());
            userProfile.setMin(pUserProfile.getMin());
            userProfile.setCredentialID(pUserProfile.getCredentialID());
            CliThreadLocalUtil.setSingleUserProfile(userProfile);
            throw new ExcutorEncryptionException(e);
        }
		
	}

}
