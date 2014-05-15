/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Monitor.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Jul 4, 2011
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitor
{ 
    public String server();
    public String parserClass();
    public String filePath() default "";
    public String[] serviceUrlKeys() default "";
}
