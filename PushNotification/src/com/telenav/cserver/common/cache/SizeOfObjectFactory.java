/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.cache;

/**
 * SizeOfObjectFactory.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-1-29
 *
 */
public class SizeOfObjectFactory{
	private final static SizeOfObject sizeOfObject = new SizeOfObjectImpl();
	
	public static SizeOfObject getInstance(){
		return sizeOfObject;
	}
	
	private SizeOfObjectFactory(){}
	
}