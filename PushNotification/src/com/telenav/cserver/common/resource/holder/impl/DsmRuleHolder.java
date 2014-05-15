/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.holder.impl;

import java.util.HashMap;
import java.util.Map;

import com.telenav.cserver.common.resource.AbstractRuleMatchHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * DsmRuleHolder for getting dsm rules from local files.
 * 
 * Note: We currently implement this DsmRuleHolder for poi server, not for resource/common/navmap cserver temporarily.
 * 
 * @author kwwang
 * @date 2010-5-10
 */
public class DsmRuleHolder extends AbstractRuleMatchHolder
{
    /**
     * Return a single dsm response by tnContextKey.
     * 
     * @author kwwang
     * @date 2010-5-10
     * @param profile
     * @param tnContextKey-- the key defined in TnContext, should be like TnContext.PROP_AD_ENGINE
     * @param tc
     * @return
     */
    public String getDsmResponseBy(UserProfile profile, String tnContextKey, TnContext tc)
    {
        HashMap outputMap = getMatchedMap(profile, tc);
        if (outputMap == null)
        {
            return null;
        }

        return (String) outputMap.get(tnContextKey);
    }

    /**
     * Return all the dsm responses
     * 
     * @author kwwang
     * @date 2010-5-10
     * @param profile
     * @param tc
     * @return
     */
    public Map getDsmResponses(UserProfile profile, TnContext tc)
    {
        return getMatchedMap(profile, tc);
    }

}
