/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.telenav.cserver.common.resource.constant.ResourceConst;
import com.telenav.cserver.common.resource.ext.BarIterationResourceLoader;
import com.telenav.cserver.common.resource.ext.BinDataResourceLoader;
import com.telenav.cserver.common.resource.ext.NativeResourecBundleLoader;
import com.telenav.cserver.common.resource.ext.ResourceBundleLoader;
import com.telenav.cserver.common.resource.ext.SpringResourceLoader;
import com.telenav.cserver.common.resource.ext.XmlResourceLoader;
import com.telenav.cserver.common.resource.fstree.Search;
import com.telenav.cserver.common.resource.fstree.TreeConstructor;
import com.telenav.cserver.common.resource.orders.FileNameLoadOrder;
import com.telenav.cserver.common.resource.orders.OrLoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;
import com.telenav.cserver.framework.management.jmx.IBackendServerMonitor;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * Resource Factory
 * 
 * @author yqchen
 * @version 1.0 2007-2-6 10:28:43
 */
public final class ResourceFactory 
{
	public static final String TYPE_RESOURCE_BUNDLE = "resource_bundle";
	public static final String SEPARATOR = "/";
	public static final String TYPE_XML = "xml";
	
	private static Logger LOGGER = Logger.getLogger(ResourceFactory.class);
	private static final String TYPE_BIN = "bin";
	private static final String NATIVE_TYPE_RESOURCE_BUNDLE = "native_resource_bundle";
	private static final String TYPE_SPRING = "spring";
	private static final String TYPE_BAR_ITERATION = "bar_iteration";
	private static final String NAME = "name";
	private static final String EXCEPTION_WHEN_INIT = "Exception when initiate hodler:";
	
	private  static IBackendServerMonitor backendMonitor;
	
	/**
	 * initiate resource, should be called at system startup
	 *
	 */
	public static void init()
	{
		    try
			{
				ResourceLoaderConfig.init();
			    TreeConstructor.init();
			    backendMonitor = (IBackendServerMonitor)Configurator.getObject("management/jmx.xml","backendServerMonitor");
			} catch (ConfigurationException e)
			{
				LOGGER.fatal(e.getMessage());
			} catch (IOException e)
			{
				LOGGER.fatal(e.getMessage());
			}

	}
	
	public IBackendServerMonitor getBackendServerMonitor(){
	    return backendMonitor;
	}
	
	
	private static ResourceBundleLoader resourceBundleLoader = new ResourceBundleLoader();
	private static XmlResourceLoader xmlResourceLoader = new XmlResourceLoader();
	private static BinDataResourceLoader binResourceLoader = new BinDataResourceLoader();
	private static NativeResourecBundleLoader nativeResourecBundleLoader = new NativeResourecBundleLoader();
	private static SpringResourceLoader springResourceLoader = new SpringResourceLoader();
	private static BarIterationResourceLoader barIterationResourceLoader = new BarIterationResourceLoader();
    
    private ResourceFactory()
    {
        init();
    }
    
    private static ResourceFactory instance = new ResourceFactory();
    
    public static ResourceFactory getInstance()
    {
        return instance;
    }
    
    /**
     * create default/common resource
     */
    public Object createObject(Object key, Object argument)
    {
        String configPath = (String)key;
        ResourceLoadMeta meta = (ResourceLoadMeta)argument;
        String type = meta.type;
        String objectName = meta.objectName;
        ResourceLoader resourceLoader = getResourceLoader(type);
        return resourceLoader.loadResource(configPath, objectName);
    }
    
    /**
     * get a object from cache, disabled cache
     * 
     * @param key
     * @param argument argument to create the object
     */
    public Object get(Object key,Object argument)
    {
        return createObject(key, argument);     
    }
    
    private static class ResourceLoadMeta
    {
        private String type;
        private String objectName;
    }
    
    public static Object createResource(
            ResourceHolder holder, UserProfile profile, TnContext tnContext)
    {
        String type = holder.getType();
        Object result = null;
        
        ResourceLoadMeta meta = new ResourceLoadMeta();
        meta.type = type;
        
        //handle spring type loader and pass on its object name
        if(type.equals(TYPE_SPRING)&&holder instanceof SpringObjectNameAware)
        {
            meta.objectName=((SpringObjectNameAware)holder).getObjectName();
        }
        
        List<LoadOrder> orders = holder.getLoadOrders().getOrders();
        
        String path = Search.search(TreeConstructor.getTree(), orders, 0, profile, tnContext);
        
        if(StringUtils.isBlank(path))
        {
            path = TreeConstructor.ROOT_PATH + SEPARATOR + getConfigSuffix(orders, profile, tnContext);
        }
        else 
        {
            //We have two source path for device conf, one is under zipFolder/***zipFolder and the other is under serviceLocator/device.
            String zipFolderPath = TreeConstructor.ZIP_FOLDER_PATH + SEPARATOR + path;
            LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>External: " + zipFolderPath);
            result = instance.get(zipFolderPath, meta);
            if (result == null)
            {
            	LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + path);
                result = instance.get(path, meta);                    
            }
            else 
            {
            	path = zipFolderPath;

            }
        }
        
