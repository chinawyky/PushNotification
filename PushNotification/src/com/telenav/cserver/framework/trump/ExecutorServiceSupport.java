/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.trump;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * ExecutorServiceSupport class, in fact a ExecutorService wrapper.
 * @author kwwang
 * @date 2009-12-21
 */
public class ExecutorServiceSupport {
	
	private static final ThreadFactory deamonFactory=new DeamonThreadFactory();
	/**
	 * Why the size of the thread pool is 2, is because that while JMX is listening on a specific port, the thread is blocked, can not be used to execute other tasks.
	 */
	private static final ScheduledExecutorService scheduledService=Executors.newScheduledThreadPool(2,deamonFactory);
	
	private static final int initialDelay=1;
	
	public static void sumbit(Runnable run)
	{
		scheduledService.schedule(run, initialDelay, TimeUnit.SECONDS);
	}
	
	public static void sumbitWithSecondPeriod(Runnable run,int period)
	{
		scheduledService.scheduleAtFixedRate(run, 0, period, TimeUnit.SECONDS);
	}
	
	public static void sumbitWithMinutePeriod(Runnable run,int period)
	{
		scheduledService.scheduleAtFixedRate(run, 0, period, TimeUnit.MINUTES);
	}
	
	public static void sumbitWithHourPeriod(Runnable run,int period)
	{
		scheduledService.scheduleAtFixedRate(run, 0, period, TimeUnit.HOURS);
	}
	
	public static void sumbitWithHourPeriod(Runnable run,long delay,int period)
	{
		scheduledService.scheduleAtFixedRate(run, delay, period, TimeUnit.HOURS);
	}
	
	public static void stop()
	{
		scheduledService.shutdown();
	}
	
	public static void stopNow()
	{
		scheduledService.shutdownNow();
	}
	
	static class DeamonThreadFactory implements ThreadFactory
	{
		@Override
		public Thread newThread(Runnable r) {
			Thread t=new Thread(r);
			t.setDaemon(true);
			return t;
		}
		
	}
}
