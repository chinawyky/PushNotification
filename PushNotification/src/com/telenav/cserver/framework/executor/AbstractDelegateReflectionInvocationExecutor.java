/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;


/**
 * AbstractDelegateReflectionInvocationExecutor, this class is used to wrap other existing object and to expose some
 * operations. Method setDelegate(Object delegate) used to set the delegate, which we want to invoke operations on.
 * 
 * @author kwwang
 * 
 */
public abstract class AbstractDelegateReflectionInvocationExecutor extends AbstractExecutor
{
    protected Logger logger = Logger.getLogger(getClass());

    protected final Map<String, Method> handledMethods = new HashMap<String, Method>();

    protected final Map<Class, Method> exceptionHandledMethods = new HashMap<Class, Method>();

    private Object delegate;

    private Object exceptionHandler;

    public void setDelegate(Object delegate)
    {
        this.delegate = delegate;
        registerHandledMethods();
        registerExceptionHandledMethods();
    }

    protected void registerHandledMethods()
    {
        handledMethods.clear();

        if (this.delegate == null)
            this.delegate = this;

        Method[] methods = delegate.getClass().getMethods();
        for (Method m : methods)
        {
            registerHandledMethod(m);
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("handledMethods->");
            for (Map.Entry<String, Method> entry : handledMethods.entrySet())
            {
                logger.debug(entry.getKey() + " ->" + entry.getValue());
            }
        }
    }

    protected void registerExceptionHandledMethods()
    {
        exceptionHandler = this;

        Method[] methods = exceptionHandler.getClass().getMethods();
        for (Method m : methods)
        {
            if (isExceptionHandledMethod(m))
            {
                exceptionHandledMethods.put(m.getParameterTypes()[3], m);
            }
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("exceptionHandledMethods->");
            for (Map.Entry<Class, Method> entry : exceptionHandledMethods.entrySet())
            {
                logger.debug(entry.getKey() + " ->" + entry.getValue());
            }
        }
    }

    protected boolean isExceptionHandledMethod(Method m)
    {
        Class[] paramTypes = m.getParameterTypes();
        return paramTypes.length == 4 && ExecutorRequest.class.isAssignableFrom(paramTypes[0])
                && ExecutorResponse.class.isAssignableFrom(paramTypes[1]) && ExecutorContext.class.isAssignableFrom(paramTypes[2])
                && Throwable.class.isAssignableFrom(paramTypes[3]) && !m.getName().equals("handleExceptions");
    }

    @Override
    public final void doExecute(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context) throws ExecutorException
    {
        try
        {
            List<String> invocationsList = beforeInvocation(req, resp, context);

            if (logger.isDebugEnabled())
            {
                logger.debug("invocationsList->" + ReflectionToStringBuilder.toString(invocationsList));
            }

            for (String invocation : invocationsList)
            {
                Method m = handledMethods.get(invocation);
                if (m == null)
                {
                    logger.error("can't handle invocation," + invocation);
                }
                else
                {
                    m.invoke(delegate, getInvocationParams(req, resp, context));
                }
            }
        }
        catch (InvocationTargetException ex)
        {
            handleExceptions(req, resp, context, ex.getTargetException());
        }
        catch (Exception e)
        {
            handleExceptions(req, resp, context, e);
        }
    }

    protected final void handleExceptions(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context, Throwable ex)
    {
    	CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("handleExceptions");
        cli.setStatus(ex);
        cli.complete();
        
        Method m = getExceptionHandler(ex);
        if (m != null)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("found method for exception " + ex.getClass() + " method->" + m);
            }

            try
            {
                Object[] params = getInvocationParams(req, resp, context);
                m.invoke(this.exceptionHandler, new Object[]
                { params[0], params[1], params[2], ex });
            }
            catch (Exception e)
            {
                logger.fatal("method " + m + " handled exception " + ex.getClass() + " failed.", e);
            }
        }
        else
        {
            logger.fatal("There is no method to handle exception " + ex.getClass(), ex);
        }
    }

    protected Method getExceptionHandler(Throwable ex)
    {
        Class exClz = ex.getClass();
        Method m = this.exceptionHandledMethods.get(exClz);
        while (m == null && !exClz.equals(Throwable.class))
        {
            exClz = exClz.getSuperclass();
            m = this.exceptionHandledMethods.get(exClz);
        }
        return m;
    }

    private String desc;

    /**
     * @return the desc
     */
    public String getDesc()
    {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    /**
     * This is to handle all uncaught exceptions, just log the failure by default.
     * 
     * @param req
     * @param resp
     * @param context
     * @param ex
     */
    protected final void handleUnCaughtExceptions(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context, Throwable ex)
    {
        logger.fatal("handleUnCaughtExceptions#There is no method to handle exception " + ex.getClass(), ex);
    }

    /**
     * beforeInvocation, let user can do some stuff before invoking the operations.This method return the keys that
     * matches the registered handled method when we do in registerHandledMethod(Method m)
     * 
     * @param req
     * @param resp
     * @param context
     * @return
     * @throws Exception
     */
    abstract protected List<String> beforeInvocation(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context) throws Exception;

    /**
     * When invoking the registered methods, we need to pass by the parameter, now the parameter is defined as
     * (ExecutorRequest req, ExecutorResponse resp, ExecutorContext context), what this method does here is only to
     * convert the ExecutorRequest and ExecutorResponse to the specific sub request and response.
     * 
     * @param req
     * @param resp
     * @param context
     * @return
     */
    abstract protected Object[] getInvocationParams(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context);

    /**
     * This tells user how to register a method, now method can be registered by annotation as
     * CombinationSyncResourceExecutor does, or we can make a new convention for the registered method, in a word, it
     * defines the method-registering strategy, for example, we can register all methods which starts with a prefix.
     * 
     * @param m
     */
    abstract protected void registerHandledMethod(Method m);

}
