/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.cache;

import java.util.concurrent.atomic.AtomicLong;

/**
 * AtomicCounter.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-2-2
 *
 */
public class AtomicCounter implements Counter {
    private AtomicLong counter;
    public AtomicCounter(long value){
    	counter = new AtomicLong(value);
    }
	@Override
	public long getAndAdd(long delta) {
		return counter.getAndAdd(delta);
	}

	@Override
	public long getAndSet(long newValue) {
		return counter.getAndSet(newValue);
	}
	@Override
	public long getAndIncrement() {
		return counter.getAndIncrement();
	}
	@Override
	public long value() {
		return counter.longValue();
	}
}