        holder.addUserProfileKeyMapping(((AbstractResourceHolder)holder).getKey(profile, tnContext), path);
        return result;
    }
    
    /**
     * get suffix to append in config file name
     * 
     * @param index order index
     * @return
     */
    public static String getConfigSuffix(List<LoadOrder> orders, UserProfile profile,
            TnContext tnContext)
    {
        if(orders.size() == 0)
        {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ;i < orders.size(); i = i + 1)
        {
            LoadOrder order = orders.get(i);
            sb.append(order.getAttributeValue(profile, tnContext));
            sb.append(SEPARATOR);
        }
        
        return sb.substring(0, sb.length() - 1);
    }
    
    public static String getConfigSuffixForKey(List orders, UserProfile profile, TnContext tnContext)
    {
        return getConfigSuffixForKey(orders, orders.size() - 1, profile, tnContext);
    }
    
    public static String getConfigSuffixForKey(List<LoadOrder> orders, int index, UserProfile profile,
            TnContext tnContext)
    {
        if(orders.size() == 0)
        {
            return "";
        }
        
        if(index >= orders.size() || index < 0)
        {
             throw new IndexOutOfBoundsException(
                        "Index: " + index +", Size: " + orders.size());
        }
        
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ;i <= index; i = i + 1)
        {
            sb.append(SEPARATOR); 
            LoadOrder order = orders.get(i);
            if( order instanceof OrLoadOrder )
            {
                List<String> list = order.getAttributeValueList(profile, tnContext);
                StringBuffer stringBuffer = new StringBuffer();
                for(String str : list)
                {
                    stringBuffer.append(str).append("_");
                }
                String str = stringBuffer.toString();
                //remove the last '_'
                if( str.length() > 0 )
                {
                    str = str.substring(0,str.length()-1);
                }
                sb.append(str);
                
            }
            else{
                sb.append(order.getAttributeValue(profile, tnContext));
            }
        }
        
        
        return sb.toString();
    }
    

    public static Object createResource(ResourceHolder holder)
    {
        return createResource(holder,  null, null);
    }
    
    public static ResourceLoader getResourceLoader(String type)
    {
        if(type.equalsIgnoreCase(TYPE_RESOURCE_BUNDLE))
        {
            return resourceBundleLoader;
        }
        else if(type.equalsIgnoreCase(TYPE_XML))
        {
            return xmlResourceLoader;
        }
        else if(type.equalsIgnoreCase(TYPE_BIN))
        {
            return binResourceLoader;
        }else if(type.equalsIgnoreCase(NATIVE_TYPE_RESOURCE_BUNDLE))
        {
            return nativeResourecBundleLoader;
        }else if(type.equalsIgnoreCase(TYPE_SPRING))
        {
            return springResourceLoader;
        }else if(type.equalsIgnoreCase(TYPE_BAR_ITERATION))
        {
            return barIterationResourceLoader;
        }
        
        
        throw new IllegalArgumentException("Invalid type to get ResourceLoader:" + type);
    }
    
    public static String getVersion(UserProfile profile)
    {
        String version = profile.getVersion();
        String[] suffix = {".d",".t"};
        for(int i=0; i<suffix.length; i = i + 1)
        {
            int idx = version.indexOf(suffix[i]);
            if (idx>0)
            {
                version = version.substring(0,idx);
            }
        }
        return version;
    }
    


    /**
     * @param versionConfig
     * @param type_resource_bundle2
     * @return
     */
    public static Object createResource(String configPath, String type) 
    {
        ResourceLoadMeta meta = new ResourceLoadMeta();
        meta.type = ResourceFactory.TYPE_RESOURCE_BUNDLE;
        return ResourceFactory.getInstance().get(configPath, 
                meta);
    }


    

private static class ResourceLoaderConfig
{
	private static final String CONFIG_FILE = "device/resource_loader.xml";
    private static final String SET_CONFIG_ROOT = "sets";
    private static final String SET_NODE = "set";
    private static final String HOLDER_CONFIG_ROOT = "holders";
    private static final String HOLDER_NODE = "holder";
    private static final String RESOURCE_PATH = "device/";
    
    private ResourceLoaderConfig()
    {
    	//do nothing
    }

