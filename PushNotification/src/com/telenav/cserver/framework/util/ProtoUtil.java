package com.telenav.cserver.framework.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import net.jarlehansen.protobuf.javame.ByteString;

import com.telenav.j2me.datatypes.BizPOI;
import com.telenav.j2me.datatypes.FavoriteCategory;
import com.telenav.j2me.datatypes.POI;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.StopPoiWrapper;
import com.telenav.j2me.framework.protocol.ProtoBasePoi;
import com.telenav.j2me.framework.protocol.ProtoBasePoiExtraInfo;
import com.telenav.j2me.framework.protocol.ProtoBizPoi;
import com.telenav.j2me.framework.protocol.ProtoFavoriteCategory;
import com.telenav.j2me.framework.protocol.ProtoProperty;
import com.telenav.j2me.framework.protocol.ProtoStop;
import com.telenav.j2me.framework.protocol.ProtoStopPoiWrapper;

public class ProtoUtil 
{
    
	/**
     * convert ProtoStop to regular Stop object
     * @param protoStop
     * @return
     */
	public static Stop convertProtoBufToStop(ProtoStop protoStop)
	{
	    if (protoStop == null)
	        return null;
	    
		Stop stop = new Stop();;
		stop.lat = protoStop.getLat();
		stop.lon = protoStop.getLon();
		stop.stopType = (byte)protoStop.getStopType();
		stop.stopStatus = (byte)protoStop.getStopStatus();
		stop.isGeocoded = protoStop.getIsGeocoded();
		stop.hashCode = protoStop.getHashCode();
		stop.isShareAddress = protoStop.getIsShareAddress();
		stop.label = protoStop.getLabel();
		stop.firstLine = protoStop.getFirstLine();
		stop.city = protoStop.getCity();
		stop.state = protoStop.getState();
		stop.stopId = protoStop.getStopId();
		stop.zip = protoStop.getZip();
		stop.country = protoStop.getCountry();
		stop.county = protoStop.getCounty();
		
		//add for ace4.0
		 stop.setExtraProperty(Stop.KEY_ADDRESSID, protoStop.getAddressId());
         stop.setExtraProperty(Stop.KEY_BUILDINGNAME, protoStop.getBuildingName());
         stop.setExtraProperty(Stop.KEY_LOCALE, protoStop.getLocale());
         stop.setExtraProperty(Stop.KEY_LOCALITY, protoStop.getLocality());
         stop.setExtraProperty(Stop.KEY_SUBLOCALITY, protoStop.getSublocality());
         stop.setExtraProperty(Stop.KEY_SUBSTREET, protoStop.getSubStreet());
         stop.setExtraProperty(Stop.KEY_SUITE, protoStop.getSuite());
         
         //add for ace4.0 in TN7.2
         stop.setExtraProperty( Stop.KEY_HOUSENUMBER , protoStop.getDoor() );
         stop.setExtraProperty( Stop.KEY_STREETNAME , protoStop.getStreetName() );
		 return stop;
	}
	
	public static ProtoStop convertStopToProtoBuf(Stop stop)
	{
	    if (stop == null)
	        return null;
	    
		ProtoStop.Builder builder = ProtoStop.newBuilder();
		builder.setLat(stop.lat);
		builder.setLon(stop.lon);
		builder.setStopType(stop.stopType);
		builder.setStopStatus(stop.stopStatus);
		builder.setIsGeocoded(stop.isGeocoded);
		builder.setHashCode(stop.hashCode);
		builder.setIsShareAddress(stop.isShareAddress);
		builder.setLabel(stop.label);
		builder.setFirstLine(stop.firstLine);
		builder.setCity(stop.city);
		builder.setState(stop.state);
		builder.setStopId(stop.stopId);
		builder.setZip(stop.zip);
		builder.setCountry(stop.country);
		builder.setCounty(stop.county);
		
		//add for ace4.0
		builder.setAddressId((String) stop.getExtraProperty( Stop.KEY_ADDRESSID ) );
		builder.setBuildingName((String) stop.getExtraProperty( Stop.KEY_BUILDINGNAME ) );
		builder.setLocale((String) stop.getExtraProperty( Stop.KEY_LOCALE ) );
		builder.setLocality((String) stop.getExtraProperty( Stop.KEY_LOCALITY ) );
		builder.setSublocality((String) stop.getExtraProperty( Stop.KEY_SUBLOCALITY ) );
		builder.setSubStreet((String) stop.getExtraProperty( Stop.KEY_SUBSTREET ) );
		builder.setSuite((String) stop.getExtraProperty( Stop.KEY_SUITE ) );
		
		//add for ace4.0 in TN7.2
		builder.setDoor( (String)stop.getExtraProperty( Stop.KEY_HOUSENUMBER ) );
		builder.setStreetName( (String)stop.getExtraProperty( Stop.KEY_STREETNAME ) );
		return builder.build();
	}
	
