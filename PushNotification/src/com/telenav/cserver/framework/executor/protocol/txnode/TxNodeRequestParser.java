/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.txnode;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.GpsData;
import com.telenav.j2me.datatypes.TxNode;

/**
 * TxNodeRequestParser.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 *
 */
public class TxNodeRequestParser implements ProtocolRequestParser {

	static Category logger = Category.getInstance(TxNodeRequestParser.class);
	private static final String GUIDETONE_FEMAILE_ID="203";
	private static final String GUIDETONE_DEFAULT_NAME=null;
	private static final String GUIDETONE_MALE_ID="204";
    private static final String GUIDETONE_MALE_NAME="male";
    private static final String GUIDETONE_SPECIAL_ID="202";
	
	/**
	 * parse the protocol bytes into ActionRequest
	 * 
	 * @param bytes
	 * @return
	 * @throws ExecutorException
	 */
	public ExecutorRequest[] parse(Object object) throws ExecutorException 
	{
	    byte[] bytes = (byte[])object;
	    logger.info("Client request bytes:" + bytes.length);
	    TxNode rootNode = TxNode.fromByteArray(bytes, 0);
		if(rootNode == null)
		{
			return null;
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Client request node:\n" + rootNode);
		}
		ExecutorRequest[] requests = null;
		UserProfile userProfile = new UserProfile();
		List<GpsData> gpsData = null;
				
		List<ExecutorRequest> requestList = new ArrayList<ExecutorRequest>(1);
		for(int i = 0; i < rootNode.childrenSize(); i ++)
		{
			TxNode node = rootNode.childAt(i);
			String nodeType = node.msgAt(0);
			if(nodeType == null)
			{
				logger.warn("Skip Unknown child node:" + node);
				continue;
			}
			if(nodeType.equalsIgnoreCase("profile"))
			{
				//create UserProfile
				userProfile = createUserProfile(node);
								
			}
			else if(nodeType.equalsIgnoreCase("gps"))
			{

				//create GpsData list
				gpsData = createGpsData(node);
			} 
			else 
			{
				//this is a business request data
				ProtocolRequestParser parser = 
					ExecutorDataFactory.getInstance().createProtocolRequestParser(nodeType + "");
				ExecutorRequest[] executorRequests = parser.parse(node);
				for(ExecutorRequest req: executorRequests)
				{
					req.setExecutorType(nodeType);
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

	/**
	 * @param node
	 * @return
	 */
	private List<GpsData> createGpsData(TxNode node) 
	{
		List<GpsData> gpsData = new ArrayList<GpsData>();
		
		for (int j = 0; j < node.childrenSize(); j++)
		{
			TxNode d = node.childAt(j);
			GpsData fix = GpsData.fromTxNode(d);
			
			gpsData.add(fix);
		}					
		return gpsData;
	}
	

	/**
	 * @param node
	 * @return
	 */
	public UserProfile createUserProfile(TxNode node) 
	{
		UserProfile userProfile = new UserProfile();
		for (int j = 0; j < node.childrenSize(); j++)
		{
			TxNode child = node.childAt(j);
			if (child.getValuesCount() == 0)
			{
				continue;
			}
			byte childType = (byte) child.valueAt(0);
			switch (childType)
			{
				case DataConstants.USER_INFO:
					userProfile.setMin(child.msgAt(0));
					userProfile.setPassword(child.msgAt(1));
					userProfile.setUserId(child.msgAt(2));
					userProfile.setEqPin(child.msgAt(3));
					userProfile.setLocale(child.msgAt(4));
					userProfile.setRegion(child.msgAt(5));
					userProfile.setSsoToken(child.msgAt(6));
					//userProfile.setGuideTone(child.msgAt(7));
					userProfile.setGuideTone(getGuideToneName(child.msgAt(7)));
					break;
				case DataConstants.CLIENT_VERSION:
					userProfile.setCarrier(child.msgAt(0));
					userProfile.setPlatform(child.msgAt(1));
					userProfile.setVersion(child.msgAt(2));
					userProfile.setDevice(child.msgAt(3));
					userProfile.setBuildNumber(child.msgAt(4));
					userProfile.setGpsType(child.msgAt(5));
					userProfile.setProduct(child.msgAt(6));
					userProfile.setDeviceCarrier(userProfile.getCarrier()); /*Keep consistent with ProtoBuf parser, use carrier to set device carrier*/
					if(child.msgsSize() > 7)
					{
						userProfile.setMapDpi(child.msgAt(7));
					}
					userProfile.setOSVersion(child.msgAt(8));
					
					//for 6x client, the program code and device carrier is the same as carrier
					userProfile.setProgramCode(child.msgAt(0));
					break;
				case DataConstants.USER_PREFS:
					userProfile.setAudioFormat(child.msgAt(0));
					userProfile.setImageType(child.msgAt(1));
					userProfile.setAudioLevel(child.msgAt(2));
					userProfile.setDataProcessType(child.msgAt(3));
                    userProfile.setScreenWidth(child.msgAt(4));
                    userProfile.setScreenHeight(child.msgAt(5));
				default:
					continue;
			}// switch
		}// for
		return userProfile;
	}
	
	private String getGuideToneName(String guideToneFromClient)
    {
        String result = GUIDETONE_DEFAULT_NAME;
        if (guideToneFromClient != null)
        {
            String id = null;
            String name = null;
            int index = guideToneFromClient.indexOf(',');
            if (index != -1)
            {
                id = guideToneFromClient.substring(0, index);
                name = guideToneFromClient.substring(index + 1);
                if ("".equals(name.trim()))
                {
                    name = GUIDETONE_DEFAULT_NAME;
                }
            }
            else
            {
                id = guideToneFromClient;
            }

            if (id.equals(GUIDETONE_SPECIAL_ID))
            {
                result = GUIDETONE_SPECIAL_ID;
            }
            else if (id.equals(GUIDETONE_FEMAILE_ID))
            {
                result = GUIDETONE_DEFAULT_NAME;
            }
            else if (id.equals(GUIDETONE_MALE_ID))
            {
                result = GUIDETONE_MALE_NAME;
            }
            else
            {
                if(isDigit(id)||(id!=null&&id.trim().length()==0))
                    result = name;
                else
                    result= id;
            }

        }
        return result;
    }
	
    private boolean isDigit(String str)
    {
        boolean flag = true;
        try
        {
            Integer.valueOf(str);
        }
        catch (Exception e)
        {
            flag = false;
        }
        return flag;
    }

}
