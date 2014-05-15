package com.telenav.cserver.framework.executor.protocol.json;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Category;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory;
import com.telenav.cserver.framework.util.JSONUtil;
import com.telenav.j2me.datatypes.GpsData;

public class JsonRequestParser implements ProtocolRequestParser 
{
    static Category logger = Category.getInstance(JsonRequestParser.class);
	@Override
	public ExecutorRequest[] parse(Object object) throws ExecutorException
	{
		byte[] bytes = (byte[])object;
		logger.info("Client request bytes : " + bytes.length);
		try
		{
			String str = new String(bytes);
			JSONObject jsonObj = new JSONObject(str);
			JSONObject[] jsonArray = JSONUtil.getJSONArray(jsonObj, "JSONRequest");//TODO : remove hardcode
			if(jsonArray == null)
			{
				return null;
			}
			ExecutorRequest[] requests = null;
			UserProfile userProfile = new UserProfile();
			List<GpsData> gpsData = null;
			
			List<ExecutorRequest> requestList = new ArrayList<ExecutorRequest>(1);
			for(int i = 0; i < jsonArray.length; i++)
			{
				JSONObject obj = jsonArray[i];
				Enumeration en = obj.keys();
				while(en.hasMoreElements())
				{
					String key = (String)en.nextElement();
					if(logger.isDebugEnabled())
					{
						logger.debug("object key : " + key);
					}
					if(key.equalsIgnoreCase("profile"))
					{
						userProfile = this.createUserProfile(JSONUtil.getJSONOBject(obj, key));
					}
					else if(key.equalsIgnoreCase("gps"))
					{
						gpsData = this.createGpsData(JSONUtil.getJSONOBject(obj, key));
					}
					else
					{
						ProtocolRequestParser parser = 
							ExecutorDataFactory.getInstance().createProtocolRequestParser(key);
						ExecutorRequest[] executorRequests = parser.parse(JSONUtil.getJSONOBject(obj, key));
						for(ExecutorRequest req : executorRequests)
						{
							req.setExecutorType(key);
							requestList.add(req);
						}
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
		catch(Exception e)
		{
			logger.fatal("Error in construct json object " + e.getMessage());	
		}
		return null;
	}
	
	//{\"Min\":\"xxx\",\"Password\":\"xxx\", ...}
	private UserProfile createUserProfile(Object obj) throws Exception
	{
		//TODO : Need remove hard coding 
		if(obj != null && obj instanceof JSONObject)
		{
            JSONObject object = (JSONObject)obj;
			UserProfile userProfile = new UserProfile();
			userProfile.setMin(JSONUtil.getJSONString(object, "Min"));
			userProfile.setPassword(JSONUtil.getJSONString(object, "Password"));
			userProfile.setUserId(JSONUtil.getJSONString(object, "UserId"));
			userProfile.setEqPin(JSONUtil.getJSONString(object, "Eqpin"));
			userProfile.setLocale(JSONUtil.getJSONString(object, "Locale"));
			userProfile.setRegion(JSONUtil.getJSONString(object, "Region"));
			userProfile.setSsoToken(JSONUtil.getJSONString(object, "SsoToken"));
			userProfile.setGuideTone(JSONUtil.getJSONString(object, "GuideTone"));
			userProfile.setCarrier(JSONUtil.getJSONString(object, "Carrier"));
			userProfile.setDeviceCarrier(JSONUtil.getJSONString(object, "Carrier")); /*Keep consistent with ProtoBuf parser, use carrier to set device carrier*/
			userProfile.setPlatform(JSONUtil.getJSONString(object, "Platform"));
			userProfile.setVersion(JSONUtil.getJSONString(object, "Version"));
			userProfile.setDevice(JSONUtil.getJSONString(object, "Device"));
			userProfile.setBuildNumber(JSONUtil.getJSONString(object, "BuildNumber"));
			userProfile.setGpsType(JSONUtil.getJSONString(object, "GpsType"));
			userProfile.setProduct(JSONUtil.getJSONString(object, "Product"));
			userProfile.setAudioFormat(JSONUtil.getJSONString(object, "AudioFormat"));
			userProfile.setImageType(JSONUtil.getJSONString(object, "ImageType"));
			userProfile.setAudioLevel(JSONUtil.getJSONString(object, "AudioLevel"));
			userProfile.setDataProcessType(JSONUtil.getJSONString(object, "DataProcess"));
			userProfile.setScreenWidth(JSONUtil.getJSONString(object, "ScreenWidth"));
			userProfile.setScreenHeight(JSONUtil.getJSONString(object, "ScreenHeight"));
			return userProfile;
		}
		else
		{
		    throw new Exception("Failed to parse UserProfile");	
		}
	}

	//[{\"TimeTag\":\"xxx\",\"Lat\":\"xxx\",\"Lon\":\"xxxx\",..},{\"TimeTag\":\"xxx\",\"Latitude\":\"xxx\",\"Lontitude\":\"xxxx\",..},{}]
	private List<GpsData> createGpsData(Object object) throws Exception
	{
		List<GpsData> gpsData = new ArrayList<GpsData>();
		if(object != null && object instanceof JSONArray)
		{
			JSONArray array = (JSONArray)object;
			if(array.length() > 0)
			{
				for(int i = 0; i < array.length(); i++)
				{
					JSONObject obj = array.getJSONObject(i);
					if(obj != null)
					{
						gpsData.add(convert2GpsData(obj));
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
			throw new Exception("Failed to parse GPSData");	
		}
		return gpsData;
	}
	
	private static GpsData convert2GpsData(Object gpsFix)  throws Exception
	{
		//TODO remove hard coding
		if(gpsFix != null && gpsFix instanceof JSONObject)
		{
			JSONObject fix = (JSONObject)gpsFix;
			GpsData d = new GpsData(); 
            d.timeTag = Long.parseLong(JSONUtil.getJSONString(fix,"TimeTag"));
            if(d.timeTag > 0)
            {
            	d.lat = Integer.parseInt(JSONUtil.getJSONString(fix,"Lat"));
            	d.lon = Integer.parseInt(JSONUtil.getJSONString(fix,"Lon"));
            	d.speed = Integer.parseInt(JSONUtil.getJSONString(fix,"Speed"));
            	d.heading = Integer.parseInt(JSONUtil.getJSONString(fix,"Heading"));
            	d.type = (byte)Integer.parseInt(JSONUtil.getJSONString(fix,"Type"));
            	d.errSize = Integer.parseInt(JSONUtil.getJSONString(fix,"ErrorSize"));
            	d.speedAndHeadingToVelocity();
            	d.isValid = true;
            }
            else
            {
            	d.isValid = false;
            }
			return d;
		}
		else
		{
            throw new Exception("Failed to parse Gps Fix");
		}
	}
}
