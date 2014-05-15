/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * RecursionMultilineToStringStyle, customerized toStringStyle, to make sure that the nested object's properties can be
 * logged out.
 * 
 * @author kwwang
 * 
 */
public class RecursionMultilineToStringStyle extends ToStringStyle
{
    private static final RecursionMultilineToStringStyle instance = new RecursionMultilineToStringStyle();

    private RecursionMultilineToStringStyle()
    {
        setContentStart("[");
        setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
        setFieldSeparatorAtStart(true);
        setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
    }

    public static RecursionMultilineToStringStyle getInstance()
    {
        return instance;
    }

    private Object readResolve()
    {
        return ToStringStyle.MULTI_LINE_STYLE;
    }

    private static final long serialVersionUID = 12L;

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Object value)
    {
        if (value != null && !value.getClass().getName().startsWith("java"))
            buffer.append(fieldName + "->" + ReflectionToStringBuilder.toString(value));
        else
            super.appendDetail(buffer, fieldName, value);
    }

    public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail)
    {
        super.append(buffer, fieldName, value, true);
    }

}
