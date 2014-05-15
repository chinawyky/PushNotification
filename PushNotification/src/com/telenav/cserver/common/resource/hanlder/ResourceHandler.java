/**
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.hanlder;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * ResourceHandler
 * @author kwwang
 *
 */
public interface ResourceHandler {
	public TxNode handle(UserProfile userProfile,TnContext tc);
}
