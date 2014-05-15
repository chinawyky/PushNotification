/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Code from sowmi.
 * 
 * @author sowmi
 * 
 */
public class LogLevelModifier {
	private static final Logger log = Logger.getLogger(LogLevelModifier.class);

	/**
	 * Constructor.
	 */
	public LogLevelModifier() {

	}

	public void modify(String level, String loggerName) {
		if (loggerName != null && level != null) {
			Logger logger = Logger.getLogger(loggerName);
			Level prevLevel = logger.getLevel();
			logger.setLevel(Level.toLevel(level, prevLevel));
			log.info("Modified level of " + loggerName + " from " + prevLevel
					+ " to " + logger.getLevel());
		}
	}

}
