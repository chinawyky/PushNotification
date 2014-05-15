/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.cache;

/**
 * Count.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-2-2
 *
 */
public interface Counter {
	
	public long getAndAdd(long delta);
	public long getAndSet(long newValue);
	public long getAndIncrement();
	public long value();

}
