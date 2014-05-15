package com.telenav.cserver.framework.util;

import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.UserProfile;

public class JSONUtil 
{
    private static final Logger LOGGER = Logger.getLogger(JSONUtil.class);
    
	public static int getInt(String input, int defValue)
	{
		try
		{
			if(input != null && input.length() > 0)
			{
				return Integer.parseInt(input.trim());
			}
		}
		catch(Exception ex)
		{
			LOGGER.fatal("Error when getting value of: " + input, ex);
		}
		return defValue;
	}
	
	public static double getDouble(String input, double defValue)
	{
		try
		{
			if(input != null && input.length() > 0)
			{
				return Double.parseDouble(input);
			}
		}
		catch(Exception ex)
		{
			LOGGER.fatal("Error when getting value of: " + input, ex);
		}
		return defValue;
	}
	
	public static boolean getFlagBoolean(String input, boolean defValue)
	{
		try
		{
			if(input != null && input.equals("1"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception ex)
		{
			LOGGER.fatal("Error when getting value of: " + input, ex);
		}
		return defValue;
	}
	
	public static boolean getBoolean(String input, boolean defValue)
	{
		try
		{
			if(input != null && Boolean.getBoolean(input))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception ex)
		{
			LOGGER.fatal("Error when getting value of: " + input, ex);
		}
		return defValue;
	}
	
	public static Object getJSONOBject(JSONObject object, String key)
	{
		try
		{
			return object.get(key);
		}
		catch(JSONException ex)
		{
			return null;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static String getJSONString(JSONObject object, String key)
	{
		try
		{
		    return object.getString(key);
		}
		catch(JSONException ex)
		{
			return "";
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	public static JSONObject[] getJSONArray(JSONObject jsonObj, String key) throws Exception
	{
		try
		{
			Object obj = getJSONOBject(jsonObj, key);
			if(obj != null && obj instanceof JSONArray)
			{
				JSONArray jsonArray = (JSONArray)obj;
				JSONObject[] result = new JSONObject[jsonArray.length()];
				for(int i = 0; i < jsonArray.length(); i++)
				{
					JSONObject subObj = (JSONObject)jsonArray.get(i);
					result[i] = subObj;
				}
				return result;
			}
			else
			{
				throw new Exception("illegal json object when parsing Stage for audioTimingAssistance");
			}
		}
		catch(Exception ex)
		{
            throw new Exception("Failed to parse JSONString " + ex.getMessage());   
		}
	}
	
	public static String filterSpecial(String str){
	    if( str == null )
	        return "";
        return str.replaceAll("\"", "").replaceAll("(\r|\n)", " ");
    }
	
	public static String getClientJson(UserProfile profile)
    {
        JSONObject jo = new JSONObject();
        try
        {
            jo.put("programCode", profile.getProgramCode());
            jo.put("deviceCarrier", UrlUtil.getUrlString(profile.getDeviceCarrier()));
            jo.put("platform", profile.getPlatform());
            jo.put("version", profile.getVersion());
            jo.put("productType", profile.getProduct());
            jo.put("device", profile.getDevice());
            jo.put("locale", profile.getLocale());
            jo.put("buildNumber", profile.getBuildNumber());
            jo.put("region", profile.getRegion());

        }
        catch (JSONException e)
        {
            LOGGER.fatal("getClientJson", e);
        }

        StringBuilder sb = new StringBuilder("clientInfo=").append(URLEncoder.encode(jo.toString()))
                            .append("&width=" + profile.getScreenWidth())
                            .append("&height=" + profile.getScreenHeight());
        
        return sb.toString();

    }
}
