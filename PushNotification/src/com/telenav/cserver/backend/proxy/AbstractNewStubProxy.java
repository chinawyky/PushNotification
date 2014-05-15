/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

import java.util.Map;
import java.util.Set;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceManager;

/**
 * Compared to old AbstractStubProxy, the createStub api is different, recommend
 * to using this first. We can construct our own object as input parameter to
 * determine the way we create stub.
 * 
 * @see EtaProxy
 * @author kwwang
 * 
 * @param <T>
 * @param <V>
 */
public abstract class AbstractNewStubProxy<T, V> extends AbstractProxy {
	
	private Logger logger = Logger.getLogger(AbstractNewStubProxy.class);
	
	protected static final String SEPARATOR = "_";

	private ConfigurationContext context;

	/**
	 * Need subclass to implement, different stubs can be created.
	 * 
	 * @param ws
	 * @return
	 * @throws Exception
	 */
	protected abstract T createStub(WebServiceConfigInterface ws, V v)
			throws Exception;

	/**
	 * Can't use DCL here, since the properties in ConfigurationContext are not
	 * volitale. but we need to share the context for multiple api in each proxy
	 * 
	 * @param ws
	 * @return
	 * @throws Exception
	 */
	protected synchronized ConfigurationContext createContext(
			WebServiceConfigInterface ws) throws Exception {
		if (context == null)
			context = WebServiceManager
					.createNewContext(ws.getWebServiceItem());
		return context;
	}

	protected WebServiceConfigInterface getWebServiceConfigInterface() {
		return WebServiceConfiguration.getInstance().getServiceConfig(
				getProxyConfType());
	}
	
	
	protected String getUrl(Map<String, String> urlMap, String key) {
		if (logger.isDebugEnabled()) {
			logger.debug("ws service key" + key);
		}
		Set<String> keys = urlMap.keySet();
		String url = null;
		if (StringUtils.isNotEmpty(key) && !keys.isEmpty()) {
			if (keys.contains(key)) {
				url = urlMap.get(key);
			} else {
				url = urlMap.get(key.substring(0, key.lastIndexOf(SEPARATOR)));
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ws service url" + url);
		}
		return url;
	}

}
