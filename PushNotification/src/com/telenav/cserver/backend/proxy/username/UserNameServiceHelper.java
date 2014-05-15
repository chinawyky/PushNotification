/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.username;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.datatypes.user.management.v11.Attribute;
import com.telenav.datatypes.user.management.v11.AttributeValue;
import com.telenav.datatypes.user.management.v11.AttributeValueType;
import com.telenav.datatypes.user.management.v11.ComplexAttributeValue;
import com.telenav.datatypes.user.management.v11.Profile;
import com.telenav.datatypes.user.management.v11.ProfileType;
import com.telenav.datatypes.user.management.v11.UserQueryBy;
import com.telenav.datatypes.user.v20.User;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.user.management.v11.GetAttributeRequestDTO;
import com.telenav.services.user.management.v11.GetUserProfileByCredentialRequestDTO;
import com.telenav.services.user.management.v11.GetUserProfileResponseDTO;
import com.telenav.services.user.management.v11.GetUserRequestDTO;
import com.telenav.services.user.management.v11.IsUniqueUsernameRequestDTO;
import com.telenav.services.user.management.v11.UpdateUserProfileRequestDTO;
import com.telenav.services.user.management.v11.UpdateUserRequestDTO;

/**
 * UserNameServiceHelper.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 Mar 9, 2011
 * 
 */
public class UserNameServiceHelper
{
    public final static String version = "v10";
    
    public final static int BASE_INDEX = 0;

    public final static String clientname = "clientName";

    public final static String transactionId = "001";
    
    public final static String REGISTER_ID="registration_id";
    
    public static final String ATTRIBUTE_USER_OPTIN_UPDATE = "optInUpdate";
    
    public static final String ATTRIBUTE_USER_LOCALE_UPDATE = "locale";
    
    public final static String OPTIN_PUSH = "optIn_push";

    public static final GetUserRequestDTO newGetUserRequestDTO(long userId, TnContext tc)
    {
        GetUserRequestDTO getUserRequest = new GetUserRequestDTO();
        getUserRequest.setParam(Long.toString(userId));
        getUserRequest.setParamType(UserQueryBy.USER_ID);
        getUserRequest.setClientName(clientname);
        getUserRequest.setClientVersion(version);
        getUserRequest.setContextString(tc.toContextString());
        getUserRequest.setTransactionId(transactionId);
        return getUserRequest;
    }
    
    public static final GetUserProfileByCredentialRequestDTO newGetUserProfileByCredentialRequestDTO(long userId, TnContext tc)
    {
    	GetUserProfileByCredentialRequestDTO request = new GetUserProfileByCredentialRequestDTO();
        
    	com.telenav.datatypes.user.management.v11.UserQueryBy credentialType = com.telenav.datatypes.user.management.v11.UserQueryBy.USER_ID;
        com.telenav.datatypes.user.management.v11.ProfileType profileType = com.telenav.datatypes.user.management.v11.ProfileType.PREFERENCES;
        
        String[] attributeNames = {ATTRIBUTE_USER_LOCALE_UPDATE, REGISTER_ID};
        
        String credentialValue = String.valueOf(userId);
        
        request.setCredentialValue(credentialValue);
        request.setCredentialType(credentialType);
        request.setProfileType(profileType);
        request.setAttributeNames(attributeNames);
        
        request.setClientName(clientname);
        request.setClientVersion(version);
        request.setContextString(tc.toContextString());
        request.setTransactionId(transactionId);
        return request;
    }
    
    public static final String getRegistrationId(GetUserProfileResponseDTO response)
    {
    	String registration_id = null;
    	
    	Attribute[] attrs = response.getUserProfile().getAttributes();
        for (int i = 0; i < attrs.length; i++)
        {
        	if (attrs[i].getName().equalsIgnoreCase(UserNameServiceHelper.REGISTER_ID))
        	{
        		registration_id = attrs[i].getValue()[BASE_INDEX].getValue();
        	}
        }
        return registration_id;
    }
    

    public static final UpdateUserRequestDTO newUpdateUserRequestDTO(User user, TnContext tc)
    {
        UpdateUserRequestDTO updateRequest = new UpdateUserRequestDTO();
        updateRequest.setUser(user);
        updateRequest.setClientName(clientname);
        updateRequest.setClientVersion(version);
        updateRequest.setContextString(tc.toContextString());
        updateRequest.setTransactionId(transactionId);
        return updateRequest;
    }

    public static final IsUniqueUsernameRequestDTO newIsUniqueUsernameRequestDTO(String userName, TnContext tc)
    {
        IsUniqueUsernameRequestDTO isUniqueRequest = new IsUniqueUsernameRequestDTO();
        isUniqueRequest.setUserName(userName);
        isUniqueRequest.setClientName(clientname);
        isUniqueRequest.setClientVersion(version);
        isUniqueRequest.setContextString(tc.toContextString());
        isUniqueRequest.setTransactionId(transactionId);
        return isUniqueRequest;
    }

    public static final SaveUserNameRequest newSaveUserNameRequest(long userId, String userName)
    {
        return new SaveUserNameRequest(userId, userName);
    }
    
    public static final UpdateUserProfileRequestDTO newUpdateUserProfileRequestDTOWithRegisterInfo(UserProfile userProfile, String registerId)
    {
        UpdateUserProfileRequestDTO request = new UpdateUserProfileRequestDTO();
        Profile profile = new Profile();
        Attribute att = new Attribute();
        att.setName(REGISTER_ID);
        att.setType(AttributeValueType.COMPLEX);
        ComplexAttributeValue complexAttributeValue = new ComplexAttributeValue();
        complexAttributeValue.setValue(registerId);
        complexAttributeValue.setProgramCode(userProfile.getProgramCode());
        complexAttributeValue.setDeviceId(userProfile.getDeviceID());
        complexAttributeValue.setPlatform(userProfile.getPlatform());
        att.setValue(new AttributeValue[] { complexAttributeValue });
        profile.setName("PREFERENCES");
        profile.setAttributes(new Attribute[] { att });
        request.setProfile(profile);
        request.setTransactionId(transactionId);
        request.setClientName(clientname);
        request.setClientVersion(version);
        return request;
        
    }
    
    public static final UpdateUserProfileRequestDTO newUpdateUserProfileRequestDTOWithAttributes(Attribute[] attrs)
    {
    	UpdateUserProfileRequestDTO request = new UpdateUserProfileRequestDTO();
		Profile profile = new Profile();
		profile.setName(ProfileType._PREFERENCES);
		profile.setAttributes(attrs);
		request.setProfile(profile);
		request.setClientName(clientname);
		request.setClientVersion(version);
		request.setTransactionId(transactionId);
		return request;
    }
    
    public static final GetAttributeRequestDTO newGetAttributeRequestDTO(String attributeName)
    {
    	GetAttributeRequestDTO request= new GetAttributeRequestDTO();
    	request.setProfile(ProfileType.PREFERENCES);
    	request.setAttributeName(attributeName);
    	request.setTransactionId(transactionId);
    	request.setClientName(clientname);
    	request.setClientVersion(version);
		return request;
    }
}
