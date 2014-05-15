/**
 * (c) Copyright 2008 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.throttling;

import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.kernel.util.datatypes.TnContext;

/**
 * Throttling Manager, to apply throttling action on API level
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2008-7-24
 * 
 */
public final class ThrottlingManager
{
	protected static final Logger LOGGER = Logger.getLogger(ThrottlingManager.class);

	
	private static ThrottlingConfiguration throttlingConf = ThrottlingConfiguration.getInstance();
	
	private ThrottlingManager()
	{}
	
	/**
	 * service list
	 */
	static List serviceList = throttlingConf.getServiceList();

	/**
	 * return service list for management, i.e, web console, i.e.
	 * 
	 * @return
	 */
	public static List getServiceList()
	{
		return serviceList;
	}

	/**
	 * start a API call
	 * 
	 * @param serviceType
	 * @param tnContext
	 * @return
	 */
	public static boolean startAPICall(String serviceType, TnContext tnContext)
	{
		if (!throttlingConf.isEnabled())
		{
			return true;
		}		

		for (int i = 0, size = serviceList.size(); i < size; i++)
		{
			Service service = (Service) serviceList.get(i);
			boolean isInService = service.isInService(serviceType);
			if (isInService)
			{
				/**
				 * can we add one more online user? If so, increase the online
				 * number and add context
				 */
				boolean hasReachMaxOnlineNumber = false;
				synchronized (service)
				{
					hasReachMaxOnlineNumber = service.hasReachMaxOnlineNumber();

					if (!hasReachMaxOnlineNumber)
					{

						service.increaseOnlineUser();
						if (tnContext != null)
						{
							service.addOnlineContext(tnContext, serviceType);
						}

						if (LOGGER.isDebugEnabled())
						{
							LOGGER
									.debug("increaseOnlineUser, total online number: "
											+ service.getOnlineNumber()
											+ " for service:" + serviceType);
//							for (int jj = 0; jj < service
//									.getOnlineContextList().size(); jj++)
//							{
//								LOGGER.debug("online context: "
//										+ service.getOnlineContextList()
//												.get(jj));
//							}
						}
					}
				}

				if (hasReachMaxOnlineNumber)
				{
					LOGGER.fatal("hasReach MaxOnlineNumber: "
							+ service.getMaxAllowedOnlineNumber()
							+ " for service:" + serviceType);
					//TODO: send out alert email
				}
				
				return !hasReachMaxOnlineNumber;

			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("No matched service:" + serviceType);
			LOGGER.debug("ending startServiceCall for service:" + serviceType);
		}
		return true;
	}

	/**
	 * end a API call
	 * 
	 * @param serviceType
	 * @param tnContext
	 */
	public static void endAPICall(String serviceType, TnContext tnContext)
	{
		if (!throttlingConf.isEnabled())
		{
			return;
		}

		for (int i = 0, size = serviceList.size(); i < size; i++)
		{
			Service service = (Service) serviceList.get(i);
			boolean isInService = service.isInService(serviceType);
			if (isInService)
			{
				synchronized (service)
				{
					service.decreaseOnlineUser();
					if (tnContext != null)
					{
						service.removeOnlineContext(tnContext);
					}

					if (LOGGER.isDebugEnabled())
					{
						LOGGER
								.debug("decreaseOnlineUser, total online number: "
										+ service.getOnlineNumber()
										+ " for service:" + serviceType);
					}
				}

				return;
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("No matched service for service:" + serviceType);
			LOGGER
					.debug("ending finishServiceCall for service:"
							+ serviceType);
		}
	}
}
