/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.telenav.cserver.common.resource.ResourceLoader;
import com.telenav.cserver.common.resource.bar.ResourceBar;
import com.telenav.cserver.common.resource.bar.ResourceBarCollection;

/**
 * BarIterationResourceLoader.java
 * Iterating all *.bar files in given directory. Only support file system.
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-14
 *
 */
public class BarIterationResourceLoader implements ResourceLoader
{
	Category logger = Category.getInstance(getClass());
	
	BarFilter barFilter = new BarFilter();
	
	static String VERSION = ".version";
	
	/**
	 * load resource 
	 * 
	 * @param path
	 * @param objectName
	 * @return Object, Map for properties, Element for XML
	 */
	public Object loadResource(String path, String objectName)
	{	
		ResourceBarCollection resourceBarCollection = null;
		if(logger.isDebugEnabled())
		{
			logger.debug("Loading resource:" + path );
		}
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		try
		{
			
				URL url = cl.getResource(path);
				if(url == null)
				{
					logger.warn("Directory not found:" + path);
					return null;
				}
				
				if (logger.isDebugEnabled())
		        {
		            logger.debug("AbsoluteFilePath:" + path);
		        }
				
				String absoluteFilePath = url.getPath();

				File dir = new File(absoluteFilePath);
				if(dir.exists() && dir.isDirectory())
				{
//					reading meta information 
					//Map metaInformation = new HashMap();
					Map metaInformation = new LinkedHashMap();
					List<String> fileNames = new ArrayList<String>();
					//fill meta map and file list
					fillBarFileList(fileNames,metaInformation,dir);
					
			        resourceBarCollection = new ResourceBarCollection();
			        resourceBarCollection.setMetaInformation(metaInformation);
					
			        logger.debug("fileNames: " + fileNames);
			        InputStream is = null;
					for(String fileName: fileNames)
					{
						String fileFullName = absoluteFilePath + File.separator + fileName;
						is = null;
						
						try {
							FileInputStream fis = new FileInputStream(fileFullName);
							is = new BufferedInputStream(fis);
							logger.warn("Reading File:" + fileFullName);
							if(is == null)
							{
								logger.warn("File not found:" + path);
								return null;
							}
							
							if (logger.isDebugEnabled())
					        {
					            logger.debug("AbsoluteFilePath:" + path);
					        }
							
							BufferedInputStream bis = new BufferedInputStream(is);
							ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
							int ch;
							while ((ch = bis.read()) != -1)
							{
				                baos.write(ch);
							}
							
							fis.close();						
							
							ResourceBar resourceBar = new ResourceBar();
							resourceBar.setData(baos.toByteArray());
							String type = fileName;
							resourceBar.setType(type);
							resourceBar.setVersion((String)metaInformation.get(type + VERSION));
							
							resourceBarCollection.addResourceBar(resourceBar);
						} catch (FileNotFoundException e) {		
							logger.warn("File not found:" + fileFullName);
						}
						finally
						{ 
							try
							{
								if (is != null)
								{
									is.close();
								}
							} catch (Exception e)
							{
								logger.warn("file close failed.", e);
							}
						}
						
						
					}
					
					
				}
				
				if(logger.isDebugEnabled())
				{
					logger.debug(resourceBarCollection);
				}
			
			
			return resourceBarCollection;
		}
		catch(IOException e)
		{
			logger.warn("Exception when reading:" + path, e);
		}	
		
		return null;
	}
	