    static void parseConfig()
    {
        // 1. read set config and store in a Map
        // load config file
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream stream =  cl.getResourceAsStream(CONFIG_FILE);
//      InputStream stream = ResourceLoaderConfig.class
//      .getResourceAsStream(CONFIG_FILE);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);

        try
        {
            // parse config
            Document doc = factory.newDocumentBuilder().parse(stream);
            Element root = doc.getDocumentElement();
            NodeList childNodes = root.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i = i + 1)
            {
                Node childNode = childNodes.item(i);
                String nodeName = childNode.getNodeName();
                if (SET_CONFIG_ROOT.equalsIgnoreCase(nodeName))
                {
                    // parse config in <sets></sets>
                    parseSetConfig(childNode);
                } else if (HOLDER_CONFIG_ROOT.equalsIgnoreCase(nodeName))
                {
                    // parse config in <holders></holders>
                    parseHolderConfig(childNode);
                }
            }
            
        }
        catch (SAXException e)
		{
        	LOGGER.fatal(e.getMessage());
		} catch (IOException e)
		{
			LOGGER.fatal(e.getMessage());
		} catch (ParserConfigurationException e)
		{
			LOGGER.fatal(e.getMessage());
		}

        // 2. read hodler config, construct holder and call init()
    }
    
    private static void parseSetConfig(Node node)
    {
        Element element = (Element) node;

        NodeList nodeList = element.getChildNodes();
        // parse elements
        for (int i = 0; i < nodeList.getLength(); i = i + 1)
        {
            Node child = nodeList.item(i);

            if (SET_NODE.equalsIgnoreCase(child.getNodeName()))
            {
                Element serverElement = (Element) child;

                String name = serverElement.getAttribute(NAME);
                String path = serverElement.getAttribute("path");

                LOGGER.info("Set name: " + name + ",path=" + path);
        //      System.out.println("Set name: " + name + ",path=" + path);
                ResourceSetManager.getInstance().addResourceSet(name, path);
            }
        }
    }

    private static void parseHolderConfig(Node node)
    {
        Element element = (Element) node;

        NodeList nodeList = element.getChildNodes();
        // parse elements
        for (int i = 0; i < nodeList.getLength(); i =  i + 1)
        {
            Node child = nodeList.item(i);

            if (HOLDER_NODE.equalsIgnoreCase(child.getNodeName()))
            {
                Element serverElement = (Element) child;

                String name = serverElement.getAttribute(NAME);
                String className = serverElement.getAttribute("class");

                String set = serverElement.getAttribute(SET_NODE);
                String configFile = serverElement.getAttribute("config_path");
                String type = serverElement.getAttribute("type");
                String orders = serverElement.getAttribute("structure_orders");
                String filenameLowerCase = serverElement.getAttribute("filename_lower_case");

                ResourceHolder holder = newHodler(className);
                holder.setName(name);
                holder.setConfigPath(configFile);
                holder.setResourceSet(set);
                holder.setType(type);
                
                boolean lowerCase = filenameLowerCase != null 
                && "true".equalsIgnoreCase(filenameLowerCase);
                LoadOrders structureOrders = new LoadOrders();
                structureOrders.addOrderString(orders);
                structureOrders.setLowerCase(lowerCase);
                
                // The file is taken as a LoadOrder when search. 
                // For service locator, as it is configured as "serviceLocator/service_mapping", it will be taken as two FileLoadOrder
                String[] fileNames = configFile.split(SEPARATOR);
                for (String temp : fileNames)
                {
                    structureOrders.addOrder(new FileNameLoadOrder(temp));
                }
                
                holder.setLoadOrders(structureOrders);
                
                holder.init();
                
                ResourceHolderManager.register(holder);
                
                LOGGER.info("Holder name: " + name + ",className=" + className
                        + ",set=" + set + ",configFile=" + configFile
                        + ",type=" + type 
                        + ",structureOrders=" + orders);
            }
        }
    }

    private static ResourceHolder newHodler(String className)
    {
            Class clazz;
			try
			{
				clazz = Class.forName(className);
				return (ResourceHolder)clazz.newInstance();
			} catch (ClassNotFoundException e)
			{
				LOGGER.fatal(ResourceConst.NO_GETINSTANCE_IN_CLASS + className);
				LOGGER.fatal(EXCEPTION_WHEN_INIT + className, e);
			} catch (InstantiationException e)
			{
				LOGGER.fatal(ResourceConst.NO_GETINSTANCE_IN_CLASS + className);
				LOGGER.fatal(EXCEPTION_WHEN_INIT + className, e);
			} catch (IllegalAccessException e)
			{
				LOGGER.fatal(ResourceConst.NO_GETINSTANCE_IN_CLASS + className);
				LOGGER.fatal(EXCEPTION_WHEN_INIT + className, e);
			}

        return null;
    }
    
    private static void init() throws ConfigurationException {
        Configurator.loadConfigFile(ResourceLoaderConfig.RESOURCE_PATH + "/resource_load_orders.xml");
        ResourceLoaderConfig.parseConfig();
    }
   
}

}