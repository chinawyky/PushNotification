/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx;

/**
 * Exception util class
 * @author kwwang
 *
 */
public class ExceptionUtil {
	private static final String NEW_LINE="\n";
	/**
	 * Collect all the stack trace messages
	 * @param t
	 * @return
	 */
	public static String collectExceptionMsg(Throwable t) {
		StringBuilder builder = new StringBuilder();

		if (t == null)
			return builder.toString();

		Throwable th = t;
		//appendExceptionMsg(th, builder);
		builder.append(th.toString());
		while (th.getCause() != null) {
			th = th.getCause();
			//appendExceptionMsg(th, builder);
			builder.append(NEW_LINE);
			builder.append("  Caused By:");
			builder.append(th.toString());
		}
		return builder.toString();
	}

	/*private static void appendExceptionMsg(Throwable t, StringBuilder builder) {
		builder.append(NEW_LINE).append(t.toString());
		for (StackTraceElement st : t.getStackTrace()) {
			builder.append(NEW_LINE).append(st.toString());
		}
	}*/
}
