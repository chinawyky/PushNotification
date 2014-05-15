/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.framework.throttling.Service;
import com.telenav.cserver.framework.throttling.ThrottlingConfiguration;
import com.telenav.cserver.framework.throttling.ThrottlingException;

/**
 * ThrottlingConfManager
 * 
 * @author kwwang
 * 
 */
public class ThrottlingConfManager {
	public static final Logger logger = Logger
			.getLogger(ThrottlingConfManager.class);

	private Map<String, ThrottlingControlItem> throttlingControls = new HashMap<String, ThrottlingControlItem>();

	private static final String DEFAULT_SERVICE = "default_service";

	public ThrottlingConfManager() {
		ThrottlingConfiguration tc = ThrottlingConfiguration.getInstance();
		List services = tc.getServiceList();
		for (int i = 0; i < services.size(); i++) {
			Service service = (Service) services.get(i);
			if (service == null || service.getServiceTypes().length == 0) {
				logger.fatal("service doesn't contains service types, "
						+ service.getName());
				continue;
			}
			String name = service.getServiceTypes()[0];
			ThrottlingControlItem item = new ThrottlingControlItem();
			item.setProxyName(name);
			item.setMaxNumberOnLine(service.getMaxAllowedOnlineNumber());
			throttlingControls.put(name, item);
		}
	}

	public void incrementThrottling(ThrottlingConf conf)
			throws ThrottlingException {

		if (throttlingControls.get(conf.value()) == null) {
			synchronized (throttlingControls) {
				if (throttlingControls.get(conf.value()) == null) {
					ThrottlingControlItem defaultItem = throttlingControls
							.get(DEFAULT_SERVICE);

					if (defaultItem == null) {
						logger.fatal("increment - there is no throttling conf for default and "
								+ conf.value());
						return;
					}

					ThrottlingControlItem newSpecificDefaultItem = new ThrottlingControlItem();
					newSpecificDefaultItem.setProxyName(conf.value());
					newSpecificDefaultItem.setMaxNumberOnLine(defaultItem
							.getMaxNumberOnLine());
					throttlingControls
							.put(conf.value(), newSpecificDefaultItem);
				}
			}
			throttlingControls.get(conf.value()).increment();
		}
		else
		{
			throttlingControls.get(conf.value()).increment();
		}

	}

	public void decrementThrottling(ThrottlingConf conf) {
		ThrottlingControlItem item = throttlingControls.get(conf.value());
		if (item != null)
			item.decrement();
		else
			logger.fatal("decrement - there is no throttling conf for "
					+ conf.value());
	}

	protected ThrottlingControlItem getThrottlingControlItem(String key) {
		return throttlingControls.get(key);
	}

}
