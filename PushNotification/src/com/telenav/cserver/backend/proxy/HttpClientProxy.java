/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.log4j.Logger;

import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.config.WebServiceItem;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
/**
 * ImageProxy.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Apr 12, 2011
 *
 */
public abstract class HttpClientProxy extends AbstractProxy
{
    private static Logger logger = Logger.getLogger(HttpClientProxy.class);
    private HttpClient httpClient = null;
    
    /**
     * when you need to add extend http client parameters, please override this method.
     * @return
     */
    protected Map<String,Object> getExtendHttpClientParams()
    {
        return new HashMap<String,Object>();
    }
    
    
    private HttpClient createHttpClient()
    {
        HttpClient httpClient = null;
        WebServiceConfigInterface config =  WebServiceConfiguration.getInstance().getServiceConfig(getProxyConfType());
        WebServiceItem item = config.getWebServiceItem();
        if(logger.isDebugEnabled())
        {
        	StringBuffer buffer = new StringBuffer();
        	buffer.append("MaxConnectionPerHost : " + item.getWebServiceMaxConnectionPerHost());
        	buffer.append(" MaxTotalConnection : " + item.getWebServiceMaxTotalConnection());
        	buffer.append(" ConnectionTimeout : " + item.getWebServiceConnectionTimeout());
        	buffer.append(" Timeout : " + item.getWebServiceTimeout());
        	buffer.append(" MinimumPoolSize : " + item.getWebServiceMinimumPoolSize());
        	buffer.append(" MaximumPoolSize: " + item.getWebServiceMaximumPoolSize());
        	logger.debug("httpclient Configurated : " + buffer.toString());
        }
        MultiThreadedHttpConnectionManager mgr = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
        connectionManagerParams.setDefaultMaxConnectionsPerHost(item.getWebServiceMaxConnectionPerHost());
        connectionManagerParams.setMaxTotalConnections(item.getWebServiceMaxTotalConnection());
        connectionManagerParams.setTcpNoDelay(true);
        connectionManagerParams.setStaleCheckingEnabled(true);
        connectionManagerParams.setLinger(0);

        mgr.setParams(connectionManagerParams);
        try
        {
            httpClient = new HttpClient(mgr);
        }
        catch (Exception e)
        {
            httpClient = new HttpClient();
        }

        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(item.getWebServiceConnectionTimeout());
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(item.getWebServiceTimeout());
        
        Iterator<Entry<String,Object>> extendHttpClientParams = getExtendHttpClientParams().entrySet().iterator();
        logger.debug("ExtendHttpClientParams size : " + getExtendHttpClientParams().entrySet().size());
        while(extendHttpClientParams.hasNext())
        {
            Entry<String,Object> entry = extendHttpClientParams.next();
            httpClient.getParams().setParameter(entry.getKey(), entry.getValue());
        }
        if(logger.isDebugEnabled())
        {
        	StringBuffer buffer = new StringBuffer();
        	buffer.append("DefaultMaxConnectionsPerHost : " + httpClient.getHttpConnectionManager().getParams().getDefaultMaxConnectionsPerHost());
        	buffer.append(" ConnectionTimeout : " + httpClient.getHttpConnectionManager().getParams().getConnectionTimeout());
        	buffer.append(" Timeout : " + httpClient.getHttpConnectionManager().getParams().getSoTimeout());
        	buffer.append(" MaxTotalConnections : " + httpClient.getHttpConnectionManager().getParams().getMaxTotalConnections());
        	logger.debug("httpclient acctural : " + buffer.toString());
        }
        return httpClient;
    }
    
    protected HttpClient getHttpClient()
    {
        if ( httpClient == null )
        {
            httpClient = createHttpClient();
        }
        if( logger.isDebugEnabled() ){
            int maxConnectionPerHost = httpClient.getHttpConnectionManager().getParams().getDefaultMaxConnectionsPerHost();
            int maxConnections       = httpClient.getHttpConnectionManager().getParams().getMaxTotalConnections();
            logger.debug(this.getProxyConfType()+": maxConnectionPerHost = "+maxConnectionPerHost+", maxConnections = "+maxConnections);
        }
        return httpClient;
    }
    
    protected String getServiceUrl()
    {
        return WebServiceConfiguration.getInstance().getServiceConfig(getProxyConfType()).getServiceUrl();
    }
    
    protected Map<String,String> getServiceUrlMapping()
    {
        return WebServiceConfiguration.getInstance().getServiceConfig(getProxyConfType()).getServiceUrlMapping();
    }
    
    protected HttpClientResponse send(String url, ProcessResult processMethod, byte[] binData)
    {
    	PostMethod method = null;
        HttpClientResponse response = null;
        try
        {
            method = new PostMethod(url);
            RequestEntity entity = new ByteArrayRequestEntity(binData);  
            method.setRequestEntity(entity);
            getHttpClient().executeMethod(method);
            response = processMethod.process(method);
        }
        catch(Exception ex)
        {
            logger.fatal(this.getProxyConfType() + " : " + ex.toString());
        }
        finally
        {
            if (method != null)
            {
                try
                {
                    method.releaseConnection();
                }
                catch (Exception ex)
                {
                }
            }
        }
        return response;     
    }
    
    protected HttpClientResponse send(String url, ProcessResult processMethod)
    {
        HttpMethod method = null;
        HttpClientResponse response = null;
        try
        {
            method = new GetMethod(url);
            getHttpClient().executeMethod(method);
            response = processMethod.process(method);
        }
        catch(Exception ex)
        {
        	logger.fatal(this.getProxyConfType() + " : " + ex.toString());
        }
        finally
        {
            if (method != null)
            {
                try
                {
                    method.releaseConnection();
                }
                catch (Exception ex)
                {
                }
            }
        }
        return response;
    }
    
    protected HttpClientResponse send(String url, String request, String contentType, ProcessResult processMethod)
    {
        PostMethod method = null;
        HttpClientResponse response = null;
        try
        {
            method = new PostMethod(url);
            RequestEntity entity = new StringRequestEntity(request, contentType, null);
            method.setRequestEntity(entity);
            getHttpClient().executeMethod(method);
            response = processMethod.process(method);
        }
        catch(Exception ex)
        {
            logger.fatal(this.getProxyConfType() + " : " + ex.toString());
        }
        finally
        {
            if (method != null)
            {
                try
                {
                    method.releaseConnection();
                }
                catch (Exception ex)
                {
                }
            }
        }
        return response;
    }
    
    protected interface ProcessResult
    {
        HttpClientResponse process(HttpMethod method) throws Exception;
    }
    
    
   
}
