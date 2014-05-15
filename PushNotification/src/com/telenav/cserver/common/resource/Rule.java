/*
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 * 
 * History:
 */
package com.telenav.cserver.common.resource;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

import com.telenav.cserver.common.cache.SizeOfObjectImpl;
import com.telenav.cserver.common.resource.constant.ResourceConst;
/**
 * Rule.java
 *
 * @author bhu
 * 2008-12-12 11:33:53
 */
public class Rule implements java.io.Serializable
{

    /**
	 * 
	 */
	
	private static final Logger LOGGER = Logger.getLogger(Rule.class);
	private static final long serialVersionUID = -7521018596943962889L;
	private static final int COMBINATION_AND = 0;
    private static final int COMBINATION_OR = 1;
    private static final String COMBINATION_LABEL = "combination";
    private static final String OR_LABEL = "or";
    private static final String INPUT_LABEL = "input";
    private static final String PARAM_LABEL = "param";
    private static final String OUTPUT_LABEL = "output";
    private static final String RESPONSE_LABEL = "response";
    private static final String NAME_ATTRIBUTE_LABEL = "name";
    private static final String VALUE_ATTRIBUTE_LABEL = "value";
    private static final String PATTERN_ATTRIBUTE_LABEL = "pattern";
    
    private int  inputCombination = COMBINATION_AND;
    private final Hashtable values = new Hashtable();
    private final Hashtable patterns = new Hashtable();
    private final Hashtable responses = new Hashtable();
    
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer(ResourceConst.BUFFER_SIZE);
        sb.append("{");
        sb.append("combindation: ").append(inputCombination == COMBINATION_AND ? "and" : OR_LABEL).append(ResourceConst.COMMA);
        sb.append("values: ").append(hashtable2String(values)).append(ResourceConst.COMMA);
        sb.append("patterns: ").append(hashtable2String(patterns)).append(ResourceConst.COMMA);
        sb.append("responses: ").append(hashtable2String(responses));
        sb.append(" }");
       
