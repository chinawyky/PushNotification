package com.telenav.cserver.framework.threadpool;

public class ThreadHandler implements Runnable 
{
	protected static final long WAIT_TIMEOUT = 60000; //10 min
	
    protected Object handlerMutex = new Object();
    
    private ThreadPool pool;
    
    private int handlerID = 0;
    
    protected ThreadHandler(ThreadPool pool, int handlerID)
    {
    	this.pool = pool;
    	this.handlerID = handlerID;
    }
    
	@Override
	public void run() 
	{
	    while(true)
	    {
	    	try
	    	{
	    		Job job = this.pool.getNextJob();
	    		if(job == null)
	    		{
	    			synchronized(handlerMutex)
	    			{
	    				this.pool.addToWaitQueue(this);
	    				
	    				long sTime = System.currentTimeMillis();
	    				try
	    				{
	    					handlerMutex.wait(WAIT_TIMEOUT);
	    				}catch(InterruptedException ie)
	    				{
	    					//TODO: do something at this moment
	    					this.pool.removeFromWaitQueue(this);
	    					throw new Exception("Thread stoped !!!", ie);
	    				}
	    				long dt = System.currentTimeMillis() - sTime;
	    				if(dt >= WAIT_TIMEOUT)
	    				{
	    					this.pool.removeFromWaitQueue(this);
	    				}
	    			}
	    		}
	    		else
	    		{
	    			job.doIt(this.handlerID);
	    		}
	    	}
	    	catch(Exception e)
	    	{
	    	    e.printStackTrace();	
	    	}
	    }
	}

}
