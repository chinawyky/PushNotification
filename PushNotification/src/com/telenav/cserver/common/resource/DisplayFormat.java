/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

/**
 * DisplayFormat.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-10-27
 *
 */
interface DisplayFormat
{
    final static String STATISTIC = "Statistic";

    final static String COUNT_OF_HOLDER_TYPE = "Holder Type:";

    final static String COUNT_OF_HOLDER = "Holders:";

    final static String TOTAL_MEMORY_OF_CACHE = "Memory:";

    final static String TOTAL_COUNT_OF_ACCESS = "Access Count:";

    final static String MISS_COUNT = "Miss Count:";

    final static String MISS_RATE = "Miss Rate:";

    final static String EVICT_CONFIGURATION = "Evict Configuration";

    final static String EVICT_CACHE_THRESHOLD = "Cache Threshold:";

    final static String EVICT_INTERVAL_DURATION = "Evict Interval Duration:";

    final static String HOLDER = "Holder";

    final static String HOLDER_TYPE = "Holder Type";

    final static String HOLDER_NAME = "Holder Name";

    final static String COUNT_OF_CACHE_OBJECT = "Counter Of Object";

    final static String MEMORY_OF_HOLDER = "Memory";

    final static String HOLDER_DETAILS = "Details";

    final static String MORE_THAN_ONE_HOLDER = "THERE ARE MORE THAN ONE HOLDER OF THE NAME OF ";

    final static String NO_HOLDER = "THERE AREN'T ANY Holder OF THE NAME OF ";

    public String statistic();

    public String details();

    /**
     * display the holder information of the name, if there are more than one of the name the function return anyone
     * 
     * @param name
     * @return
     */
    public String details(String holderName);
    
    public String contents(String holderName, String key);
    
    public String viewCachedClient(String holderName, String key);
}

