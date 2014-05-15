/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.holder.impl;

import com.telenav.cserver.common.resource.AbstractResourceHolder;
import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.common.resource.SpringObjectNameAware;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;
/**
 * For holding billing related configuration
 * @author kwwang
 *
 */
public class BillingConfHolder extends AbstractResourceHolder implements SpringObjectNameAware
{

    @Override
    public ResourceContent createObject(String key, UserProfile profile, TnContext tnContext)
    {
        Object result = ResourceFactory.createResource(this, profile, tnContext);
        ResourceContent rc = new ResourceContent();
        rc.setObject(result);
        return rc;
    }

    @Override
    public String getObjectName()
    {
        return "billing_conf";
    }

}
