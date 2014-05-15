/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.pushNotification;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.proxy.AbstractStubProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.services.pushnotification.v10.PushNotificationRequestDTO;
import com.telenav.services.pushnotification.v10.PushNotificationResponseDTO;
import com.telenav.services.pushnotification.v10.PushNotificationServiceStub;
import com.telenav.ws.services.messaging.AndroidPushMessage;
import com.telenav.ws.services.messaging.PlatformPushMessage;

/**
 * 
 * 
 * 
 */

@BackendProxy
@ThrottlingConf("PushNotificationProxy")
public class PushNotificationProxy extends AbstractStubProxy<PushNotificationServiceStub>
{

    private static final Logger logger = Logger.getLogger(PushNotificationProxy.class);
    
    protected PushNotificationProxy()
    {
        
    }
    
 
    @ProxyDebugLog
    @Throttling
    public PushNotificationResponseDTO PushMessage(long userId, String registrationId, AndroidPushMessage msg) throws java.rmi.RemoteException 
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("PushMessage");

        cli.addData("PushMessage request param: ", "userID: " + userId + " ,registrationId: " + registrationId);

        PushNotificationResponseDTO response = null;
        PushNotificationServiceStub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());  
            
            PushNotificationRequestDTO pushRequest = new PushNotificationRequestDTO();
            
            pushRequest.setMessages(new PlatformPushMessage[] { msg });
            
            response = stub.push(pushRequest);
            System.out.println(response);
            System.out.println(response.getStatus()); // the status may be 0 : all messages sent succeeded sending to 3rd party, or -1 :  All failed, or -2 : At least one succeed and at least one failed
            System.out.println(response.getStatusString());
            
            cli.addData("PushNotificationResponseDTO", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
            logger.fatal("PushNotificationProxy#PushMessage",e);
            cli.setStatus(e);
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }   
    
    @Override
    protected PushNotificationServiceStub createStub(WebServiceConfigInterface ws) throws Exception
    {
    	PushNotificationServiceStub stub = null;
        try
        {
            stub = new PushNotificationServiceStub(createContext(ws), ws.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout());

        }
        catch (Exception e)
        {
            logger.fatal("create PushNotificationServiceStub stub failed", e);
        }

        return stub;
    }

    @Override
    public String getProxyConfType()
    {
        return "PUSH_NOTIFICATION";
    }

}
