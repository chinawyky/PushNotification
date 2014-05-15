/*
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 * 
 * History:
 */
package com.telenav.cserver.common.resource;


import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Iterator;
import java.util.Enumeration;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.apache.xpath.XPathAPI;

import com.telenav.cserver.common.resource.constant.ResourceConst;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * AbstractRuleMatchHolder.java
 *
 * @author bhu
 * 2008-12-12 11:29:44
 */
public abstract class AbstractRuleMatchHolder extends AbstractResourceHolder
{
    private static final String RULES_LABEL = "rules";
    private static final String RULE_LABEL = "rule";
    public final static String KEY_OUTPUT_MAP = "OUTPUT_MAP";
    public final static String KEY_RULE_VECTOR = "RULE_VEC";
    public final static String INPUT_PARAM_CARRIER = "carrier";
    public final static String INPUT_PARAM_PLATFORM = "platform";
    public final static String INPUT_PARAM_PRODUCT = "product";
    public final static String INPUT_PARAM_VERSION = "version";
    public final static String INPUT_PARAM_DEVICE = "device";
    
    private Vector defaultDsmRules = createDefaultDsmRules();
    
    public ResourceContent createObject(String key, UserProfile profile, TnContext tc)
    {
    	ResourceContent content = null;
        Element root = (Element)ResourceFactory.createResource(this,profile, tc);
        if (root != null)
        {
	        Vector ruleVec = new Vector();
	        try
	        {
	            
	            NodeIterator itRuleDefs = XPathAPI.selectNodeIterator(
	                (Element)root,
	                RULES_LABEL + ResourceConst.BACKSLASH + RULE_LABEL);
	           
	            for(Node ruleElement = (Node)itRuleDefs.nextNode();
	                    ruleElement != null;
	                    ruleElement = (Node)itRuleDefs.nextNode())
	            {
	                Rule rule = new Rule(ruleElement);
	                ruleVec.add(rule);
	            } 
	        }
	        catch (TransformerException ex)
	        {
	            logger.warn(ex);
	            return null;
	        }
	        
	        content = new ResourceContent();
	        content.setProperty(KEY_RULE_VECTOR, ruleVec);
        }
        return content;
    }
    
    
    protected HashMap getMatchedMap(UserProfile profile, TnContext tc)
    {
        if(profile == null)
        {
            return null;
        }
        
        HashMap map = getMatchedMapFromTrump(profile,tc);
        
        if( map == null || map.isEmpty() )
        {
            map = getMatchedMapFromDefaultDsm(profile, tc);
        }
        
        return map;
    }
    
    protected HashMap getMatchedMapFromTrump(UserProfile profile, TnContext tc)
    {
        
    	HashMap map = null;
        HashMap inputMap = createInputMap(profile);
        
        ResourceContent content = getResourceContent(profile, tc);
        
        if(content != null)
        {
            Vector ruleVec = (Vector)content.getProps().get(KEY_RULE_VECTOR);
            
            if(ruleVec == null)
            {
            	logger.debug("failed to get rule vector.");
            }
            else
            {
            	map = findRule(ruleVec,inputMap);
            }
        }
        
        return map;
        
    }
    
    protected HashMap getMatchedMapFromDefaultDsm(UserProfile profile, TnContext tc)
    {
        HashMap inputMap = createInputMap(profile);
        
        HashMap map = findRule(defaultDsmRules,inputMap);
        
        return map;
    }
    
    private HashMap createInputMap(UserProfile profile)
    {
        HashMap inputMap = new HashMap();
        inputMap.put(INPUT_PARAM_CARRIER, profile.getCarrier());
        inputMap.put(INPUT_PARAM_PLATFORM, profile.getPlatform());
        inputMap.put(INPUT_PARAM_PRODUCT, profile.getProduct());
        inputMap.put(INPUT_PARAM_VERSION, profile.getVersion());
        inputMap.put(INPUT_PARAM_DEVICE, profile.getDevice());
        return inputMap;
    }
    
    private HashMap findRule(Vector ruleVec, HashMap inputMap)
    {
        HashMap map =  new HashMap(); 
        for(Iterator iter = ruleVec.iterator();iter.hasNext();)
        {
            Rule rule = (Rule)iter.next();
            
            if(rule.checkRule(inputMap))
            {
                 
                for (Enumeration responseKeys = rule.getResponses().keys(); responseKeys.hasMoreElements();)
                {
                    String responseName = (String)responseKeys.nextElement();
                    String responseValue = (String)rule.getResponses().get(responseName);
                    if(responseName != null)
                    {
                        map.put(responseName, responseValue);
                    }
                }
                break;
            }
        }
        if(map.isEmpty())
        {
        	return null;
        }
        return map;
    }

    protected Object newInstance(String className)
    {       
        if(logger.isDebugEnabled())
        {
            logger.debug("newInstance:" + className);
        }
        
            Class clazz;
			try
			{
				clazz = Class.forName(className);
				return clazz.newInstance();
			} catch (ClassNotFoundException e)
			{
				logger.fatal(e.getMessage());
			} catch (InstantiationException e)
			{
				logger.fatal(e.getMessage());
			} catch (IllegalAccessException e)
			{
				logger.fatal(e.getMessage());
			}
            
        return null;
    }
    
    private Map class2Instance = new HashMap();
    
    protected Object getImplementationInstance(UserProfile profile, TnContext tc, String classNameKey)
    {
        Map outputMap = getMatchedMap(profile, tc);
        if(outputMap == null)
        {
            return null;
        }
        String className = (String)outputMap.get(classNameKey);
        if(className == null || className.length() == 0)
        {
            return null;
        }
        Object instance = class2Instance.get(className);
        if(instance == null)
        {
            synchronized (class2Instance)
            {
                instance = class2Instance.get(className);
                if(instance == null)
                {
                    instance = newInstance(className);
                    class2Instance.put(className, instance);
                }
            }
        }
        return instance;
    }
    
    
    
    private Vector createDefaultDsmRules()
    {
        
        Vector ruleVec = new Vector();
        //assign the new value to default dsm rules
        defaultDsmRules = ruleVec;
            
        String fullPath = "dsmRules.xml";
        
        //check the file whether exists
        if(Thread.currentThread().getContextClassLoader().getResource(fullPath) == null)
        {
            return ruleVec;
        }
        
        Element root = (Element)ResourceFactory.getResourceLoader(ResourceFactory.TYPE_XML).loadResource(fullPath, null);
      
        try
        {
            NodeIterator itRuleDefs = XPathAPI.selectNodeIterator((Element) root, RULES_LABEL + ResourceConst.BACKSLASH + RULE_LABEL);
            for (Node ruleElement = (Node) itRuleDefs.nextNode(); ruleElement != null; ruleElement = (Node) itRuleDefs.nextNode())
            {
                Rule rule = new Rule(ruleElement);
                ruleVec.add(rule);
            }
        }
        catch (TransformerException ex)
        {
            logger.warn(ex);
            return ruleVec;
        }
        
        return ruleVec;
    }
}
