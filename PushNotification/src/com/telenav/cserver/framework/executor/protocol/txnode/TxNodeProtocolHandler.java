/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.txnode;

import com.telenav.cserver.framework.executor.protocol.ProtocolHandler;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;

/**
 * TxNodeProtocolHandler.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-4-12
 *
 */
public class TxNodeProtocolHandler implements ProtocolHandler
{
	ProtocolRequestParser requestParser = new TxNodeRequestParser();
	
	ProtocolResponseFormatter responseFormatter = new TxNodeResponseFormatter();

	/**
	 * @return the requestParser
	 */
	public ProtocolRequestParser getRequestParser() {
		return requestParser;
	}

	/**
	 * @param requestParser the requestParser to set
	 */
	public void setRequestParser(ProtocolRequestParser requestParser) {
		this.requestParser = requestParser;
	}

	/**
	 * @return the responseFormatter
	 */
	public ProtocolResponseFormatter getResponseFormatter() {
		return responseFormatter;
	}

	/**
	 * @param responseFormatter the responseFormatter to set
	 */
	public void setResponseFormatter(ProtocolResponseFormatter responseFormatter) {
		this.responseFormatter = responseFormatter;
	}


}
