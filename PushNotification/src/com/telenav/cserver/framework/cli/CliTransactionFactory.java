/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.cli;

import org.apache.commons.lang3.StringUtils;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.UserProfile;

/**
 * get Fake cli or cli based on the cli_client.xml
 * only log exception
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2010-6-23
 *
 */
public class CliTransactionFactory
{
    
    public static CliTransaction getInstance(String type)
    {
        UserProfile userProfile = CliThreadLocalUtil.getUserProfile();
        String executorType = CliThreadLocalUtil.getExecutorType();
        
        boolean b = false;
        
        if(StringUtils.isNotBlank(executorType))
        {
            b = CliClientConfig.getCliClientConfig(userProfile, executorType);
        }

        if (b)
            return new CliTransaction(type);
        else
            return new CliFakeTransaction(type);
    }

}
