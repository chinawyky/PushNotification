/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import org.apache.commons.lang3.StringUtils;

/**
 * String Util
 * 
 * @author kwwang
 * 
 */
public class CSStringUtil
{
    public static final String NULL_STR = "null";

    /**
     * Judge if the string has length, that is to say, not null.
     * 
     * @param str
     * @return
     */
    public static boolean isNotNil(String str)
    {
        return !isNil(str);
    }

    /**
     * Judge if the str is null.
     * 
     * @param str
     * @return
     */
    public static boolean isNil(String str)
    {
        return (StringUtils.isBlank(str) || (str.trim().equalsIgnoreCase(NULL_STR)));
    }

    /**
     * Remove the tail from src, ex, assume the src is 'map_color.client.bar.version' and the tail is '.version', then
     * we will get the 'map_color.client.bar' as result.
     * 
     * @param src
     * @param tail
     * @return
     */
    public static String removeTheTail(String src, String tail)
    {
        if (isNotNil(src) && isNotNil(tail))
        {
            int idx = src.indexOf(tail);
            if (idx != -1)
            {
            	return src.substring(0, idx);
            }
        }
        return null;
    }

    /**
     * The isEqual method, if both of strings are not null, compare with each other ignoring their cases.
     * 
     * @param str
     * @param otherStr
     * @return
     */
    public static boolean isEqual(String str, String otherStr)
    {
        if (isNotNil(str) && isNotNil(otherStr))
        {
	        if (str.trim().equalsIgnoreCase(otherStr.trim()))
	        {
	            return true;
	        }
        }
        return false;
    }

   

    public static String parse(char[] in, int off, int len, char[] convtBuf)
    {
        if (convtBuf.length < len)
        {
            int newLen = len * 2;
            if (newLen < 0)
            {
                newLen = Integer.MAX_VALUE;
            }
            convtBuf = new char[newLen];
        }
        char aChar;
        char[] out = convtBuf;
        int outLen = 0;
        int end = off + len;

        while (off < end)
        {
            aChar = in[off++];
            if (aChar == '\\')
            {
                aChar = in[off++];
                if (aChar == 'u')
                {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++)
                    {
                        aChar = in[off++];
                        switch (aChar)
                        {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    out[outLen++] = (char) value;
                }
                else
                {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    out[outLen++] = aChar;
                }
            }
            else
            {
                out[outLen++] = (char) aChar;
            }
        }
        return new String(out, 0, outLen);
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
}
