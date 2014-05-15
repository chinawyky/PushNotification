/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.trump;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.configuration.Configurator;
import com.telenav.cserver.framework.util.CSStringUtil;
import com.telenav.cserver.framework.util.ClassUtils;

/**
 * TrumpRunnable object, for detecting modified zip files under folder
 * 'zipFolder' in classpath, and for unzipping the modified zip file to the
 * 'device' folder.
 * 
 * @author kwwang
 * 
 */
public class TrumpRunnable implements Runnable {
	private static Logger logger = Logger
			.getLogger("com.telenav.cserver.framework.trump.TrumpRunnable");

	private static String ZIP_FOLDER = "zipFolder";

	private static final String RESOURCE_FOLDER = "device";

	private static final String EXECUTOR_FOLDER = "executor";
	
	private static final String DEVICE_CON_PATH="management/device_control.xml";

	private static final FileFilter zipFileFilter = new ZipFileFilter();

	private static final Map<String, Long> zipFileCache = new HashMap<String, Long>();

	private static final ZipHelper zipHelper = new ZipHelper();

	private static final TrumpRunnable trumpRunnable = new TrumpRunnable();
	
	static
	{
		try
		{
			DeviceConItem deviceConItem=(DeviceConItem)Configurator.getObject(DEVICE_CON_PATH,"DeviceControlItem");
			if(deviceConItem!=null&&CSStringUtil.isNotNil(deviceConItem.getZipFolderPath()))
			{
				ZIP_FOLDER=deviceConItem.getZipFolderPath();
			}
		}catch(Exception e)
		{
			logger.warn("Can not load Device control file ->"+DEVICE_CON_PATH);
		}
	}

	private TrumpRunnable() {
		makeDeviceFolder();
	}

	public static String getZipFolderPath()
	{
	    return ZIP_FOLDER;
	}
	
	public static TrumpRunnable getTrumpRunnable() {
		return trumpRunnable;
	}

	@Override
	public void run() {
		unzipAll();
	}

	/**
	 * If the device folder does not exist, find the url of the 'executor'
	 * folder,and create a new device folder
	 * 
	 * @author kwwang
	 * @date 2009-12-24
	 */
	private void makeDeviceFolder() {
		try {
			URL executorUrl = ClassUtils.getUrl(EXECUTOR_FOLDER);
			if (executorUrl != null) {
				String parentPath = new File(executorUrl.getPath())
						.getCanonicalPath();

				parentPath = parentPath.substring(0, parentPath
						.lastIndexOf(File.separator));
				
				File deviceFolder = new File(parentPath + File.separator
						+ RESOURCE_FOLDER);
				
				if(!deviceFolder.exists())
				{
					deviceFolder.mkdir();
				}
			}

		} catch (Exception e) {
			logger.warn("Exception happens when making device folder", e);
		}
	}

	public void unzipAll() {
		try {
			List<File> files = getAllZipFiles();
			String fileName = null;
			long lastModified = -1;
			for (File f : files) {
				fileName = f.getCanonicalPath();
				lastModified = f.lastModified();
				// for added zip file and updated zip file
				if (zipFileCache.get(fileName) == null
						|| lastModified > zipFileCache.get(fileName)) {
					if (zipHelper.unZip(ZIP_FOLDER + ZipTask.FILE_SEPARATOR + f.getName(),
							RESOURCE_FOLDER)) {
						// update zipFileCache
						zipFileCache.put(fileName, lastModified);
					}
				}
				// for deleted zip file
				else if (zipFileCache.containsKey(fileName)
						&& !files.contains(f)) {
					zipFileCache.remove(fileName);
					if (logger.isDebugEnabled()) {
						logger.debug("Remove the correponding zip File from cache "
										+ fileName);
					}
				}
			}
			
			//del the zip currently
			//FileUtils.delFiles(files);
			
		} catch (Exception e) {
			logger.warn("Exception happens when unzipping file", e);
		}
	}

	public void unzipByName(String... zipFileNames) {
		URL url = ClassUtils.getUrl(ZIP_FOLDER);
		if (url == null)
			return;

		for (String zipFileName : zipFileNames) {
			File specificFile = new File(url.getPath() + File.separator
					+ zipFileName);
			if (specificFile.exists()) {
				try {
					String filePath = specificFile.getCanonicalPath();
					boolean isSuccess = zipHelper.unZip(ZIP_FOLDER + "/"
							+ specificFile.getName(), RESOURCE_FOLDER);
					printLog(isSuccess,filePath);
				} catch (IOException e) {
					logger.warn("Exception happens when unzipping speicfic file",e);
				}

			}
		}
	}
	
	private void printLog(boolean isSuccess,String filePath)
	{
		if (isSuccess) {
			if (logger.isDebugEnabled()) {
				logger.debug("Unzipping specific file " + filePath
						+ " succeed.");
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Unzipping specific file " + filePath
						+ " failed.");
			}
		}
	}

	private List<File> getAllZipFiles() {
		URL url = ClassUtils.getUrl(ZIP_FOLDER);
		if (url == null)
			return Arrays.asList(new File[0]);
		File zipFolder = new File(url.getPath());
		File[] files = zipFolder.listFiles(zipFileFilter);
		return Arrays.asList(files);
	}

	private static class ZipFileFilter implements FileFilter {
		@Override
		public boolean accept(File pathname) {
			if (pathname.getName().endsWith("zip"))
				return true;
			return false;
		}

	}
	
	static class DeviceConItem
	{
		private String zipFolderPath;

		public String getZipFolderPath() {
			return zipFolderPath;
		}

		public void setZipFolderPath(String zipFolderPath) {
			this.zipFolderPath = zipFolderPath;
		}
		
	}

}
