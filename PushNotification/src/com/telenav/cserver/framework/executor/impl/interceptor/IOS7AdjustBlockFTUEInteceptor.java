/**
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl.interceptor;

import org.apache.commons.lang3.StringUtils;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.Interceptor;

/**
 * Interceptor for cases when IPhone Scout and TNGPS clients update to iOS7 MacAddress messup issue.
 * 
 * @author qjwang2
 *
 */
public class IOS7AdjustBlockFTUEInteceptor implements Interceptor{

	//SHA1 for macAddress '02:00:00:00:00:00'
	private static final String IOS7_FAKE_MACADDRESS = "c1976429369bfe063ed8b3409db7c7e7d87196d9";
	
	@Override
	public InterceptResult intercept(ExecutorRequest request,
			ExecutorResponse response, ExecutorContext context) 
	{
		
		CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
		cli.setFunctionName("IOS7_Adjust_Block_FTUE_Interceptor");
		UserProfile userProfile = request.getUserProfile();
		
		if(isLoginAfterIos7WithLegacyClient(userProfile))
		{
			cli.addData("case", "Login after iOS7 With Legacy Clients(User Identifier info is invalid)");
			return InterceptResult.HALT;
		}
		
		cli.addData("case", "Non-filter case");
		
		return InterceptResult.PROCEED;
		
	}

	/**
	 * Those user who with legacy clients(with only macAddress and no openUDID and vendorId in ProtoUserProfile) do fresh login after iOS 7
	 * 
	 * @return
	 */
	private boolean isLoginAfterIos7WithLegacyClient(UserProfile userProfile) {		

		boolean isLoginAfterIos7WithLegacyClient = false;
		
		String openUDID = userProfile.getOpenUDID();//3rd Party Identifier, based on macAddress
		String macAddress = userProfile.getMacID();//Identifier for pre-iOS7
		String vendorID = userProfile.getVendorId();//Identifier for iOS7
		String userID = userProfile.getUserId();//device login userId
		String credentialId = userProfile.getCredentialID();//email login userId
		
		if("SCOUTPROG".equalsIgnoreCase(userProfile.getProgramCode())||"TNNAVPROG".equalsIgnoreCase(userProfile.getProgramCode())||"TNNAVPLUSPROG".equalsIgnoreCase(userProfile.getProgramCode()))
		{
			if(StringUtils.isEmpty(vendorID)&&isIOS7GarbageMacAddress(macAddress)&&StringUtils.isEmpty(openUDID)&&StringUtils.isEmpty(userID)&&StringUtils.isEmpty(credentialId))
			{
				isLoginAfterIos7WithLegacyClient = true;
			}
		}
		
		return isLoginAfterIos7WithLegacyClient;
	}
	
	private boolean isIOS7GarbageMacAddress(String macAddress)
	{
		return StringUtils.equals(macAddress, IOS7_FAKE_MACADDRESS);
	}
}
