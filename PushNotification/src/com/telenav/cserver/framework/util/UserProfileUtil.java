/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import org.apache.commons.lang3.StringUtils;

import com.telenav.cserver.framework.UserProfile;

/**
 * UserProfileUtil.java
 * @author njiang
 * @version 1.0 2013-2-20
 */
public class UserProfileUtil {
	
	public static final String CREDENTIAL_TYPE_VALUE = "EMAIL";
	
	public static long convertToUserId(UserProfile userProfile){
    	long userId = Long.valueOf(userProfile.getUserId());
        String credentialId = userProfile.getCredentialID();
        String credentialType = userProfile.getCredentialType();
        if(StringUtils.isNotBlank(credentialId) && StringUtils.isNotBlank(credentialType) && credentialType.equalsIgnoreCase(CREDENTIAL_TYPE_VALUE)){
        	userId = Long.valueOf(credentialId);
        }
        
        return userId;
   }

}
