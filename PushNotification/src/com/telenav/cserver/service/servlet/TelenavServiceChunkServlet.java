package com.telenav.cserver.service.servlet;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.framework.data.DataProcessor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolHandler;
import com.telenav.cserver.framework.executor.protocol.protobuf.ProtocolBufferHandler;
import com.telenav.cserver.framework.transportation.Transportor;
import com.telenav.cserver.framework.transportation.http.ServletTransportor;
import com.telenav.cserver.framework.trump.ExecutorServiceSupport;
import com.telenav.cserver.framework.trump.TrumpRunnable;
import com.telenav.cserver.service.chunkhandler.ChunkCallbackImpl;
import com.telenav.cserver.service.chunkhandler.ChunkProcessor;
import com.telenav.cserver.service.chunkhandler.ChunkProcessorFactory;
import com.telenav.cserver.service.chunkhandler.ChunkReadImpl;

public class TelenavServiceChunkServlet extends HttpServlet 
{
    private static Logger logger = Logger.getLogger(TelenavServiceChunkServlet.class);
    
    private boolean needLogReqRespBinData = false;   
    private String reqRespBinDataLogFolder = "";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HHmmssSSS");
    private static final String BIN_TYPE_REQUEST = ".request";
    private static final String BIN_TYPE_RESPONSE = ".response";
    private static final Object folderMutex = new Object();
    
	public TelenavServiceChunkServlet()
	{
		super();
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		doPost(req, res);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		ExecutorContext context = new ExecutorContext();
		context.setTimestamp(System.currentTimeMillis());
		Transportor transportor = new ServletTransportor(req, res);
		DataProcessor dataProcessor = null;
		context.setTransportor(transportor);
		logger.info("New request coming from " + transportor.getRemoteAddress());
		ProtocolHandler handler = new ProtocolBufferHandler();
		context.setProtocolHandler(handler);
		context.setServerUrl(req.getHeader("host"));
		context.setAttribute(ExecutorContext.REQUEST_IP, req.getRemoteAddr());
		
		CliTransaction cli = null;//new CliTransaction(CliConstants.TYPE_URL);
	    
		ChunkReadImpl chunkRead = null;
		ExecutorRequest request = null;
		ExecutorResponse response = null;
		try
		{
			//ChunkProcessor processor = ChunkProcessorFactory.getInstance().createProcessor("DsrProcessor");
			chunkRead = new ChunkReadImpl(req, res, request, response, dataProcessor, context, cli); 
			//processor.getCallbackCalss().setListener(chunkRead);
			//ChunkCallbackImpl callback = new ChunkCallbackImpl(chunkRead);
			//chunkRead.setCallback(callback);
			transportor.read(chunkRead);
			/*Disable log bin
			if(needLogReqRespBinData)
			{
				logger.info("start loggin req & resp");
				String id = getTimeString() + "_" + Thread.currentThread().getId();
				String requestFileName = getFullFileName(chunkRead.getSbExecutorType().toString() + "_" + id + BIN_TYPE_REQUEST);
				String responseFileName = getFullFileName(chunkRead.getSbExecutorType().toString() + "_" + id + BIN_TYPE_RESPONSE);
				//logBin(chunkRead.g)
			}
			*/
			if(logger.isDebugEnabled())
			{
				logger.debug("Finish Chunk Session ---");
			}
		}
		catch(Exception e)
		{
			logger.error("Chunk Session Error : " + e.getMessage());
			ExecutorRequest reqForCli = null;
            if(request != null)
            {
            	reqForCli = request;
            }
            chunkRead.cliLoggingException(reqForCli, chunkRead.getSbExecutorType().toString(), e);
            logger.error(e, e);
            if(response == null)
            {
            	response.setExecutorType(chunkRead.getFirstExecutorType());
            	response.setStatus(ExecutorResponse.STATUS_EXCEPTION);
            }
		}
	}
	
	public void init() throws ServletException
	{
		super.init();
		needLogReqRespBinData = isNeedLogReqRespBinData();
		reqRespBinDataLogFolder = getReqRespBinDataLogFolder();
		ResourceFactory.getInstance();
	}
	
	public void destroy()
	{
		super.destroy();
		ExecutorServiceSupport.stop();
		try
		{
			Thread.sleep(2000);
		}
		catch(Exception e)
		{
			logger.warn("Exception occurs when destroying TelenavServiceServlet", e);
		}
	}
	
	private void logBin(byte[] data, String fileName)
	{
		logger.debug("fileName : " + fileName);
		DataOutputStream output = null;
		try
		{
			createParentFolderIfNotExist(fileName);
			output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
			output.write(data);
			output.flush();
		}
		catch(IOException e)
		{
			logger.warn("logBin", e);
		}
		finally
		{
			if(output != null)
			{
				try
				{
					output.close();
				}
				catch(IOException e)
				{
					logger.warn("logBin", e);
				}
			}
		}
	}
	
	private void createParentFolderIfNotExist(String fileName)
	{
		File file = new File(fileName);
		if(!file.getParentFile().exists())
		{
			synchronized(folderMutex)
			{
				file = new File(fileName);
				if(!file.getParentFile().exists())
				{
					logger.debug("create Folder : " + file.getParentFile());
					file.getParentFile().mkdir();
				}
			}
		}
	}
	
	private boolean isNeedLogReqRespBinData()
	{
		String needLogBinData = getInitParameter("need_log_req_resp_data");
		logger.info("need_log_req_resp_data : " + needLogBinData);
		return Boolean.valueOf(needLogBinData);
	}
	
	private String getReqRespBinDataLogFolder()
	{
		String binDataRootFolder = getInitParameter("req_resp_data_log_folder");
		logger.info("binDataRootFolder : " + binDataRootFolder);
		if(binDataRootFolder == null)
			return "/tmp/tn6x";
		else
			return binDataRootFolder;
	}
	
	private String getFullFileName(String fileName)
	{
		return reqRespBinDataLogFolder + File.separator + getDateString() + File.separator + fileName;
	}
	
	private String getTimeString()
	{
		return TIME_FORMAT.format(new Date());
	}
	
	private String getDateString()
	{
		return DATE_FORMAT.format(new Date());
	}
}
