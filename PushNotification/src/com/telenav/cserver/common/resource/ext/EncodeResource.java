/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.telenav.cserver.framework.util.CSStringUtil;

/**
 * EncodeResource.java
 * The class provides input stream of ClassPathResource with the function that interpret Unicode
 *  string to some coding format. For example, the content 'a\u00e9' of file will be translated 
 *  to 'a¨¦' in UTF-8 format in getInputStream().
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-6-21
 * 
 */
public class EncodeResource extends ClassPathResource
{

    private static final Logger logger = Logger.getLogger(EncodeResource.class);

    private final String path;
    //TODO get ENCODING from xml declaration, e.g. <?xml version="1.0" encoding="UTF-8"?>, ENCODING=UTF-8
    private static final String DEFAULT_ENCONDING = "UTF-8";

    public EncodeResource(String path)
    {
        super(path);
        this.path = path;
    }


    /***
     * @throws IOException when can't find resource; 
     * @throws IllegalArgumentException when find bad unicode string format, e.g. /utttt
     */
    public InputStream getInputStream() throws IOException
    {
        String encoding = DEFAULT_ENCONDING;
        //open input stream
        InputStream is = null;
        URL url = ClassUtils.getDefaultClassLoader().getResource(path);
        if( url == null ){
            throw new FileNotFoundException(
                getDescription() + " cannot be opened because it does not exist");
        }
        is = new FileInputStream(url.getPath());
        
        // read contents
        int bufSize = 1024;
        byte[] buf = new byte[bufSize];
        int len = 0;
        StringBuffer sb = new StringBuffer();
        while ((len = is.read(buf, 0, bufSize)) != -1)
        {
            sb.append(new String(buf, 0, len, encoding));
        }
        is.close();
        String str = sb.toString();
        
        //TODO get encoding from xml
        
        //interpret Unicode string, e.g. translate '\u00e9' to  [00] [e9] in memory
        String dest = null;
        try{
            int strLength = str.length();
            dest = CSStringUtil.parse(str.toCharArray(), 0, strLength, new char[strLength]);
        }catch(IllegalArgumentException ex)
        {
            logger.fatal("EncodeResource#getInputStream",ex);
            throw ex;
        }
        
        //provide ByteArrayInputStream for source
        byte[] bytes = dest.getBytes(encoding);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        if (logger.isDebugEnabled())
        {
            logger.debug("src  = " + str);
            logger.debug("dest = " + new String(bytes,encoding));
            logger.debug("dest.byteSize_"+encoding+ " = " + bytes.length);
        }
        return bis;
    }

    @Override
    public Resource createRelative(String relativePath)
    {
        String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
        return new EncodeResource(pathToUse);
    }
    
    private String getEncoding(String xmlContents)
    {
        
        return "";
    }

}
