/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.resource.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * RegionGroup.java
 * hold the region -- region_group mapping
 * @author jzhu@telenav.cn
 * @version 1.0 2012-3-15
 *
 */
public final class RegionGroup
{
    public static final String REGION_GROUP_NA = "NA";
    public static final String REGION_GROUP_EU = "EU";
    public static final String REGION_GROUP_SA = "SA";
    
    
    private final List<String> regionEUGroup = new ArrayList<String>();
    private final List<String> regionNAGroup = new ArrayList<String>();
    private final List<String> regionSAGroup = new ArrayList<String>();

    private Map<String, List<String>> regionGroupMap = new HashMap<String, List<String>>();
    
    private static RegionGroup instance = new RegionGroup();
    
    private RegionGroup()
    {
        init();
    }
    
    
    /**
     * return the region group
     * if it does not belong to any group, return itself.
     * @param region
     * @return
     */
    public static String getRegionGroup(String region)
    {
        Iterator<String> iterator = instance.regionGroupMap.keySet().iterator();
        while (iterator.hasNext())
        {
            String regionGroup = iterator.next();
            List<String> regionList = instance.regionGroupMap.get(regionGroup);
            if (regionList.contains(region))
            {
                return regionGroup;
            }
            
        }
        
        return region;
    }
    
    
    private void init()
    {
        initEU();
        initNA();
        initSA();
        
        regionGroupMap.put(REGION_GROUP_NA, regionNAGroup);
        regionGroupMap.put(REGION_GROUP_EU, regionEUGroup);
        regionGroupMap.put(REGION_GROUP_SA, regionSAGroup);
    }


    private void initEU()
    {
        //regionEUGroup.add("IN");
        regionEUGroup.add("GB");
        regionEUGroup.add("IT");
        regionEUGroup.add("FR");
        regionEUGroup.add("DE");
        regionEUGroup.add("AT");
        regionEUGroup.add("ES");
        regionEUGroup.add("CH");
        regionEUGroup.add("PT");
        regionEUGroup.add("IE");
        regionEUGroup.add("NL");
        regionEUGroup.add("BE");
        regionEUGroup.add("LU");
        regionEUGroup.add("DK");
        regionEUGroup.add("SE");
        regionEUGroup.add("NO");
        regionEUGroup.add("FI");
        regionEUGroup.add("MC");
        regionEUGroup.add("LI");
        regionEUGroup.add("AD");
        regionEUGroup.add("GR");
        regionEUGroup.add("IM");
        regionEUGroup.add("GI");
        regionEUGroup.add("SM");
        regionEUGroup.add("VA");
    }
    
    private void initNA()
    { 
        regionNAGroup.add("US");
        regionNAGroup.add("CA");
        regionNAGroup.add("MX");
        //regionNAGroup.add("BR");
        regionNAGroup.add("PR");
        regionNAGroup.add("VI");
    }
    
    private void initSA()
    {
        regionSAGroup.add("BR");
    }
    
}
