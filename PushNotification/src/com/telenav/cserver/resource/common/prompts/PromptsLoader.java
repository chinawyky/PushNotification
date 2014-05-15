/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.resource.common.prompts;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * PromptsLoader, is used to load car icon and other prompt resource like voice. It provides the following functions. 1.
 * cache, will cache the binary of all prompt resource 2. read prompt conf 3. retrieve prompt file
 * 
 * @author kwwang
 * 
 */
public class PromptsLoader
{

    private static final PromptsLoader instance = new PromptsLoader();

    public static final String PROMPTS_CONF_PATH = "config/prompts.properties";

    private Properties pros = new Properties();

    private Logger logger = Logger.getLogger(PromptsLoader.class);

    private static HttpClient client = null;

    private static final Map<String, BinaryHolder> promptsCache = new HashMap<String, BinaryHolder>();

    private static final Object lock = new Object();

    private PromptsLoader()
    {
        try
        {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource(PROMPTS_CONF_PATH);
            pros.load(url.openStream());
        }
        catch (Exception e)
        {
            logger.fatal("PromptsConfLoader read file failed," + e);
        }
    }

    public static PromptsLoader getInstance()
    {
        return instance;
    }

    public String getValue(PromptConfKeys key)
    {
        return pros.getProperty(key.toString());
    }

    public int getIntValue(PromptConfKeys key)
    {
        int value = 0;
        try
        {
            value = Integer.valueOf(getValue(key));
        }
        catch (Exception e)
        {
            logger.fatal("getIntValue failed,", e);
        }
        return value;
    }

    private synchronized HttpClient getHttpClient()
    {
        if (client != null)
            return client;
        MultiThreadedHttpConnectionManager mgr = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
        connectionManagerParams.setDefaultMaxConnectionsPerHost(getIntValue(PromptConfKeys.MAX_CONNECTION));
        connectionManagerParams.setTcpNoDelay(true);
        connectionManagerParams.setStaleCheckingEnabled(true);
        connectionManagerParams.setLinger(0);

        mgr.setParams(connectionManagerParams);
        try
        {
            client = new HttpClient(mgr);
        }
        catch (Exception e)
        {
            client = new HttpClient();
        }

        client.getHttpConnectionManager().getParams().setConnectionTimeout(getIntValue(PromptConfKeys.CONNECTION_TIMEOUT));
        client.getHttpConnectionManager().getParams().setSoTimeout(getIntValue(PromptConfKeys.SO_TIMEOUT));
        return client;
    }

    public BinaryHolder retrieveBinaryDataVia(String fileName, PromptConfKeys urlType)
    {
        BinaryHolder bh = null;
        synchronized (lock)
        {
            if (promptsCache.containsKey(fileName))
            {
                bh = promptsCache.get(fileName);
            }
            else
            {
                bh = getPromptResourceFromRemote(fileName, urlType);
            }

            if (logger.isDebugEnabled())
            {
                logger.debug("binaryHolder" + bh);
            }
        }
        return bh;
    }

    protected BinaryHolder getPromptResourceFromRemote(String fileName, PromptConfKeys urlType)
    {
        byte[] bytes = new byte[0];
        String baseUrl = getValue(urlType);
        String url = null;
        BinaryHolder bh = null;

        if (StringUtils.isEmpty(baseUrl))
        {
            logger.fatal("baseUrl for " + urlType + " is empty");
            bh = new BinaryHolder(fileName, bytes);
            return bh;
        }

        try
        {
            url = baseUrl + "/" + fileName;
            HttpMethod method = new GetMethod(url);
            getHttpClient().executeMethod(method);
            if(200==method.getStatusCode())
                bytes = method.getResponseBody();
            bh = new BinaryHolder(fileName, bytes);
            promptsCache.put(fileName, bh);
        }
        catch (Exception e)
        {
            logger.fatal("retrieveBinaryDataVia failed,", e);
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("url = " + url);
        }

        return bh;
    }
    
    public void refresh()
    {
        synchronized (lock)
        {
            promptsCache.clear();
        }
    }

}
