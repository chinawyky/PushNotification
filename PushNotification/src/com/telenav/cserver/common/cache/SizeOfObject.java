/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.cache;

/**
 * SizeOfObject.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-1-21
 *
 */
public interface SizeOfObject {
	
	//long sizeOf(Object object);
	long deepSizeOf(Object object);
	String humanReadable(long size);
   
}

