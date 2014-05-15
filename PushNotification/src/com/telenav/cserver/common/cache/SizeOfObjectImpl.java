/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.cache;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

//import net.sourceforge.sizeof.SizeOf;

/**
 * SizeOfObjectImpl.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-1-28
 * 
 */
public class SizeOfObjectImpl implements SizeOfObject
{
    private static final Logger LOGGER = Logger.getLogger(SizeOfObjectImpl.class);

    private static OutputStream out = System.out;

    private static long MIN_CLASS_SIZE_TO_LOG = 10 * 1024 * 1024;

    private static boolean SKIP_STATIC_FIELD = false;

    private static boolean SKIP_FINAL_FIELD = false;

    private static boolean SKIP_FLYWEIGHT_FIELD = false;

    private static boolean debug = false;
    
    public static final int OBJECT_SHELL_SIZE   = 8;
    public static final int OBJREF_SIZE         = 4;
    public static final int LONG_FIELD_SIZE     = 8;
    public static final int INT_FIELD_SIZE      = 4;
    public static final int SHORT_FIELD_SIZE    = 2;
    public static final int CHAR_FIELD_SIZE     = 2;
    public static final int BYTE_FIELD_SIZE     = 1;
    public static final int BOOLEAN_FIELD_SIZE  = 1;
    public static final int DOUBLE_FIELD_SIZE   = 8;
    public static final int FLOAT_FIELD_SIZE    = 4;
    
    public static final int ALIGNMENT = 8;


    private static String[] unit = { "Byte", "KByte", "MByte", "GByte" };

    /**
     * Format size in a human readable format
     * 
     * @param size
     * @return a string representation of the size argument followed by b for byte, Kb for kilobyte or Mb for megabyte
     */
    public String humanReadable(long size)
    {
        int i = 0;
        double dSize = size;
        for (i = 0; i < 3; ++i)
        {
            if (dSize < 1024)
                break;
            dSize /= 1024;
        }
        DecimalFormat df = null;
        if (dSize - (int) dSize < 0.001){ 
            df = new DecimalFormat("#####");
        }
        else{
            df = new DecimalFormat("#####.00");
        }

        return df.format(dSize) + unit[i];
    }
    

    /**
     * Compute an implementation-specific approximation of the amount of storage consumed by objectToSize and by all the
     * objects reachable from it
     * 
     * @param object
     * @return an implementation-specific approximation of the amount of storage consumed by objectToSize and by all the
     *         objects reachable from it
     */
    public long deepSizeOf(Object object)
    {
        Map<Object, Object> doneObj = new IdentityHashMap<Object, Object>();
        return deepSizeOf(object, doneObj, 0);
    }