	public static BizPOI convertProtoBufToBizPOI(ProtoBizPoi protoBizPoi)
	{
	    if (protoBizPoi == null)
	        return null;
	    
	    BizPOI bizPoi = new BizPOI();
	    
	    Vector vSupportInfo = protoBizPoi.getSupportInfos();
	    if (vSupportInfo != null && vSupportInfo.size() > 0)
	    {
	        bizPoi.supportInfo = (String)vSupportInfo.get(0);
	    }
	    bizPoi.brand = protoBizPoi.getBrand();
	    bizPoi.phoneNumber = protoBizPoi.getPhoneNumber();
	    bizPoi.navigable = protoBizPoi.getNavigable();
	    bizPoi.distance = protoBizPoi.getDistance();
	    
	    Vector vSupplementalInfo = protoBizPoi.getSupplementalInfos();
        if (vSupplementalInfo != null && vSupplementalInfo.size() > 0)
        {
            bizPoi.supplementalInfo = (String)vSupplementalInfo.get(0);
        }
	    
	    bizPoi.parentCatName = protoBizPoi.getParentCatName();
	    bizPoi.price = protoBizPoi.getPrice();
	    bizPoi.groupID = protoBizPoi.getGroupID();
	    bizPoi.address = convertProtoBufToStop(protoBizPoi.getAddress());
	    bizPoi.layout = protoBizPoi.getLayout();
	    bizPoi.vendorCode = protoBizPoi.getVendorCode();
	    
	    return bizPoi;
	}
	
	public static ProtoBizPoi convertBizPOIToProtoBuf(BizPOI bizPoi)
    {
        if (bizPoi == null)
            return null;
        
        ProtoBizPoi.Builder builder = ProtoBizPoi.newBuilder();
        if (bizPoi.supportInfo != null)
        {
            Vector v = new Vector();
            v.add(bizPoi.supportInfo);
            builder.setSupportInfos(v);
        }
        
        if (bizPoi.supplementalInfo != null)
        {
            Vector v = new Vector();
            v.add(bizPoi.supplementalInfo);
            builder.setSupplementalInfos(v);
        }
        
        builder.setBrand(bizPoi.brand);
        builder.setPhoneNumber(bizPoi.phoneNumber);
        builder.setNavigable(bizPoi.navigable);
        builder.setDistance(bizPoi.distance);
        
        builder.setParentCatName(bizPoi.parentCatName);
        builder.setPrice(bizPoi.price);
        builder.setGroupID(bizPoi.groupID);
        ProtoStop pStop = convertStopToProtoBuf(bizPoi.address);
        if (pStop != null)
        {
            builder.setAddress(pStop);
        }
        builder.setLayout(bizPoi.layout);
        builder.setVendorCode(bizPoi.vendorCode);
        builder.setCategoryId(bizPoi.categoryID);
        
        return builder.build();
    }
	
	public static POI convertProtoBufToPoi(ProtoBasePoi protoPoi)
	{
	    if (protoPoi == null)
	        return null;
	    
	    POI poi = new POI();
	    
	    poi.bizPoi = convertProtoBufToBizPOI(protoPoi.getBizPoi());
	    poi.avgRating = protoPoi.getAvgRating();
	    poi.popularity = protoPoi.getPopularity();
	    poi.isRatingEnable = protoPoi.getIsRatingEnable();
	    poi.hasUserRatedThisPoi = protoPoi.getHasUserRatedThisPoi();
	    poi.poiId = protoPoi.getPoiId();
	    
	    if (protoPoi.getBasePoiExtraInfo() != null)
	    {
    	    Vector<ProtoProperty> protoPropertyVector = protoPoi.getBasePoiExtraInfo().getExtraProperties();
    	    if (protoPropertyVector != null)
    	    {
        	    for(ProtoProperty protoProperty: protoPropertyVector)
        	    {
        	        poi.setExtraProperty(protoProperty.getKey(), protoProperty.getValue());
        	    }
    	    }
    	    poi.setAudioPoiName(convertProtoBufToByteArray(protoPoi.getBasePoiExtraInfo().getPoiNameAudio()));
	    }
	    
	    
	    return poi;
	}
	
