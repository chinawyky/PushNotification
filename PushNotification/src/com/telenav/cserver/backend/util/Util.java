/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * Util.java
 *
 */
package com.telenav.cserver.backend.util;

import java.util.Date;

/**
 *@author lwen (lwen@telenavsoftware.com)
 *@date Oct 24, 2013
 */
public class Util {
	public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units
	
	public static String getTrxId()
	{
		Date today = new Date();
		//using date + ptn as trxn id
		String trxnId = String.valueOf(today.getTime());
		return trxnId;
	}
	
	public static String getString(String string)
	{
		if(string == null) return "";
		
		return string.trim();
	}
	
	public static int convertToDM5(double degree) {
        return (int) (degree * DEGREE_MULTIPLIER);
    }
}
