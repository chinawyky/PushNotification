/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliTransaction;



/**
 * this class is for sending deplement info to cli log
 * @author jzhu
 * @version 2007-06-11
 */
public class LogDeploymentInfo
{
	private static Logger logger = Logger.getLogger(LogDeploymentInfo.class);
	
	//the information will send to cli only one time when c-server start up.
	private static boolean isLogged = false;
	
	
	public synchronized static void logInfo()
	{
		if (!isLogged)
		{
			isLogged = true;
			
			String version = getVersion();
		    String libNames = getLibNames();
			logger.info("sending deployment info, version="+version+", jarfiles="+libNames);

			CliTransaction cli = CliTransactionFactory.getInstance(CliTransaction.TYPE_SQL);
		    //cli.setApplicationId(Constants.CLI_APP_ID);
		    cli.setFunctionName("startService");
		    cli.addData("DEPLOYINFO", "version="+version+"&jars="+libNames);  //Please use "DEPLOYINFO" as label here.
		    cli.complete();
		}
	}
	
	//read version info from version.txt
	public static String getVersion()
	{
		String version = "";
		InputStream is = null;
		BufferedReader br = null;
		try
		{
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("version.txt");
			br = new BufferedReader(new InputStreamReader(is));
			version = br.readLine();
		} catch (Exception e)
		{
			logger.error("LogDeplementInfo getVersion error:", e);
		} finally
		{
			try
			{
				if (br != null)
				{
					br.close();
				}
				if (is != null)
				{
					is.close();
				}
			} catch (Exception e)
			{
				logger.error("LogDeplementInfo getVersion error:", e);
			}
		}
		return version;
	}
	
	//get all jar files in lib folder 
	private static String getLibNames()
	{
		String libNames = "";
		try
		{
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			
			//in application environment.
			String appPath = path + "/lib/";
			
			//in web environment
			String webPath = path + "/../lib/";
			
			libNames += getJarFileNamesInDir(appPath);
			libNames += getJarFileNamesInDir(webPath);
		}
		catch (Exception e)
		{
			logger.error("LogDeplementInfo getLibNames error:", e);
		}
		
		return libNames;
	}
	
	private static String getJarFileNamesInDir(String dirName)
	{
		String names = "";
		File dir = new File(dirName);
		if (!dir.isDirectory())
			return names;
		File[] files = dir.listFiles();
		int len = (files==null)?0:files.length;
		for (int i = 0; i < len; i++)
		{
			String name = files[i].getName();
			if (!name.endsWith(".jar"))
				continue;
			names += name + ",";
		}

		return names;
	}

}