	public static ProtoBasePoi convertPoiToProtoBuf(POI poi)
    {
        if (poi == null)
            return null;
        
        ProtoBasePoi.Builder builder = ProtoBasePoi.newBuilder();
        
        ProtoBizPoi pBizPoi = convertBizPOIToProtoBuf(poi.bizPoi);
        if (pBizPoi != null)
        {
            builder.setBizPoi(pBizPoi);
        }
        builder.setAvgRating(poi.avgRating);
        builder.setPopularity(poi.popularity);
        builder.setIsRatingEnable(poi.isRatingEnable);
        builder.setHasUserRatedThisPoi(poi.hasUserRatedThisPoi);
        builder.setPoiId(poi.poiId);
        
//        Vector<ProtoProperty> protoPropertyVector = protoPoi.extraProperties;
//        for(ProtoProperty protoProperty: protoPropertyVector)
//        {
//            poi.setExtraProperty(protoProperty.getKey(), protoProperty.getValue());
//        }
        
        ProtoBasePoiExtraInfo.Builder pBasePoiExtraInfo = ProtoBasePoiExtraInfo.newBuilder();
        pBasePoiExtraInfo.setPoiNameAudio(convertByteArrayToProtoBuf(poi.getAudioPoiName()));
        builder.setBasePoiExtraInfo(pBasePoiExtraInfo.build());
        
        return builder.build();
    }
	
	public static HashMap convertProtoBufToHashMap(Vector<ProtoProperty> protoPropertyVector)
	{
	    if (protoPropertyVector == null)
	        return null;
	    
	    HashMap hashMap = new HashMap();
	    for(ProtoProperty protoProperty: protoPropertyVector)
	    {
	        hashMap.put(protoProperty.getKey(), protoProperty.getValue());
	    }
	    
	    return hashMap;
	}
	
	public static Map convertProtoBufToMap(Vector<ProtoProperty> protoPropertyVector)
    {
	    
        return convertProtoBufToHashMap(protoPropertyVector);
    }
	
    public static Vector<ProtoProperty> convertMapToProtoBuf(Map<String, String> map)
    {
        if (map == null)
            return null;
        
        Iterator<String> iterator = map.keySet().iterator();
        Vector<ProtoProperty> vector = new Vector<ProtoProperty>();
        while (iterator.hasNext())
        {
            String key = iterator.next();
            String value = map.get(key);
            vector.add(ProtoProperty.newBuilder().setKey(key).setValue(value).build());
        }
        
        return vector;
    }
	
	public static byte[] convertProtoBufToByteArray(ByteString byteString)
	{
	    if (byteString == null)
	        return null;
	    else
	        return byteString.toByteArray();
	}
	
	public static ByteString convertByteArrayToProtoBuf(byte[] buff)
    {
        if (buff == null)
            return null;
        else
            return ByteString.copyFrom(buff);
    }
	
	
	public static StopPoiWrapper convertProtoBufToStopPoiWrapper(ProtoStopPoiWrapper pStopPoiWrapper)
    {
	    if (pStopPoiWrapper == null)
	        return null;
	    
        StopPoiWrapper stopPoiWrapper = new StopPoiWrapper();
        stopPoiWrapper.type = (byte)pStopPoiWrapper.getType();
        stopPoiWrapper.status = (byte)pStopPoiWrapper.getStatus();
        stopPoiWrapper.label = pStopPoiWrapper.getLabel();
        stopPoiWrapper.id = pStopPoiWrapper.getId();
        stopPoiWrapper.eventId = pStopPoiWrapper.getEventId();
        stopPoiWrapper.sharedFromUser = pStopPoiWrapper.getSharedFromUser();
        stopPoiWrapper.sharedFromPTN = pStopPoiWrapper.getSharedFromPTN();
        stopPoiWrapper.categories = pStopPoiWrapper.getCategories();
        stopPoiWrapper.stop = convertProtoBufToStop(pStopPoiWrapper.getStop());
        stopPoiWrapper.poi = convertProtoBufToPoi(pStopPoiWrapper.getPoi());
        stopPoiWrapper.createdTimeInMillis = pStopPoiWrapper.getCreateTime();
        stopPoiWrapper.updatedTimeInMillis = pStopPoiWrapper.getUpdateTime();
        
        return stopPoiWrapper;
    }
	
