/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.trump;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.util.ClassUtils;
import com.telenav.cserver.framework.util.FileUtils;

/**
 * ZipHelper class for zipping/unzipping zip file.
 * @author kwwang
 * @date 2009-12-21
 */
public class ZipHelper {
	private Logger logger = Logger.getLogger("com.telenav.cserver.framework.trump.ZipHelper");

	//true by default
	private boolean overwrite=true;

	public boolean isOverrider() {
		return overwrite;
	}

	public void setOverrider(boolean overrider) {
		this.overwrite = overrider;
	}

	public boolean unZip(String fromPath, String toPath) {
		boolean isSuccess = false;
		if (!(ClassUtils.isExist(toPath) && ClassUtils.isExist(fromPath))) {
			return isSuccess;
		}
		
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipEntry zipEntry = null;
		URL toURL = ClassUtils.getUrl(toPath);
		URL fromURL = ClassUtils.getUrl(fromPath);
		String zipFilePath=fromPath.split(ZipTask.FILE_SEPARATOR)[1].replace(ZipTask.ZIP_SEPARATOR, ZipTask.FILE_SEPARATOR);
		String removeDirPath=toURL.getPath()+zipFilePath.substring(0,zipFilePath.indexOf("."));
	    //remove the device before unzip
		FileUtils.delDir(new File(removeDirPath));

		ZipFile zipFile =null;
		File entryFile = null;
		InputStream zipInputStream=null;
		try {
			zipFile= new ZipFile(URLDecoder.decode(fromURL.getPath(),"UTF-8"));
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			
			while (entries.hasMoreElements()) {
				zipEntry = entries.nextElement();
				entryFile = new File(toURL.getPath() + File.separator
						+ zipEntry.getName());

				// don't overwrite the existing file
				if (!overwrite && entryFile.exists()) {
					continue;
				}

				// make necessary directories
				if (zipEntry.isDirectory()) {
					entryFile.mkdirs();
					continue;
				}

				zipInputStream=zipFile.getInputStream(zipEntry);
				bis = new BufferedInputStream(zipInputStream);
				fos = new FileOutputStream(entryFile);
				bos = new BufferedOutputStream(fos);
				int read = -1;
				byte[] data = new byte[1024];
				while ((read = bis.read(data)) != -1) {
					bos.write(data, 0, read);
				}
				bos.flush();
				fos.close();
				bos.close();
				bis.close();
				zipInputStream.close();
			}

			isSuccess = true;
			
			if(logger.isDebugEnabled())
			{
				logger.debug("unzip file "+fromURL.getPath()+" succeed.");
			}

		} catch (Exception e) {
			logger.warn("Could not upzip file from " + fromPath + " to "
					+ toPath);
			logger.warn(e);
			isSuccess = false;
		}finally
		{
			try {
				bos.flush();
				bos.close();
				fos.close();
				bis.close();
				zipFile.close();
			} catch (Exception e) {
				logger.warn("Error happens when closing outputstream", e);
			}
		}

		return isSuccess;

	}
}
