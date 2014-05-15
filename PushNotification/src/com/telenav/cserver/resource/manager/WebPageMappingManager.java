package com.telenav.cserver.resource.manager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Category;

import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.common.resource.device.DeviceProperties;
import com.telenav.cserver.common.resource.device.DevicePropertiesHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;
import com.telenav.cserver.framework.util.UrlUtil;
import com.telenav.cserver.resource.common.ServiceLocatorHolder;
import com.telenav.cserver.resource.datatypes.ServiceItem;
import com.telenav.cserver.resource.datatypes.ServiceMapping;
import com.telenav.cserver.resource.datatypes.WebPageMapping;
import com.telenav.cserver.resource.datatypes.WebPageMappingItem;
import com.telenav.kernel.util.datatypes.TnContext;

public class WebPageMappingManager
{
    public static final String POI_SERVICE = "poi";
    
    public static final String POI_SERVICE_URL_ENTRY = "poi.http";
    
    public static final String POI_DETAIL_URL = "poidetail";
    
 	public static final String LOGIN_SERVICE = "login";
    
    public static final String LOGIN_SERVICE_URL_ENTRY = "login.http";
    
    public static final String LOGIN_WELCOME_URL = "loginWelcome";
    
    public static final String LOGIN_EMAIL_LOGIN_URL = "loginEmailLogin";
    
    public static final String LOGIN_UPSELL_URL = "needUpsellURL";
    
    private static WebPageMapping webPageMapping;
    
    private static Category logger = Category.getInstance(WebPageMappingManager.class);
    
    static ServiceLocatorHolder serviceLocatorHolder = (ServiceLocatorHolder)ResourceHolderManager.getResourceHolder(HolderType.SERVICE_LOCATOR_TYPE);
    static DevicePropertiesHolder devicePropertiesHolder = (DevicePropertiesHolder)ResourceHolderManager.getResourceHolder(HolderType.DEVICE_TYPE);

    
    private static Pattern urlPattern = Pattern.compile(".+://[^/]*");
    
    private WebPageMappingManager()
    {
        
    }
    
    static
    {
        
        try
        {           
            webPageMapping = (WebPageMapping)Configurator.getObject("web-page-mapping.xml","web_page_mapping");
            
            if(logger.isDebugEnabled())
            {
                logger.debug("web_page_mapping:" + webPageMapping);
            }
        }
        catch(ConfigurationException e)
        {
            logger.fatal("initWebPageMapping", e);
        }
        
    }
    
    
    public static String getPoiDetailUrl(UserProfile userProfile, TnContext tnContext)
    {
        String domainUrl = getDomainUrl(userProfile, tnContext, POI_SERVICE, POI_SERVICE_URL_ENTRY);
        String subWebPageUrl = getSubWebPageUrl(POI_SERVICE, POI_DETAIL_URL);
        
        String url = domainUrl + subWebPageUrl;
        
        return WebPageMappingManager.processUrl(url, userProfile, tnContext);
    }
    
    public static String getWelcomeUrl(UserProfile userProfile, TnContext tnContext)
    {
    	return getUrl(userProfile, tnContext, LOGIN_SERVICE, LOGIN_SERVICE_URL_ENTRY, LOGIN_WELCOME_URL);
    }
    
    public static String getEmailLoginUrl(UserProfile userProfile, TnContext tnContext)
    {
    	return getUrl(userProfile, tnContext, LOGIN_SERVICE, LOGIN_SERVICE_URL_ENTRY, LOGIN_EMAIL_LOGIN_URL);
    }
    
    public static String getUpsellUrl(UserProfile userProfile, TnContext tnContext)
    {
    	return getUrl(userProfile, tnContext, LOGIN_SERVICE, LOGIN_SERVICE_URL_ENTRY, LOGIN_UPSELL_URL);
    }
    
    public static String getUrl(UserProfile userProfile, TnContext tnContext,String webPageService,String webPageUrlEntry,String webPageKey)
    {
    	 String domainUrl = getDomainUrl(userProfile, tnContext, webPageService, webPageUrlEntry);
         String subWebPageUrl = getSubWebPageUrl(webPageService, webPageKey);
         
         String url = domainUrl + subWebPageUrl;
         
         return WebPageMappingManager.processUrl(url, userProfile, tnContext);

    }
    
    public static String getDomainUrl(UserProfile userProfile, TnContext tnContext, String service, String entry)
    {
        ResourceContent rs = serviceLocatorHolder.getResourceContent(userProfile, tnContext);
        ServiceMapping serviceMapping = (ServiceMapping)rs.getObject();
        
        ServiceItem serviceItem =  getServiceItem(serviceMapping, service);
        if (serviceItem == null)
            return "";
        
        String url = serviceItem.getUrlMap().get(entry);
        
        if (url == null)
            return "";
        
        else
        {
        	Matcher matcher = urlPattern.matcher(url);
        	
        	if(matcher.find()){
        		return matcher.group(0);
        	}
        	
        	return "";
        }
    }
    
    public static String getSubWebPageUrl(String service, String urlEntry)
    {
        String subWebPageUrl = "";
        if (webPageMapping == null)
            return subWebPageUrl;
        
        List<WebPageMappingItem> webPageMappingItemList = webPageMapping.getWebPageMapping();
        for(WebPageMappingItem webPageMappingItem : webPageMappingItemList)
        {
            if (webPageMappingItem.getService().equals(service))
            {
                subWebPageUrl = webPageMappingItem.getWebpageMapping().get(urlEntry);
                if (subWebPageUrl == null)
                    subWebPageUrl = "";
                break;
            }
        }
        
        return subWebPageUrl;
    }
    
    private static ServiceItem getServiceItem(ServiceMapping serviceMapping, String type)
    {
        if (serviceMapping == null)
            return null;
        
        List<ServiceItem> serviceItemList = serviceMapping.getServiceMapping();
        for(ServiceItem serviceItem : serviceItemList)
        {
            if (serviceItem != null && serviceItem.getType().equals(type))
            {
                return serviceItem;
            }
        }
        
        return null;
    }
    
    
    public static String processUrl(String url, UserProfile userProfile, TnContext tc)
    {
        DeviceProperties dp = devicePropertiesHolder.getDeviceProperties(userProfile, tc);
        int flag = dp.getInt("APPEND_CLIENTINFO_AFTER_BSERVER_URL", 1); 
        if (flag == 1)
            return UrlUtil.appendClientInfo(url.toString(), userProfile);
        else
            return url.toString();
    }

}