    private static String indent(int depth)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < depth; i++)
            builder.append("  ");

        return builder.toString();
    }

    private long deepSizeOf(Object o, Map<Object, Object> doneObj, int depth)
    {
        if (o == null)
        {
            if (debug)
            {
                print("null\n");
            }
            return 0;
        }

        long size = 0;

        if (doneObj.containsKey(o))
        {
            if (debug)
            {
                print("\n%s{ yet computed }\n", indent(depth));
            }
            return 0;
        }
        if (debug)
        {
            print("\n%s{ %s\n", indent(depth), o.getClass().getName());
        }

        doneObj.put(o, null);
        
     
        size = sizeOf(o);
        

        if (o instanceof Object[])
        {
            int i = 0;
            for (Object obj : (Object[]) o)
            {
                if (debug)
                {
                    print("%s [%d] = ", indent(depth), i++);
                }
                size += deepSizeOf(obj, doneObj, depth + 1);
            }
        }
        else
        {

            Field[] fields = o.getClass().getDeclaredFields();

            for (Field field : fields)
            {
                field.setAccessible(true);
                Object obj;
                try
                {
                    obj = field.get(o);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }

                if (isComputable(field))
                {
                    if (debug)
                    {
                        print("%s %s = ", indent(depth), field.getName());
                    }
                    size += deepSizeOf(obj, doneObj, depth + 1);
                }
                else
                {
                    if (debug)
                    {
                        print("%s %s = %s\n", indent(depth), field.getName(), obj.toString());
                    }
                }
            }
        }

        if (debug)
        {
            print("%s} size = %s\n", indent(depth), humanReadable(size));
        }

        
        if (MIN_CLASS_SIZE_TO_LOG > 0 && size >= MIN_CLASS_SIZE_TO_LOG)
        {
            print("Found big object: %s%s@%s size: %s\n", indent(depth), o.getClass().getName(), System.identityHashCode(o),
                humanReadable(size));
        }

        return size;
    }
    
    /**
     * get size of an object, but we don't compute the objects reachable from it.
     * @param object
     * @return
     */
    private static long sizeOf(Object object)
    {
        //size of null object is 0
        if( object == null)
            return 0;
        
        //every object needs object shell memory
        long result = OBJECT_SHELL_SIZE;  
        
        //process array object
        if(  object.getClass().isArray() ){
            result += processClassArray(object);
        }
        //otherwise, the size of object is sum of memory of every field
        else
        {
            result += processNonArrayClass(object);
        }
        
        //align to 8 bytes boundary
        if( result%ALIGNMENT != 0 ){
            result = (result/ALIGNMENT+1)*ALIGNMENT;
        }
        return result;
    }
    
    private static long processClassArray(Object object)
    {
    	long result = 0;
    	int length = Array.getLength(object);
        Class componentType = object.getClass().getComponentType();
        //primitive array
        if( componentType.isPrimitive()){
            result += getSizeOfPrimitiveType(componentType)*length; 
        }else{
             result += OBJREF_SIZE*length;
        }
        return result;    	
    }
    
    private static long processNonArrayClass(Object object)
    {
    	long result = 0;
    	Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);
            int modificatori = field.getModifiers();
            if ((SKIP_STATIC_FIELD && Modifier.isStatic(modificatori)) || (SKIP_FINAL_FIELD && Modifier.isFinal(modificatori)) )
            {
                continue;
            }
            else if (field.getType().isPrimitive())
            {
                result += getSizeOfPrimitiveType(field.getType());
            }
            //it's a object type, we add the object reference, but the object will be processed later
            else
            {
                result += OBJREF_SIZE;
            }
        }
        return result;
    	
    }
   
    
    /**
     * return size of primitiveType
     * @param clazz
     * @return
     */
    private static long getSizeOfPrimitiveType(Class clazz)
    {
        if (clazz == java.lang.Boolean.TYPE)
            return BOOLEAN_FIELD_SIZE;

        if (clazz == java.lang.Character.TYPE)
            return CHAR_FIELD_SIZE;

        if (clazz == java.lang.Byte.TYPE)
            return BYTE_FIELD_SIZE;

        if (clazz == java.lang.Short.TYPE)
            return SHORT_FIELD_SIZE;

        if (clazz == java.lang.Integer.TYPE)
            return INT_FIELD_SIZE;

        if (clazz == java.lang.Long.TYPE)
            return LONG_FIELD_SIZE;

        if (clazz == java.lang.Float.TYPE)
            return FLOAT_FIELD_SIZE;

        if (clazz == java.lang.Double.TYPE)
            return DOUBLE_FIELD_SIZE;
        
        //TODO how big is void 
        if (clazz == java.lang.Void.TYPE)
            return 1;
        
        throw new IllegalArgumentException(clazz +" is not a primitive type!");
    }
    

    /**
     * @param field
     * @param obj
     * @return true if the field must be computed
     */
    private static boolean isComputable(Field field)
    {
        int modificatori = field.getModifiers();

        if ( field.getType().isPrimitive())
            return false;
        else if (SKIP_STATIC_FIELD && Modifier.isStatic(modificatori))
            return false;
        else if (SKIP_FINAL_FIELD && Modifier.isFinal(modificatori))
            return false;
        else
            return true;
    }   

    /**
     * The objects processed by deepSizeOf are logged to the log output stream if their size (in byte) is greater than
     * this value. The default value is 1024*1024 (1 megabyte), 0 turn off logging
     */
    public static void setMinSizeToLog(long min_class_size_to_log)
    {
        MIN_CLASS_SIZE_TO_LOG = min_class_size_to_log;
    }

    /**
     * If true deepSizeOf() doesn't compute the final fields of an object. Default value is false.
     */
    public static void skipFinalField(boolean skip_final_field)
    {
        SKIP_FINAL_FIELD = skip_final_field;
    }

    /**
     * If true deepSizeOf() doesn't compute the static fields of an object. Default value is false.
     */
    public static void skipStaticField(boolean skip_static_field)
    {
        SKIP_STATIC_FIELD = skip_static_field;
    }

    /**
     * If true flyweight objects has a size of 0. Default value is false.
     */
    public static void skipFlyweightObject(boolean skip)
    {
        SKIP_FLYWEIGHT_FIELD = skip;
    }

    private static void print(String s)
    {
        try
        {
            out.write(s.getBytes());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void print(String s, Object... args)
    {
        try
        {
            out.write(String.format(s, args).getBytes());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the OutputStream to use for logging. The default OutputStream is System.out
     * 
     * @param o
     */
    public static void setLogOutputStream(OutputStream o)
    {
        if (o == null)
        {
            throw new IllegalArgumentException("Can't use a null OutputStream");
        }
        out = o;
    }

    /**
     * Turn on debugging information
     */
    public static void turnOnDebug()
    {
        debug = true;
    }

    /**
     * Turn off debugging information
     */
    public static void turnOffDebug()
    {
        debug = false;
    }   
}
