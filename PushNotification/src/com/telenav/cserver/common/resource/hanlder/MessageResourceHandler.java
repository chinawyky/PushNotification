/**
 * (c) Copyright 2013 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.hanlder;

import java.util.Iterator;

import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.common.resource.message.Messages;
import com.telenav.cserver.common.resource.message.MessagesHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * MessageResourceHandler
 * @author kwwang
 *
 */
public class MessageResourceHandler implements ResourceHandler {

	public static final String RESOURCE_TYPE="message";
	
	private final MessagesHolder messageHolder=(MessagesHolder)ResourceHolderManager.getResourceHolder(HolderType.MESSAGE_TYPE);
	
	@Override
	public TxNode handle(UserProfile userProfile, TnContext tc) {
		
		TxNode messageNode = null;
		Messages messages = messageHolder.getMessages(userProfile, tc);
		if(messageHolder != null)
		{
			if(messages != null)
			{
				messageNode = new TxNode();
				messageNode.addMsg(RESOURCE_TYPE);
				TxNode messageItemNode = new TxNode();
				messageNode.addChild(messageItemNode);
				
				if(messages.getMessages() != null)
				{
					Iterator keyIterator = messages.getMessages().keySet().iterator();
					while(keyIterator.hasNext())
					{
						String key = (String)keyIterator.next();
						messageItemNode.addMsg(key+"="+messages.getMessages().get(key));
					}
				}
			}
		}
		return messageNode;
	}

}
