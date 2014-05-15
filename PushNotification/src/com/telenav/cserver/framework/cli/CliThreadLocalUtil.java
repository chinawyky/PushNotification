package com.telenav.cserver.framework.cli;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorRequest;

public class CliThreadLocalUtil
{

    private static ThreadLocal<CliEntity> cli = new ThreadLocal<CliEntity>() {
        protected CliEntity initialValue()
        {
            CliEntity entity = new CliEntity();
            cli.set(entity);
        
            return entity;
        }
    };
    
    public static void setCliThreadLocal(ExecutorRequest[] request)
    {
        CliEntity entity = new CliEntity();
        if (request != null && request.length > 0)
        {
            entity.setUserProfile(request[0].getUserProfile());
            
            StringBuffer sb = new StringBuffer();
            for(int i=0; i<request.length; i++)
            {
                sb.append(request[i].getExecutorType()).append(",");
            }
            if (sb.toString().endsWith(","))
                sb.deleteCharAt(sb.lastIndexOf(","));
            
            entity.setAllExecutorType(sb.toString());
        }
        
        cli.set(entity);
    }
    
    public static void setSingleExecutorType(String executorType)
    {
        CliEntity entity = cli.get();
        entity.setExecutorType(executorType);
    }
    
    public static UserProfile getUserProfile()
    {
        return cli.get().getUserProfile();
    }
    
    public static void setSingleUserProfile(UserProfile profile)
    {
        CliEntity entity = cli.get();
        entity.setUserProfile(profile);
    }
    
    public static String getExecutorType()
    {
        String executorType = cli.get().getExecutorType();
        if (executorType == null)
            executorType = cli.get().getAllExecutorType();
        
        return executorType;
    }
    
    public static void removeCliEntity()
    {
        cli.remove();
    }
    
    //test
//    public static void main(String args[])
//    {
//        UserProfile userProfile = new UserProfile();
//        userProfile.setCarrier("ATT");
//        
//        ExecutorRequest[] requests = new ExecutorRequest[2];
//        requests[0] = new ExecutorRequest();
//        requests[0].setUserProfile(userProfile);
//        requests[0].setExecutorType("Map");
//        
//        requests[1] = new ExecutorRequest();
//        requests[1].setUserProfile(userProfile);
//        requests[1].setExecutorType("Dynamical_Route");
//        
//        CliThreadLocalUtil.setCliThreadLocal(requests);
//        
//        System.out.println(CliThreadLocalUtil.getUserProfile().getCarrier());
//        CliThreadLocalUtil.setSingleExecutorType("Map");
//        
//        testSub();
//        
//        
//    }
    
//    private static void testSub()
//    {
//        System.out.println(CliThreadLocalUtil.getUserProfile().getCarrier());
//        System.out.println(CliThreadLocalUtil.getExecutorType());
//        CliThreadLocalUtil.setSingleExecutorType("Map");
//        System.out.println(CliThreadLocalUtil.getExecutorType());
//    }

}

class CliEntity
{
    private UserProfile userProfile;
    private String allExecutorType;
    private String executorType;
    
    
    public String getAllExecutorType()
    {
        return allExecutorType;
    }
    public void setAllExecutorType(String allExecutorType)
    {
        this.allExecutorType = allExecutorType;
    }
    public String getExecutorType()
    {
        return executorType;
    }
    public void setExecutorType(String executorType)
    {
        this.executorType = executorType;
    }
    public UserProfile getUserProfile()
    {
        return userProfile;
    }
    public void setUserProfile(UserProfile userProfile)
    {
        this.userProfile = userProfile;
    }
}
