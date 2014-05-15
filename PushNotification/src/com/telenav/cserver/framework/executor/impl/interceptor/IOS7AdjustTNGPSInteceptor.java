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
 * Interceptor for cases when IPhone TNGPS clients update to iOS7 MacAddress mess up issue.

 * 
 * @author qjwang2
 *
 */
public class IOS7AdjustTNGPSInteceptor implements Interceptor{

	//SHA1 for macAddress '02:00:00:00:00:00'
	private static final String IOS7_FAKE_MACADDRESS = "c1976429369bfe063ed8b3409db7c7e7d87196d9";
	
	@Override
	public InterceptResult intercept(ExecutorRequest request,
			ExecutorResponse response, ExecutorContext context) 
	{
		
		CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
		cli.setFunctionName("IOS7_Adjust_TNGPS_Interceptor");
		UserProfile userProfile = request.getUserProfile();
		
		if(isTNGPSUpdatedCase(request.getUserProfile()))
		{
			cli.addData("case", "Login after iOS7 With Updated TNGPS Clients");
			userProfile.setMacID(userProfile.getVendorId());
			
			return InterceptResult.PROCEED;
		}
		
		cli.addData("case", "Non-filter case");
		
		return InterceptResult.PROCEED;
		
	}

	/**
	 * An updated TNGPS/TNGPS+ clients do login after iOS 7. They will only get vendorID.
	 * We use vendorId as an Identifier to replace macAddress in Cserver, thus billing will not change their database structure.
	 * 
	 * Since for TNGPS/TNGPSPLUS FTUE login logic is from Browser cserver side. Cserver will not run into cases that userId/credential Id is null.
	 * So only replace the macAddress with vendorId, In case billing will do update with the garbage macAddress.
	 * 
	 * @param userProfile
	 * @return
	 */
	private boolean isTNGPSUpdatedCase(UserProfile userProfile) 
	{
		
		if(!IsTNGPSClient(userProfile))
		{
			return false;
		}
		
		boolean isTNGPSUpdatedCase = false;
		
		String openUDID = userProfile.getOpenUDID();//3rd Party Identifier, based on macAddress
		String macAddress = userProfile.getMacID();//Identifier for pre-iOS7
		String vendorID = userProfile.getVendorId();//Identifier for iOS7
		
		if(StringUtils.isNotEmpty(vendorID)&&isIOS7GarbageMacAddress(macAddress)&&StringUtils.isEmpty(openUDID))
		{
			isTNGPSUpdatedCase = true;
		}		
		
		return isTNGPSUpdatedCase;
	}
	
	private boolean IsTNGPSClient(UserProfile userProfile) {
		
		boolean isTNGPSClients = false;
		
		if("TNNAVPROG".equalsIgnoreCase(userProfile.getProgramCode())||"TNNAVPLUSPROG".equalsIgnoreCase(userProfile.getProgramCode()))
		{
			isTNGPSClients = true;
		}
		
		return isTNGPSClients;
	}

	private boolean isIOS7GarbageMacAddress(String macAddress)
	{
		return StringUtils.equals(macAddress, IOS7_FAKE_MACADDRESS);
	}
}
