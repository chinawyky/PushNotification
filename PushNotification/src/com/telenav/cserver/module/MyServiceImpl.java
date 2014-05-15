package com.telenav.cserver.module;

import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.proxy.pushNotification.PushNotificationProxy;
import com.telenav.cserver.backend.proxy.username.UserNameServiceHelper;
import com.telenav.cserver.backend.proxy.username.UserNameServiceProxy;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.pushnotification.v10.PushNotificationResponseDTO;
import com.telenav.services.user.management.v11.GetUserProfileByCredentialRequestDTO;
import com.telenav.services.user.management.v11.GetUserProfileResponseDTO;
import com.telenav.ws.services.messaging.AndroidPushMessage;

/**
 * @author  yeeku.H.lee kongyeeku@163.com
 * @version  1.0
 * <br>Copyright (C), 2005-2008, yeeku.H.Lee
 * <br>This program is protected by copyright laws.
 * <br>Program Name:
 * <br>Date: 
 */
public class MyServiceImpl implements MyService
{
	public boolean valid(String username ,String pass)
	{
        if (username.equals("scott") && pass.equals("tiger") )
		{
			return true;
		}
		return false;
	}
	
	public boolean pushNotification(String title , String message, String action, long userId) throws Exception
	{
		String registrationId = null;
		TnContext tnContext = new TnContext();
		
		GetUserProfileByCredentialRequestDTO request = UserNameServiceHelper.newGetUserProfileByCredentialRequestDTO(userId, tnContext);
		
		GetUserProfileResponseDTO userProfileResponse = BackendProxyManager.getBackendProxyFactory().getBackendProxy(UserNameServiceProxy.class)
                .getRegistrationID4UserId(request, tnContext);
		
		registrationId = UserNameServiceHelper.getRegistrationId(userProfileResponse);

		AndroidPushMessage msg = new AndroidPushMessage(); 
        
        msg.setUserIds(new long[] { userId });//recipient's user id
        
        msg.setTitle(title);
        
        msg.setMessage(message);
        
        //msg.setAction( "telenav://map?v=2.0&cb=planner%3A%2F%2F08092009&k=AQAAASSR1YUgf%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAABAAAAAQEAAAAQUm2yry0wCYjB0QetcacwXwEAAAAOAwAAABEAAAAZAAAAAQA%3D&c=cn&markers=CURRENT");  //set the action to indicate client how to act,   it can be maitai URL etc.
        msg.setAction(action);  //set the action to indicate client how to act,   it can be maitai URL etc.
        
        msg.setPopupFlag( false ); //set it to true for auto wake up

        msg.setRegistrationId(registrationId);//REG_ID must be given for ANDROID Push Notification Message
		
		PushNotificationResponseDTO response = BackendProxyManager.getBackendProxyFactory().getBackendProxy(PushNotificationProxy.class)
                .PushMessage(userId, registrationId, msg);
		
		return true;
	}
}