        return sb.toString();
    }
    
    
    private String hashtable2String(Hashtable hashtable){
        StringBuffer sb = new StringBuffer("[");
        if( hashtable != null ){
            Set keys = hashtable.keySet();
            for( Object key : keys){
                sb.append(key).append('=').append(hashtable.get(key)).append(ResourceConst.COMMA);
            }
            if( sb.charAt(sb.length()-1) != '['){
                sb.deleteCharAt(sb.length()-1);
                sb.deleteCharAt(sb.length()-1);
            }
        }
        sb.append("]");
        return sb.toString();
    }


    /**
     * create a new rule given a node specifying the rule
     * @param ruleElement the node specifying the rule
     */
    public Rule(Node ruleElement)
    {
        try
        {
            Element inputElement = (Element) ((Element) ruleElement).getElementsByTagName(INPUT_LABEL).item(0);
            if (inputElement.hasAttribute(COMBINATION_LABEL))
            {
                if (inputElement.getAttribute(COMBINATION_LABEL).equals(OR_LABEL))
                {
                    this.inputCombination = COMBINATION_OR;
                }
                else
                {
                    this.inputCombination = COMBINATION_AND;
                }
            }
            
            NodeIterator paramDefs = XPathAPI.selectNodeIterator(
                   (Element)ruleElement, 
                    INPUT_LABEL + ResourceConst.BACKSLASH + PARAM_LABEL);
            for (Element param = (Element)paramDefs.nextNode(); 
                param != null; 
                param = (Element)paramDefs.nextNode())
            { 
                if (param.hasAttribute(NAME_ATTRIBUTE_LABEL) && param.hasAttribute(VALUE_ATTRIBUTE_LABEL))
                {
                    Vector curValues = (Vector) this.values.get(param.getAttribute(NAME_ATTRIBUTE_LABEL));
                    if (curValues == null)
                    {
                        curValues = new Vector();
                        curValues.add(param.getAttribute(VALUE_ATTRIBUTE_LABEL));
                        this.values.put(param.getAttribute(NAME_ATTRIBUTE_LABEL), curValues); 
                    }
                    else
                    {
                    	curValues.add(param.getAttribute(VALUE_ATTRIBUTE_LABEL));
                    }
                }
                else if (param.hasAttribute(NAME_ATTRIBUTE_LABEL) && param.hasAttribute(PATTERN_ATTRIBUTE_LABEL))
                {
                    Pattern attrPattern = Pattern.compile(param.getAttribute(PATTERN_ATTRIBUTE_LABEL));
                    Vector curPatterns = (Vector) this.patterns.get(param.getAttribute(NAME_ATTRIBUTE_LABEL));
                    if (curPatterns == null)
                    {
                        curPatterns = new Vector();
                        curPatterns.add(attrPattern);
                        this.patterns.put(param.getAttribute(NAME_ATTRIBUTE_LABEL), curPatterns);
                    }
                    else
                    {
                    	curPatterns.add(attrPattern);

                    }
                }
            } 
            NodeIterator responseDefs = XPathAPI.selectNodeIterator(
                   (Element)ruleElement, 
                    OUTPUT_LABEL + ResourceConst.BACKSLASH + RESPONSE_LABEL);
            for (Element response = (Element)responseDefs.nextNode(); 
                response != null; 
                response = (Element)responseDefs.nextNode())
            { 
                if (response.hasAttribute(NAME_ATTRIBUTE_LABEL) && response.hasAttribute(VALUE_ATTRIBUTE_LABEL))
                {
                    this.responses.put(response.getAttribute(NAME_ATTRIBUTE_LABEL), response.getAttribute(VALUE_ATTRIBUTE_LABEL));
                }
            }
        }
        catch (TransformerException e)
        {
        	LOGGER.fatal(e);
        }   
    }   //end constructor rule
    
    
    /**
     * get the values for this rule
     * @return the values for this rule
     */
    private Hashtable getValues()
    {
        return this.values;
    }
    
    /**
     * get the patterns for this rule
     * @return the patterns for this rule
     */
    private Hashtable getPatterns()
    {
        return this.patterns;
    }
    
    /**
     * Get the responses associated with this rule
     * @return the responses for this rule
     */
    public Hashtable getResponses()
    {
        return this.responses;
    }
    
    
    /**
     * check if this rule passes for the given inputParams
     * @param inputParams   [in/out]    the input parameters
     * @return  true if the rule passes for these input parameters, false otherwise
     */
    public boolean checkRule(Map inputParams)
    {
        if(inputParams == null)
        {
            return false;
        }
        for (Enumeration valueKeys = this.getValues().keys();valueKeys.hasMoreElements();)
        {
            String valueName = (String) valueKeys.nextElement();
            String value = (String)inputParams.get(valueName);
            if (value == null)
            {
                if (this.inputCombination == COMBINATION_AND)
                {
                    return false;
                }
            }
            else
            {
                for (Iterator values = ((Vector) this.getValues().get(valueName)).iterator(); values.hasNext();)
                {
                    if (value.equals(values.next())) 
                    {
                        if (this.inputCombination == COMBINATION_OR)
                        {
                            return true;
                        }
                    }
                    else 
                    {
                        if (this.inputCombination == COMBINATION_AND)
                        {
                            return false;
                        }
                    }
                }
            }
        }
        for (Enumeration patternKeys = this.getPatterns().keys();patternKeys.hasMoreElements();)
        {
            String patternName = (String) patternKeys.nextElement();
            String patternValue = (String)inputParams.get(patternName);
            if (patternValue == null) 
            {
                if (this.inputCombination == COMBINATION_AND)
                {
                    return false;
                }

            }
            else 
            {
                for (Iterator patterns = ((Vector) this.getPatterns().get(patternName)).iterator(); patterns.hasNext();)
                {
                    Pattern pattern = (Pattern) patterns.next();
                    Matcher match = pattern.matcher(patternValue);
                    if (match.matches())
                    {
                        if (this.inputCombination == COMBINATION_OR)
                        {
                            return true;
                        }
                    }
                    else
                    {
                        if (this.inputCombination == COMBINATION_AND)
                        {
                            return false;
                        }
                    }
                }
            }
        }
        if (this.inputCombination == COMBINATION_OR)
        {
            return false;
        }
        else
        {
            return true;
        }
    } //end method checkRule

}
