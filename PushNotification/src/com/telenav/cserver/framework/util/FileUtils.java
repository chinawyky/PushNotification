/**
 * Class-related operations util.
 * @author kwwang
 * @date 2009-12-21
 */
package com.telenav.cserver.framework.util;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * FileUtils, provide some convenient methods to manipulate file.
 * @author kwwang
 * @date 2009-12-29
 */
public class FileUtils {

	private static final Logger logger=Logger.getLogger("com.telenav.cserver.framework.util.FileUtils");
	
	public static void delDir(File dir)
	{
		if(!dir.exists()||!dir.isDirectory()){
			logger.warn("File is not directory or doesn't exist "+dir.getAbsolutePath());
			return;
		}
		
		File[] files=dir.listFiles();
		for(File f:files)
		{
			try
			{
				delFile(f);
			}catch(Exception e)
			{
				logger.warn("Can't del file->"+f.getAbsolutePath(),e);
			}
		}
		
		//delete directory
		if(!dir.delete())
		{
			logger.warn("Can't del directory->"+dir.getAbsolutePath());
		}
	}
	
	
	public static void delFile(File file)
	{
		if(!file.exists())
		{
			logger.warn("File  doesn't exist "+file.getAbsolutePath());
			return;
		}
		if(file.isDirectory())
		{
			delDir(file);
		}
		else
		{
			if(!file.delete())
			{
				logger.warn("Can not del file "+file.getAbsolutePath());
			}
		}
	}
	
	public static void delFiles(List<File> files)
	{
		if(files==null)
		{
			return;
		}
		
		for(File f:files)
		{
			delFile(f);
		}
	}
	
//   public static void main(String[] args) throws Exception 
//   {
//	   Object objs=HandlerHelper.convertValue("[Ljava.lang.String;", "1,2");
//	   System.out.println(objs);
//   }
   
  
}
