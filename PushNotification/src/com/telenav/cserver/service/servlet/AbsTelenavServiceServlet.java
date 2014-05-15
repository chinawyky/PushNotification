/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.service.servlet;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.cli.CliThreadLocalUtil;
import com.telenav.cserver.framework.cli.CliTransactionFactory;
import com.telenav.cserver.framework.cli.LogDeploymentInfo;
import com.telenav.cserver.framework.data.DataProcessor;
import com.telenav.cserver.framework.data.impl.GZIPDataProcessor;
import com.telenav.cserver.framework.executor.ExcutorEncryptionException;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolHandler;
import com.telenav.cserver.framework.executor.protocol.txnode.ByteArrayWrapper;
import com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory;
import com.telenav.cserver.framework.transportation.Transportor;
import com.telenav.cserver.framework.transportation.http.ServletTransportor;
import com.telenav.cserver.framework.trump.ExecutorServiceSupport;

/**
 * AbsTelenavServiceServlet, the entry for HTTP client
 * 
 * @author jzhu@telenav.cn
 * @version 1.0 2011-6-20
 * 
 */
public abstract class AbsTelenavServiceServlet extends HttpServlet
{
    

    /**
     * 
     */
    private static final long serialVersionUID = -5126392125855904495L;

    private static Logger logger = Logger
            .getLogger(AbsTelenavServiceServlet.class);

    private boolean needLogReqRespBinData = false;;

    private String reqRespBinDataLogFolder = "";

    private static final String BIN_TYPE_REQUEST = ".request";

    private static final String BIN_TYPE_RESPONSE = ".response";

