/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.telenav.cserver.common.encryption.CipherUtil;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExcutorEncryptionException;
import com.telenav.cserver.common.encryption.CipherException;

/**
 * EncryptionUtil
 * 
 * @author kwwang
 * 
 */
public class EncryptionUtil {

	public static Logger logger = Logger.getLogger(EncryptionUtil.class);

	public static String decryptByPtnSource(String unDecryptedString,
			int ptnSource) throws ExcutorEncryptionException {
		String encryptedString = null;
		if (UserProfile.PTN_SOURCE_FROM_TELENAV_CSERVER != ptnSource)
			return unDecryptedString;
		try {
			if (StringUtils.isNotBlank(unDecryptedString))
				encryptedString = CipherUtil.decrypt(URLDecoder.decode(
						unDecryptedString, CipherUtil.STRING_ENCODING));
		} catch (UnsupportedEncodingException e) {			
			handleException(unDecryptedString, e);
		}
		catch (CipherException e)
		{
			handleException(unDecryptedString, e);			
		}

		return encryptedString;
	}
	
	private static void handleException(String unDecryptedString, Exception e)throws ExcutorEncryptionException
	{
		String errorMsg = "decryption failed, originString is " + unDecryptedString;
		logger.fatal(errorMsg);
		throw new ExcutorEncryptionException(errorMsg, e);
		
	}
}
