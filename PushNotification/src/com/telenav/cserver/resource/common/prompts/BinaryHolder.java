/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.resource.common.prompts;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * BinaryHolder
 * @author kwwang
 *
 */
public class BinaryHolder
{
    private String name;

    private String md5;

    private byte[] content=new byte[0];

    public BinaryHolder(String name, byte[] content)
    {
        this.name = name;
        this.content = content;
        this.md5 = new String(DigestUtils.md5Hex(content));
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMd5()
    {
        return md5;
    }

    public void setMd5(String md5)
    {
        this.md5 = md5;
    }

    public byte[] getContent()
    {
        return content;
    }

    public void setContent(byte[] content)
    {
        this.content = content;
    }

    public boolean isMd5Equal(String md5)
    {
        return StringUtils.isNotEmpty(this.md5) && this.md5.equals(md5);
    }

    public String toString()
    {
        StringBuilder builder=new StringBuilder();
        builder.append(" name = "+this.name);
        builder.append(" md5 = "+this.md5);
        builder.append(" binary length = "+this.content.length);
        return builder.toString();
    }

}
