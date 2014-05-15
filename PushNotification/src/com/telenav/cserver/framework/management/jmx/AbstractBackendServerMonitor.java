/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.management.jmx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.management.jmx.config.Monitor;
import com.telenav.cserver.framework.management.jmx.config.ServiceUrlParser;
import com.telenav.cserver.framework.management.jmx.config.ServiceUrlParserFactory;

/**
 * CServerConfiguration.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 Jun 23, 2011
 * 
 */

public abstract class AbstractBackendServerMonitor implements IBackendServerMonitor
{
    private static Logger logger = Logger.getLogger(AbstractBackendServerMonitor.class);

    protected List<BackendServerConfiguration> backendServers;

    public AbstractBackendServerMonitor()
    {
        backendServers = new ArrayList<BackendServerConfiguration>();
        init();
    }

    private void init()
    {
        Method methods[] = getClass().getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            Method monitorMethod = methods[i];
            
            Monitor monitor = (Monitor) monitorMethod.getAnnotation(Monitor.class);
            //check whether this is a load configuration method
            if (monitor == null)
                continue;
            
            //check signature of monitor method, it should has none parameter, and return DetectResult as result
            if (!checkSignatureOfMonitorMethod(monitorMethod))
            {
                logger.fatal("MonitorMethod["+monitorMethod.getName()+"()] should none parameter, and its return type should be DetectResult.class");
                break;
            }
            
            //check server name of the annotation, it can't be null
            String serverName = monitor.server();
            if (serverName == null)
            {
                logger.fatal("serverName can't be null");
                continue;
            }
            
            //check the server name of the load configuration method exist
            if (checkServerNameExist(monitor.server()))
            {
                logger.fatal("Server["+serverName+"] duplicate. Please check "+getClass());
                continue;
            }
            
            ServiceUrlParser serviceUrlParser = ServiceUrlParserFactory.getParser(monitor.parserClass());
            if( serviceUrlParser == null )
            {
                logger.error("can't find service url parser["+monitor.parserClass()+"] for "+this.getClass());
                continue;
            }
            
            //create a BackendServerConfiguration, and add it into backendServers list
            BackendServerConfiguration tmp = new BackendServerConfiguration();
            tmp.setName(serverName);
            tmp.setMonitorMethod(monitorMethod.getName());
            try
            {
                serviceUrlParser.parse(tmp, monitor.filePath(), monitor.serviceUrlKeys());
            }
            catch(Exception ignored){
                logger.error("#init",ignored);
            }
            backendServers.add(tmp);
        }

