/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.cli;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.UserProfile;



/**
 * CliFakeTransaction.java
 * only log exception
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2010-6-23
 *
 */
public class CliFakeTransaction extends CliTransaction
{
    public CliFakeTransaction(String type)
    {
        super(type);
    }

    
    public void setFunctionName(String functionName)
    {
        
    }
    public void addData(String label, String data)
    {
    }

    public void setStatus(Throwable t)
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_URL);

        UserProfile userProfile = CliThreadLocalUtil.getUserProfile();
        String executorType = CliThreadLocalUtil.getExecutorType();
        
        cli.setFunctionName("service_" + executorType);

        if (userProfile != null)
        {
            cli.addData(CliConstants.LABEL_CLIENT_INTO, "url="
                    + executorType + "&userid=" + userProfile.getUserId()
                    + "&min=" + userProfile.getMin() + "&carrier="
                    + userProfile.getCarrier() + "&platform=" + userProfile.getPlatform()
                    + "&device=" + userProfile.getDevice() + "&version="
                    + userProfile.getVersion() + "&buildNo="
                    + userProfile.getBuildNumber());
        }
        else
        {
            cli.addData(CliConstants.LABEL_CLIENT_INTO, "url="
                    + executorType);
        }

        cli.setStatus(t);
        cli.complete();
    }

    public void complete()
    {
    }

    public void setStatus(String status)
    {

    }

}