	/**
	 * Traversal all *.meta file under the directory and save all including bar files. 
	 * @param fileNames the bar file.
	 * @param metaInformation data and version 
	 * @param dir folder
	 */
	private void fillBarFileList(List<String> fileNames,Map metaInformation,File dir) {
		BufferedReader br = null;
		// Use filter to collect all files that name end with ".meta"
		File[] fileArray = dir.listFiles(new MetaFilter());
        fileArray = this.resortFileName(fileArray, dir.getAbsolutePath());
		for (File metaFile : fileArray) {
			if (logger.isDebugEnabled()) {
				logger.debug("meta file name:======== " + metaFile.getName());
			}
			try {			
				br = new BufferedReader(new FileReader(metaFile));
				String str = null;
				while ((str = br.readLine()) != null) {
					int k = str.indexOf("=");
					if (k != -1) {
						String s1 = str.substring(0, k).trim();
						String s2 = str.substring(k + 1).trim();
						metaInformation.put(s1, s2);

						if (s1.endsWith(VERSION)) {
							String fileName = s1.substring(0, s1.length()
									- VERSION.length());
							fileNames.add(fileName);
						}
					}
				}
			} catch (Exception e) {
				logger.fatal("BarIterationResourceLoader::fillBarFileList()", e);
			} finally {
				try {
					//close the buffer reader
					if (br != null) {
						br.close();
					}
				} catch (Exception e) {
					logger.warn("file close failed.", e);
				}
			}
		}
	}
	
	private File[] resortFileName(File[] fileArray, String absoluteFilePath)
	{
		String metaFilePath = absoluteFilePath + File.separator + "meta.order";
		BufferedReader br = null;
		MetaString[] metaString = new MetaString[fileArray.length];
		for(int i = 0; i < metaString.length; i++)
		{
			metaString[i] = new MetaString();
			metaString[i].setMetaFile(fileArray[i]);
		}
		try
		{
			File metaFile = new File(metaFilePath);
			if(!metaFile.exists())
			{
				return fileArray;
			}
			FileReader reader = new FileReader(metaFile);
			if(reader == null)
			{
				return fileArray;
			}
			br = new BufferedReader(reader);
			String str = null;
			while((str = br.readLine()) != null)
			{
				int k = str.indexOf('=');
				if(k != -1)
				{
					String s1 = str.substring(0, k).trim();
					String s2 = str.substring(k + 1).trim();
					for(int n = 0; n < metaString.length; n++)
					{
						if((absoluteFilePath + File.separator + s1).equals(metaString[n].getMetaFile().getPath()))
						{
							metaString[n].setPriority(Integer.parseInt(s2));
							break;
						}
					}
				}
			}
			Arrays.sort(metaString);
		}
		catch (Exception e) 
		{
			logger.fatal("BarIterationResourceLoader::fillBarFileList()", e);
		} 
		finally 
		{
			try 
			{
				//close the buffer reader
				if (br != null) 
				{
					br.close();
				}
			}
			catch (Exception e) 
			{
				logger.warn("file close failed.", e);
			}
		}
		File[] files = new File[metaString.length];
		for(int i = 0 ; i < metaString.length; i++)
		{
			files[i] = metaString[i].getMetaFile();
		}
		return files;
	}
	
	/**
	 * MetaFilter to check the file type and file name end with '.meta'
	 * @author zhjdou
	 *
	 */
	static class MetaFilter implements FileFilter
	{
		@Override
		public boolean accept(File pathname) {
			if (pathname.isFile() && pathname.getName().endsWith(".meta")) 
			{
				return true;
			}
			return false;		
		}		
	}
	
	static class BarFilter implements FilenameFilter
	{

		public boolean accept(File file, String name) 
		{
			if(name != null && name.endsWith(".node"))
			{
				return true;
			}
			return false;
		}
		
	}
	
	class MetaString implements Comparable
	{		
		private File metaFile;
		private int priority = Integer.MAX_VALUE;
		public File getMetaFile()
		{
			return metaFile;
		}
		public void setMetaFile(File metaFile) 
		{
			this.metaFile = metaFile;
		}
		public int getPriority() 
		{
			return priority;
		}
		public void setPriority(int priority)
		{
			this.priority = priority;
		}
		public int compareTo(Object anotherMeta) throws ClassCastException
		{
			if(!(anotherMeta instanceof MetaString))
			{
	            throw new ClassCastException("illeagal meta object");
			}
			int anotherPriority = ((MetaString)anotherMeta).getPriority();
			return this.priority - anotherPriority; 
		}
	}
	
}