    private static final Object folderMutex = new Object();

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd");

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
            "HHmmssSSS");

    public static final String HTTP_HEAD_PARAMETER_USER_PHONE_NUMBER = "x-up-Calling-Line-ID";
    public static final String  COMMA_DIVISION = ",";

    /**
     * Constructor.
     */
    public AbsTelenavServiceServlet()
    {
        super();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        doPost(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {

        ExecutorRequest[] requests = null;
        ExecutorResponse[] responses = null;

        ExecutorContext context = new ExecutorContext();
        context.setTimestamp(System.currentTimeMillis());

        // need to consider how we can apply different transportor
        Transportor transportor = new ServletTransportor(req, res);
        DataProcessor dataProcessor = null;

        context.setTransportor(transportor);

        logger.info("New request coming from " + transportor.getRemoteAddress());

        //ProtocolHandler handler = new ProtocolBufferHandler();
        ProtocolHandler handler = getHandler();
        // allow multiple requests
        context.setProtocolHandler(handler);
        context.setServerUrl(req.getHeader("host"));
        context.setAttribute(ExecutorContext.REQUEST_IP, req.getRemoteAddr());
        String firstExecutorType = "";

        CliTransaction cli = null;// new CliTransaction(CliConstants.TYPE_URL);
        StringBuffer sbExecutorType = new StringBuffer();

        byte[] requestBuff = null;
        byte[] responseBuff = null;

        try
        {
            CliThreadLocalUtil.removeCliEntity();
            // Reader the request and parse the data into a request
            requestBuff = transportor.read();

            if (requestBuff == null)
            {

                logger.warn("There is an empty request coming in.");
                return;
            }

            requests = handler.getRequestParser().parse(requestBuff);

            if (requests == null)
            {
                // TODO: error
                logger.warn("There is an empty request coming in.");
                return;
            }
            responses = new ExecutorResponse[requests.length];

            for (int i = 0; i < requests.length; i++)
            {
                ExecutorRequest request = requests[i];
                String executorType = request.getExecutorType();
                sbExecutorType.append(executorType).append(",");
            }
            if (sbExecutorType.toString().endsWith(","))
                sbExecutorType.deleteCharAt(sbExecutorType.lastIndexOf(","));

            CliThreadLocalUtil.setCliThreadLocal(requests);

            cli = CliTransactionFactory.getInstance(CliConstants.TYPE_URL);

            cli.setFunctionName("service_" + sbExecutorType.toString());

            for (int i = 0; i < requests.length; i++)
            {
                ExecutorRequest request = requests[i];
                String executorType = request.getExecutorType();
                if (i == 0)
                {
                    firstExecutorType = executorType;

                    UserProfile userProfile = request.getUserProfile();
                    String dataProcessType = userProfile != null ? userProfile
                            .getDataProcessType() : null;
                    if (dataProcessType != null
                            && dataProcessType.equalsIgnoreCase("gzip"))
                    {
                        dataProcessor = new GZIPDataProcessor();
                    }
                }

                copyHttpHeadToExecutorRequest(req, request);
                //cmcc special process:get phonoe number from httphead
                if(request.getUserProfile() != null && "CMCCNAVPROG".equals(request.getUserProfile().getProgramCode()))
                {
                	String cmwapPhoneNumber = getMinNumberFromHttpHead(request);
                	if(cmwapPhoneNumber != null && cmwapPhoneNumber.trim().length() > 0)
                	{
                		if(cmwapPhoneNumber.startsWith("+86"))
                		{
                			cmwapPhoneNumber = cmwapPhoneNumber.substring("+86".length());
                		}else if(cmwapPhoneNumber.startsWith("86"))
                		{
                			cmwapPhoneNumber = cmwapPhoneNumber.substring("86".length());
                		}
                    	if(!cmwapPhoneNumber.equals(request.getUserProfile().getMin()))
                    	{
                    		logger.warn("user min in request not equal with http head!!!");
                    		request.getUserProfile().setMin(cmwapPhoneNumber);//use http head phonenumber	
                    	}                	                		
                	}
                }                
                ExecutorResponse response = ExecutorDataFactory.getInstance()
                        .createExecutorResponse(executorType);
                responses[i] = response;

                try
                {
                    CliThreadLocalUtil.setSingleExecutorType(request
                            .getExecutorType());
                    // Now, execute the request.
                    ExecutorDispatcher.getInstance().execute(request, response,
                            context);
                    CliThreadLocalUtil.setSingleExecutorType(null);
                }
                catch (Exception t)
                {
                    // cli log exception
                    cliLoggingException(request, executorType, t);

                    logger.error(t, t);
                    if (response.getErrorMessage() == null)
                    {
                        response.setErrorMessage(t.getMessage());
                    }
                    response.setStatus(ExecutorResponse.STATUS_EXCEPTION);
                }
            }
        }catch(ExcutorEncryptionException e){
        	
            // cli log exception
            ExecutorRequest reqForCli = null;
            cliLoggingException(reqForCli, sbExecutorType.toString(), e);
            
            /*
             *  some 7x client send unencrypted PTN with non-zero ptnSource, which causes exception. 
             *  The client is already launched. We still can't find a good way to avoid it. Even worse, 
             *  it happens on production frequently. we'd better log the key information of the exception only.
             *  The less we log, this lighter it has impact on the server.
   			 */
            logger.error(e.getMessage());
      
            if (responses == null || responses.length == 0)
            {
                // treat it as a critical error, need to define the protocol btw
                // c/s
                responses = new ExecutorResponse[1];
                ExecutorResponse response = new ExecutorResponse();
                response.setExecutorType(firstExecutorType);
                response.setStatus(ExecutorResponse.STATUS_EXCEPTION);
                // response.setErrorMessage(e.getMessage());
                responses[0] = response;
            }
            
        }catch (Exception e)
        {
            // cli log exception
            ExecutorRequest reqForCli = null;
            if (requests != null && requests.length > 0)
            {
                reqForCli = requests[0];
            }
            cliLoggingException(reqForCli, sbExecutorType.toString(), e);
            
            logger.error(e, e);
            
            if (responses == null || responses.length == 0)
            {
                // treat it as a critical error, need to define the protocol btw
                // c/s
                responses = new ExecutorResponse[1];
                ExecutorResponse response = new ExecutorResponse();
                response.setExecutorType(firstExecutorType);
                response.setStatus(ExecutorResponse.STATUS_EXCEPTION);
                // response.setErrorMessage(e.getMessage());
                responses[0] = response;
            }
        }
        finally
        {
            if (responses != null && responses.length > 0)
            {
                if (responses[0].getStatus() == ExecutorResponse.STATUS_WRITE_FINISHED)
                {
                    ExecutorRequest reqForCli = null;
                    if (requests != null && requests.length > 0)
                    {
                        reqForCli = requests[0];
                    }
                    cliLoggingClientInfo(reqForCli,
                            "service_" + sbExecutorType.toString(), cli);
                    cli.complete();
                    // it is for step write, no need to write again
                    return;
                }
            }
            try
            {

                // cli prepare
                ExecutorRequest reqForCli = null;
                if (requests != null && requests.length > 0)
                {
                    reqForCli = requests[0];
                }
                cliLoggingClientInfo(reqForCli,
                        "service_" + sbExecutorType.toString(), cli);

                // transportation
                ByteArrayWrapper wrapper = new ByteArrayWrapper();
                handler.getResponseFormatter().format(wrapper, responses);
                responseBuff = wrapper.bytes;

                if (dataProcessor != null)
                {
                    responseBuff = dataProcessor.process(responseBuff);
                }

                res.setContentLength(responseBuff.length);
                
                if (logger.isDebugEnabled())
                {
	                logger.debug("start write response");
                }
                transportor.write(responseBuff);
                if (logger.isDebugEnabled())
                {
	                logger.debug("done write response");
                }
                if (logger.isDebugEnabled())
                {
	                logger.debug("start flush response");
                }
                
                logger.info("response_length: " + responseBuff.length);
                logger.info("=============end=================");
                // record response length
                cli.addData("response_length",
                        Integer.toString(responseBuff.length));
                                transportor.flush();
                
                if (logger.isDebugEnabled())
                {
	                logger.debug("done flush response");
                }
                
                cli.complete();
                
                if (needLogReqRespBinData)
                {
                    logger.info("start logging req & resp");
                    String id = getTimeString() + "_"
                            + Thread.currentThread().getId();
                    String requestFileName = getFullFileName(sbExecutorType
                            .toString() + "_" + id + BIN_TYPE_REQUEST);
                    String responseFileName = getFullFileName(sbExecutorType
                            .toString() + "_" + id + BIN_TYPE_RESPONSE);
                    logBin(requestBuff, requestFileName);
                    logBin(responseBuff, responseFileName);
                    logger.info("end logging req & resp");
                }

            }
            catch (Exception e)
            {
            	if(cli != null){
            		cli.addData(CliTransaction.LABEL_ERROR, "Parse Response or Response timeout");
                	cli.setStatus(e);
                	cli.complete();
            	}
            	
                logger.error(e, e);
            }
        }
    }

    protected abstract ProtocolHandler getHandler();
    

    private void logBin(byte[] data, String fileName)
    {
        logger.debug("fileName:" + fileName);
        DataOutputStream output = null;
        try
        {
            createParentFolderIfNotExist(fileName);
            output = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(fileName)));
            output.write(data);
            output.flush();
        }
        catch (IOException e)
        {
            logger.warn("logBin", e);
        }
        finally
        {
            if (output != null)
            {
                try
                {
                    output.close();
                }
                catch (IOException e)
                {
                    logger.warn("logBin", e);
                }
            }
        }
    }

    private String getFullFileName(String fileName)
    {
        return reqRespBinDataLogFolder + File.separator + getDateString()
                + File.separator + fileName;
    }

    private String getDateString()
    {
        return DATE_FORMAT.format(new Date());
    }

    private String getTimeString()
    {
        return TIME_FORMAT.format(new Date());
    }

    private void createParentFolderIfNotExist(String fileName)
    {
        File file = new File(fileName);
        if (!file.getParentFile().exists())
        {
            synchronized (folderMutex)
            {
                file = new File(fileName);
                if (!file.getParentFile().exists())
                {
                    logger.debug("create Folder :" + file.getParentFile());
                    file.getParentFile().mkdirs();
                }
            }
        }

        return;
    }

    @Override
    public void destroy()
    {
        super.destroy();

        ExecutorServiceSupport.stop();

        try
        {
            // sleep for a while
            Thread.sleep(2000);
        }
        catch (Exception e)
        {
            logger.warn(
                    "Exception occurs when destroying TelenavServiceServlet", e);
        }
    }

    @Override
    public void init() throws ServletException
    {
        super.init();

        needLogReqRespBinData = isNeedLogReqRespBinData();
        reqRespBinDataLogFolder = getReqRespBinDataLogFolder();

        ResourceFactory.getInstance();
        
        LogDeploymentInfo.logInfo();
    }

    private boolean isNeedLogReqRespBinData()
    {
        String needLogBinData = getInitParameter("need_log_req_resp_data");
        logger.info("need_log_req_resp_data: " + needLogBinData);
        return Boolean.valueOf(needLogBinData);
    }

    private String getReqRespBinDataLogFolder()
    {
        String binDataRootFolder = getInitParameter("req_resp_data_log_folder");
        logger.info("binDataRootFolder: " + binDataRootFolder);
        if (binDataRootFolder == null)
            return "/tmp/tn6x";
        else
            return binDataRootFolder;
    }

    private void cliLoggingClientInfo(ExecutorRequest request,
            String executorType, CliTransaction cli)
    {
        if (cli == null)
            return;
        
        UserProfile user = null;
        if (request != null)
        {
            user = request.getUserProfile();
        }
        if (user != null)
        {
            String screenWidth = user.getScreenWidth();
            String screenHeight = user.getScreenHeight();
            String resolution = (screenWidth != null ? screenWidth : "") + "x"
                    + (screenHeight != null ? screenHeight : "");

            StringBuilder sb = new StringBuilder();
            sb.append("url=").append(executorType)
              .append("&userid=").append(user.getUserId())
              .append("&vendorId=").append(user.getVendorId())
              .append("&macAddress=").append(user.getMacID())
              .append("&openUDID=").append(user.getOpenUDID())
              .append("&min=").append(user.getMin())
              .append("&credentialType=").append(user.getCredentialType() != null ? user.getCredentialType() : "")
              .append("&credentialID=").append(user.getCredentialID() != null ? user.getCredentialID() : "")
              .append("&credentialValue=").append(user.getCredentialValue() != null ? user.getCredentialValue() : "")
              .append("&carrier=").append(user.getCarrier())
              .append("&platform=").append(user.getPlatform())
              .append("&locale=").append(user.getLocale())
              .append("&product=").append(user.getProduct())
              .append("&device=").append(user.getDevice())
              .append("&resolution=").append(resolution)
              .append("&programCode=").append(user.getProgramCode())
              .append("&mapDpi=").append(user.getMapDpi() != null ? user.getMapDpi() : "")
              .append("&OSVersion=").append(user.getOSVersion() != null ? user.getOSVersion() : "")
              .append("&version=").append(user.getOriginalVersion())
              .append("&buildNo=").append(user.getBuildNumber())
              .append("&DC=").append(user.getDeviceCarrier())
              .append("&PS=").append(user.getPtnSource());
            
            cli.addData(CliConstants.LABEL_CLIENT_INTO, sb.toString());
        }
        else
        {
            cli.addData(CliConstants.LABEL_CLIENT_INTO, "url=" + executorType);
        }

    }

    /**
     * @param t
     */
    private void cliLoggingException(ExecutorRequest request,
            String executorType, Throwable t)
    {
        CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory
                .getInstance(CliConstants.TYPE_MODULE);
        cli.setFunctionName(executorType);

        cliLoggingClientInfo(request, executorType, cli);
        cli.setStatus(t);
        cli.complete();
    }

    /**
     * copy http header into executor request
     * 
     * @param req
     * @param request
     */
    protected void copyHttpHeadToExecutorRequest(HttpServletRequest req,
            ExecutorRequest request)
    {
        Enumeration e = req.getHeaderNames();
        while (e.hasMoreElements())
        {
            String key = (String) e.nextElement();
            String value = req.getHeader(key);
            request.setAttribute(key, value);
        }
    }
    /**
     * get min number from different http head
     * 
     * X-Up-Calling-Line-ID:8613911893734
     * X-Network-info      :GPRS,8613799269557,10.90.110.46,FZGGSN23BNK,unsecured
     * @param request http request
     * @return
     */
    public static String getMinNumberFromHttpHead(ExecutorRequest request)
    {
        String headName = HTTP_HEAD_PARAMETER_USER_PHONE_NUMBER;
        //format "X-Up-Calling-Line-ID:8613911893734"
        String minnumber  = request.getAttribute(headName);
        if(minnumber == null || minnumber.trim().length() < 1)
        {
        	minnumber  = request.getAttribute(headName.toLowerCase());
        }
        if(minnumber != null &&  !minnumber.equals(""))
        {

        	if(minnumber.indexOf(COMMA_DIVISION)!=-1)        		
        	{
        		//avoid such gateway http head info:  x-up-Calling-Line-ID:13636505256,8618601124563
        		//use last phone number as the really phone number
        		return minnumber.substring(minnumber.lastIndexOf(COMMA_DIVISION)+COMMA_DIVISION.length());
        	}else
        	{
        		return minnumber;	
        	}            
        }
        logger.info("normal get min failed!");
        headName = "X-Network-info";
        //format "X-Network-info:GPRS,8613799269557,10.90.110.46,FZGGSN23BNK,unsecured"
        minnumber  = request.getAttribute(headName);
        if(minnumber != null &&  !minnumber.equals(""))
        {
            //parse out the minnumber
            String valueDelimiter = ",";
            int position = 1;            
            StringTokenizer st = new StringTokenizer(minnumber,valueDelimiter);
            int currentPosition = 0;
            while (st.hasMoreTokens())
            {
                if(currentPosition == position)
                {
                    minnumber = st.nextToken();
                    break;
                }
                st.nextToken();
                currentPosition++;
            }               
            return minnumber;
        }        
        //now search in all http head 
        Iterator<String> names = request.getAttributes().keySet().iterator();
        while(names.hasNext())
        {
            String key = (String) names.next();
            String value = request.getAttribute(key);
            int minStart = value.indexOf("861");
            if(minStart !=-1 && (minStart+13)<=value.length())
            {
                return value.substring(minStart,minStart+13);
            }
        }        
        logger.fatal("failed to get min number from http head!");
//        names = request.getHeaderNames();
//        logger.debug("------record http head begin------");
//        httpheadLogger.info("------record http head begin[NO MIN]------");
//        while(names.hasMoreElements())
//        {
//            String key = (String) names.nextElement();
//            String value = request.getHeader(key);
//            logger.debug(key +"=" +value);
//            httpheadLogger.info(key +"=" +value);
//        }                   
//        httpheadLogger.info("remote-ip="+ request.getRemoteAddr());
//        httpheadLogger.info("------record http parameters begin------");
//        Enumeration paraNames = request.getParameterNames();
//        while(paraNames.hasMoreElements())
//        {
//            String key = (String) paraNames.nextElement();
//            String value = request.getParameter(key);
//            logger.debug(key +"=" +value);
//            httpheadLogger.info(key +"=" +value);
//        }            
//        httpheadLogger.info("------record http parameters end------");        
//        httpheadLogger.info("------record http head end[NO MIN]------");
//        
//        logger.debug("------record http head end------");
        return "";
    }

}
