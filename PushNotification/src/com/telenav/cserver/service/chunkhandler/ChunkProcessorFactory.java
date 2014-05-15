package com.telenav.cserver.service.chunkhandler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.configuration.Configurator;

public class ChunkProcessorFactory 
{
	private static Logger logger = Logger.getLogger(ChunkProcessorFactory.class);
	
    private static ChunkProcessorFactory instance = new ChunkProcessorFactory();
	private static final String TYPE_DEFAULT = "DEFAULT";
	
	private ChunkProcessorFactory(){}
	
	static
	{
	    try
	    {
	    	instance = (ChunkProcessorFactory)Configurator.getObject("executor/processor_mapping.xml" , "processor_factory");
            logger.debug("instance : " + instance);	    	
	    }
	    catch(Exception ex)
	    {
	    	logger.fatal(ex, ex);
	    	ex.printStackTrace();
	    }
	}
	
	public static ChunkProcessorFactory getInstance()
	{
		return instance;
	}
	
	private Map<String, ChunkProcessor> classMap = new HashMap<String, ChunkProcessor>();
	
	public Map<String, ChunkProcessor> getClassMap()
	{
		return classMap;
	}
	
	public void setClassMap(Map<String, ChunkProcessor> classMap)
	{
		this.classMap = classMap;
	}
	
	public ChunkProcessor createProcessor(String jobName) throws Exception
	{
		ChunkProcessor processor = classMap.get(jobName).getClass().newInstance();
		if(processor == null)
		{
			processor = classMap.get(TYPE_DEFAULT);
		}
		if(processor == null)
		{
			throw new IllegalArgumentException("Invalid node type");
		}
		return processor;
	}
}