        //sort the backend server according to server name
        Collections.sort(backendServers, new Comparator<BackendServerConfiguration>()
        {
            public int compare(BackendServerConfiguration first, BackendServerConfiguration second)
            {
                return first.getName().compareTo(second.getName());
            }
        });
    }

    public List<BackendServerConfiguration> fetchBackendServers()
    {
        return backendServers;
    }

    public DetectResult monitor(BackendServerConfiguration conf)
    {
        if (logger.isDebugEnabled())
            logger.debug("monitor request ["+conf.getName()+"] is coming...");
        
        DetectResult result = new DetectResult(conf.getName());
        boolean find = false;
        
        Iterator<BackendServerConfiguration> iterator = backendServers.iterator();
        while (iterator.hasNext())
        {
            BackendServerConfiguration tmp = (BackendServerConfiguration) iterator.next();
            if (!tmp.getName().equals(conf.getName()))
                continue;
            try
            {
                if( logger.isDebugEnabled() )
                    logger.debug("monitor method is "+tmp.getMonitorMethod());
                
                long startTime = System.currentTimeMillis();
                result = (DetectResult) invokeMethod(this, tmp.getMonitorMethod(), new Class[0], new Object[0]);
                result.testTime = System.currentTimeMillis() - startTime;
            }
            catch (Exception ex)
            {
                logger.fatal("#monitor", ex);
                result.isSuccess = false;
                result.msg = "c-server has exception when mointer: "+ex.getMessage();
            }
            find = true;
            break;
        }
        if (!find)
        {
            result.isSuccess = false;
            result.msg = "backendServer "+conf.getName()+" doesn't exist";
        }
        result.setName(conf.getName());
        return result;
    }

    public List<DetectResult> monitorAll()
    {
        List<DetectResult> results = new ArrayList<DetectResult>();
        Iterator<BackendServerConfiguration> iterator = backendServers.iterator();
        while (iterator.hasNext())
        {
            results.add(monitor(iterator.next()));
        }
        return results;
    }
    
    /**
     * you can overridden this method to provide file path for version.txt
     * @return
     */
    protected String getBuildVersionFilepath()
    {
        return null;
    }

    public String fetchCServerBuildVersion()
    {
        String filePath = getBuildVersionFilepath();
        String version = "";
        if (filePath == null)
        {
            String defaultPath[] = { "version.txt", "../version.txt" };
            for (int i = 0; i < defaultPath.length; i++)
            {
                version = getCServerBuildVersion(defaultPath[i]);
                if (version != null && !"".equals(version))
                    break;
            }
        }
        else
        {
            version = getCServerBuildVersion(filePath);
        }
        return version;
    }

    /**
     * get build version from file path
     * @param filePath. the filepath is according to WEB-INF/classes/
     * e.g. filePath is version.txt, it is in WEB-INF/classes/version.txt
     *      filePath is ../version.txt, it is in WEB-INF/version.txt
     * @return
     */
    private String getCServerBuildVersion(String filePath)
    {
        StringBuilder version = new StringBuilder();
        
        InputStream inputStream = null;
        if (filePath.startsWith(".."))
        {
            String realPath = null;
            URL rootPath = Thread.currentThread().getContextClassLoader().getResource("");
            File file = new File(rootPath.getPath());
            File parentOfRoot = file.getParentFile();
            if (parentOfRoot == null || !parentOfRoot.isDirectory())
                return "";
            String suffix = filePath.replace("../", "");
            suffix = suffix.replace("..\\", "");
            realPath = (new StringBuilder()).append(parentOfRoot.getAbsolutePath()).append(File.separator).append(suffix).toString();
            try
            {
                inputStream = new FileInputStream(new File(realPath));
            }
            catch(FileNotFoundException ex)
            {
                return "";
            }
        }
        else
        {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        }
      
        if (inputStream == null)
            return "";
        
        try
        {
            byte buffer[] = new byte[1024];
            int length = 0;
            while( (length = inputStream.read(buffer)) != -1 )
                version.append(new String(buffer, 0, length));
        }
        catch (IOException ignored)
        {
            logger.fatal("#getCServerBuildVersion", ignored);
            version = new StringBuilder();
        }
        finally
        {
            try
            {
                if (inputStream != null)
                    inputStream.close();
            }
            catch (Exception ignored)
            {
                logger.fatal("#getCServerBuildVersion", ignored);
                version = new StringBuilder();
            }
        }

        return version.toString();
    }

    /*
    protected String[] getValuesFromPropertiesFile(String filePath, String keys[])
    {
        String values[] = new String[keys.length];
        InputStream inputStream = AbstractBackendServerMonitor.class.getClassLoader().getResourceAsStream(filePath);
        if(inputStream == null)
            return null;
        Properties properties = new Properties();
        try
        {
            properties.load(inputStream);
            inputStream.close();
        }
        catch(IOException ignore)
        {
            logger.fatal("#getServiceUrlFromPropertiesFile", ignore);
            return null;
        }
        for(int i = 0; i < keys.length; i++)
            values[i] = properties.getProperty(keys[i]);
        return values;
    }

    protected String getValueFromPropertiesFile(String filePath, String key)
    {
        String value = "";
        String keys[] = new String[1];
        keys[0] = key;
        String values[] = getValuesFromPropertiesFile(filePath, keys);
        if (values == null || values.length == 0)
            value = (new StringBuilder()).append("can't find configuration file [").append(filePath).append("]").toString();
        else
            value = values[0];
        return value;
    } */
    
    private Object invokeMethod(Object instance, String methodName, Class parameterTypes[], Object parameters[]) throws Exception
    {
        Class clazz = instance.getClass();
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(instance, parameters);
    }

    private boolean checkParameterOfLoadConfigurationMethod(Method loadConfMethod)
    {
        Class parameterTypes[] = loadConfMethod.getParameterTypes();
        if (parameterTypes.length == 1)
        {
            Class firstParameter = parameterTypes[0];
            if (firstParameter == BackendServerConfiguration.class)
                return true;
        }
        return false;
    }

    private boolean checkSignatureOfMonitorMethod(Method monitor)
    {
        Class parameterTypes[] = monitor.getParameterTypes();
        return parameterTypes.length == 0 && monitor.getReturnType() == DetectResult.class;
    }

    private boolean checkServerNameExist(String serverName)
    {
        Iterator<BackendServerConfiguration> iterator = backendServers.iterator();
        while (iterator.hasNext())
        {
            BackendServerConfiguration backendServer = (BackendServerConfiguration) iterator.next();
            if (serverName.equals(backendServer.getName()))
                return true;
        }

        return false;
    }
}
