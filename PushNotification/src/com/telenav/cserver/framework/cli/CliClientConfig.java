package com.telenav.cserver.framework.cli;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.telenav.cserver.framework.UserProfile;

public class CliClientConfig
{
    private static String CONFIG_FILE = "cli_client.xml";
    private static final String ACTION_CONFIG_ROOT = "actions";
    private static final String ACTION_ROOT = "action";
    private static Category logger = Category.getInstance(CliClientConfig.class);
    private static Map<CliClientConfigKey, Boolean> cliClientHolder = getCliClientHolder();
    
    
//    public static void main(String[] args)
//    {
//        UserProfile user = new UserProfile();
//        user.setCarrier("ATT");
//        user.setPlatform("RIM");
//        user.setVersion("6.0.1");
//        boolean b = getCliClientConfig(user, "Map");
//        System.out.println(b);
//    }
    
    
    
    /**
     * get the cli enable/disable flag from config file
     * the default value is true
     * @param userProfile
     * @param executorType
     * @return
     */
    public static boolean getCliClientConfig(UserProfile userProfile, String executorType)
    {
        CliClientConfigKey key = new CliClientConfigKey();
        key.setCarrier(userProfile==null?"":userProfile.getCarrier());
        key.setType(executorType);
        
        Boolean b = cliClientHolder.get(key);
        if (b == null)
        {
            key.setCarrier("");
            b = cliClientHolder.get(key);
            
            if (b == null)
                return true;
            else
                return b.booleanValue();
        }
        else 
            return b.booleanValue();
    }
    
    private static Map<CliClientConfigKey, Boolean> getCliClientHolder()
    {
        Map<CliClientConfigKey, Boolean> map = new HashMap<CliClientConfigKey, Boolean>();
        try
        {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream stream =  cl.getResourceAsStream(CONFIG_FILE);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            
            // parse config
            Document doc = factory.newDocumentBuilder().parse(stream);
            Element root = doc.getDocumentElement();
            NodeList childNodes = root.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++)
            {
                Node childNode = childNodes.item(i);
                String nodeName = childNode.getNodeName();
                if (ACTION_CONFIG_ROOT.equalsIgnoreCase(nodeName))
                {
                    // parse config in <holders></holders>
                    parseActionsConfig(childNode, map);
                }
            }
        }
        catch (Exception e)
        {
            logger.warn("CliClientConfig::parseConfig:" + e.getMessage());
        } 

        return map;
       
    }

    private static void parseActionsConfig(Node node, Map<CliClientConfigKey, Boolean> map)
    {
        Element element = (Element) node;

        NodeList nodeList = element.getChildNodes();
        // parse elements
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node child = nodeList.item(i);

            if (ACTION_ROOT.equalsIgnoreCase(child.getNodeName()))
            {
                Element actionElement = (Element) child;

                String type = actionElement.getAttribute("type");
                String carrier = actionElement.getAttribute("carrier");
                String value = actionElement.getAttribute("value");
                
                CliClientConfigKey key = new CliClientConfigKey();
                key.setType(type);
                key.setCarrier(carrier);
                
                map.put(key, Boolean.valueOf(value));
            }
        }

    }
}


class CliClientConfigKey
{
    private String type;
    private String carrier;
    public String getCarrier()
    {
        return carrier;
    }
    public void setCarrier(String carrier)
    {
        this.carrier = carrier;
    }
    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public boolean equals(Object anObject)
    {
        if (this == anObject) 
        {
            return true;
        }
        if (anObject instanceof CliClientConfigKey)
        {
            CliClientConfigKey anotherKey = (CliClientConfigKey)anObject;
            String anotherCarrier = anotherKey.getCarrier();
            String anotherType = anotherKey.getType();
            
            //a simple way, because there is no possibility that have "\r\n" in these value 
            String split = "\r\n";
            String anotherStr = anotherCarrier + split + anotherType;
            String str = carrier + split + type;
            
            return str.equals(anotherStr);
            
        }
        return false;
            
    }
    
    public int hashCode()
    {
        //a simple way, because there is no possibility that have "\r\n" in these value 
        String split = "\r\n";
        String str = carrier + split + type;
        
        return str.hashCode();
    }
    
}