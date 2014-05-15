package com.telenav.cserver.framework.util;

import java.util.Locale;

import com.telenav.j2me.datatypes.TxNode;

// Got this class from CommonUtil of TN5.x and renamed it to TxNodeUtil
public class TxNodeUtil
{

	
    
    /**
     * get a clone of input TxNode
     * 
     * @param node
     * @return
     */
    public static TxNode clone(TxNode node)
    {
        if(node == null)
        {
            return null;
        }
        TxNode retNode = new TxNode();
        for(int j =0; j < node.getValuesCount(); j ++)
        {
            retNode.addValue(node.valueAt(j));
        }
        for(int j =0; j < node.msgsSize(); j ++)
        {
            retNode.addMsg(node.msgAt(j));
        }
        retNode.setBinData(node.getBinData());
        
        int childSize = node.childrenSize();
        
        for(int j =0; j < childSize; j ++)
        {
            retNode.addChild(clone(node.childAt(j)));
        }
        return retNode;
    }
    
    
//    public static void main(String[] args)
//    {
//        TxNode node1 = new TxNode();
//        node1.addValue(1);
//        node1.addValue(2);
//        node1.addValue(333);
//        
//        node1.addMsg("111");
//        node1.addMsg("222");
//        
//        node1.setBinData(new byte[]{1,2});
//        
//        
//        TxNode childNode = new TxNode();
//        childNode.addValue(10001);
//        childNode.addValue(10002);
//        childNode.addValue(10003);
//        childNode.addMsg("childNode msg");
//        childNode.setBinData(new byte[]{1,2});
//        node1.addChild(childNode);
//        
//        TxNode copy = clone(node1);
//        
//        System.out.println(copy);
//        
//    }
}