	public static ProtoStopPoiWrapper convertStopPoiWrapperToProtoBuf(StopPoiWrapper pStopPoiWrapper)
    {
        if (pStopPoiWrapper == null)
            return null;
        
        ProtoStopPoiWrapper.Builder builder = ProtoStopPoiWrapper.newBuilder();
        builder.setType(pStopPoiWrapper.type);
        builder.setStatus(pStopPoiWrapper.status);
        builder.setLabel(pStopPoiWrapper.label);
        builder.setId(pStopPoiWrapper.id);
        builder.setEventId(pStopPoiWrapper.eventId);
        builder.setSharedFromUser(pStopPoiWrapper.sharedFromUser);
        builder.setSharedFromPTN(pStopPoiWrapper.sharedFromPTN);
        builder.setCategories(pStopPoiWrapper.categories);
        ProtoStop protoStop = convertStopToProtoBuf(pStopPoiWrapper.stop);
        if ( protoStop != null)
        {
            builder.setStop(convertStopToProtoBuf(pStopPoiWrapper.stop));
        }
        
        ProtoBasePoi protoPoi = convertPoiToProtoBuf(pStopPoiWrapper.poi);
        if (protoPoi != null)
        {
            builder.setPoi(protoPoi);
        }
        
        builder.setCreateTime(pStopPoiWrapper.createdTimeInMillis);
        builder.setUpdateTime(pStopPoiWrapper.updatedTimeInMillis);
        
        return builder.build();
    }
	
	public static FavoriteCategory convertProtoBufToFavoriteCategory(ProtoFavoriteCategory pFavoriteCategory)
	{
	    if (pFavoriteCategory == null)
	        return null;
	    
	    FavoriteCategory favoriteCategory = new FavoriteCategory();
	    favoriteCategory.id = pFavoriteCategory.getId();
	    favoriteCategory.catType = (byte)pFavoriteCategory.getCatType();
	    favoriteCategory.name = pFavoriteCategory.getName();
	    favoriteCategory.status = (byte)pFavoriteCategory.getStatus();
	    favoriteCategory.createdTimeInMillis = pFavoriteCategory.getCreateTime();
	    favoriteCategory.updatedTimeInMillis = pFavoriteCategory.getUpdateTime();
	    
	    return favoriteCategory;
	}


	public static ProtoFavoriteCategory convertFavoriteCategoryToProtoBuf(FavoriteCategory favoriteCategory)
    {
        if (favoriteCategory == null)
            return null;
        
        ProtoFavoriteCategory.Builder builder = ProtoFavoriteCategory.newBuilder();
        builder.setId(favoriteCategory.id);
        builder.setCatType(favoriteCategory.catType);
        builder.setName(favoriteCategory.name);
        builder.setStatus(favoriteCategory.status);
        builder.setCreateTime(favoriteCategory.createdTimeInMillis);
        builder.setUpdateTime(favoriteCategory.updatedTimeInMillis);
        
        return builder.build();
    }
	
	
	public static long[] convertProtoBufToLong(Vector<Long> longVector)
    {
        if (longVector == null)
            return null;
        
        long[] longArray = new long[longVector.size()];
        for(int i=0; i<longVector.size(); i++)
        {
            longArray[i] = longVector.get(i);
        }
        return longArray;
    }
	
	
	public static Vector<Long> convertLongToProtoBuf(long[] longArray)
    {
        if (longArray == null)
            return null;
        
        Vector<Long> longVector = new Vector<Long>();
        for(long longValue: longArray)
        {
            longVector.add(longValue);
        }
        return longVector;

    }
	
	
	public static Vector<ProtoStopPoiWrapper> toStopPoiWrapperVector(Vector<StopPoiWrapper> stopPoiWrapperVector)
    {
        if (stopPoiWrapperVector == null)
            return null;
        
        
        Vector<ProtoStopPoiWrapper> pStopPoiWrapperVector = new Vector<ProtoStopPoiWrapper>();
        for(StopPoiWrapper stopPoiWrapper : stopPoiWrapperVector)
        {
            pStopPoiWrapperVector.add(ProtoUtil.convertStopPoiWrapperToProtoBuf(stopPoiWrapper));
        }
        
        return pStopPoiWrapperVector;
    }
    
	public static Vector<ProtoFavoriteCategory> toFavoriteCategoryVector(Vector<FavoriteCategory> favoriteCategoryVector)
    {
        if (favoriteCategoryVector == null)
            return null;
        
        
        Vector<ProtoFavoriteCategory> pFavoriteCategory = new Vector<ProtoFavoriteCategory>();
        for(FavoriteCategory favoriteCategory : favoriteCategoryVector)
        {
            pFavoriteCategory.add(ProtoUtil.convertFavoriteCategoryToProtoBuf(favoriteCategory));
        }
        
        return pFavoriteCategory;
    }
	
	
	
	
	
}
