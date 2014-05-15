/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.username;

import org.apache.axis2.databinding.utils.Constants;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.StatusConstants;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.proxy.AbstractStubProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.datatypes.user.management.v11.Attribute;
import com.telenav.datatypes.user.v20.User;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.user.management.v11.GetAttributeRequestDTO;
import com.telenav.services.user.management.v11.GetAttributeResponseDTO;
import com.telenav.services.user.management.v11.GetUserProfileByCredentialRequestDTO;
import com.telenav.services.user.management.v11.GetUserProfileResponseDTO;
import com.telenav.services.user.management.v11.GetUserRequestDTO;
import com.telenav.services.user.management.v11.GetUserResponseDTO;
import com.telenav.services.user.management.v11.IsUniqueUsernameRequestDTO;
import com.telenav.services.user.management.v11.IsUniqueUsernameResponseDTO;
import com.telenav.services.user.management.v11.UpdateUserProfileRequestDTO;
import com.telenav.services.user.management.v11.UpdateUserRequestDTO;
import com.telenav.services.user.management.v11.UserManagementResponseDTO;
import com.telenav.services.user.management.v11.UserManagementService11Stub;

/**
 * 
 * jhjin@telenav.cn Mar 8, 2011
 * 
 */

@BackendProxy
@ThrottlingConf("UserNameServiceProxy")
public class UserNameServiceProxy extends AbstractStubProxy<UserManagementService11Stub>
{

    private static final Logger logger = Logger.getLogger(UserNameServiceProxy.class);
    
    protected UserNameServiceProxy()
    {
        
    }
    
    @ProxyDebugLog
    @Throttling
    public GetUserProfileResponseDTO getRegistrationID4UserId(GetUserProfileByCredentialRequestDTO request, TnContext tnContext)
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getUserName");

        cli.addData("GetUserProfileByCredentialRequestDTO: ", ReflectionToStringBuilder.toString(request));

        GetUserProfileResponseDTO response = null;
        UserManagementService11Stub stub=null;
        
        String registration_id = null;
        
        try
        {
            stub=createStub(getWebServiceConfigInterface()); 
            
            response = stub.getUserProfileByCredential(request);
           
            cli.addData("GetUserProfileResponseDTO: ", ReflectionToStringBuilder.toString(response));
            //cli.addData("GetUserProfileResponseDTO: RegistrationID: ", response.ge);
        }
        catch (Exception e)
        {
            logger.fatal("UserNameServiceProxy#getRegistrationID4UserId",e);
            cli.setStatus(e);
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
        return response;

    }
    
    /**
     * query user name according to userId
     * 
     * @param userNameRequest
     * @param userNameResponse
     * @param tnContext
     * @return
     */
    @ProxyDebugLog
    @Throttling
    public GetUserResponseDTO getUser(GetUserRequestDTO getUserRequest, TnContext tnContext)
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getUserName");

        cli.addData("GetUserRequestDTO", ReflectionToStringBuilder.toString(getUserRequest));

        GetUserResponseDTO response = null;
        UserManagementService11Stub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response = stub.getUser(getUserRequest);
            cli.addData("GetUserResponseDTO", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
            logger.fatal("UserNameServiceProxy#queryUserName",e);
            cli.setStatus(e);
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
        return response;

    }

    /**
     * check if user name exists
     * @param userNameRequest
     * @param tnContext
     * @return
     */
    @ProxyDebugLog
    @Throttling
    public  IsUniqueUsernameResponseDTO checkUserName(IsUniqueUsernameRequestDTO isUniqueRequest, TnContext tnContext)
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("checkUserName");
        cli.addData("IsUniqueUsernameRequestDTO", ReflectionToStringBuilder.toString(isUniqueRequest));
        
        IsUniqueUsernameResponseDTO response = null;
        UserManagementService11Stub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response = stub.isUniqueUsername(isUniqueRequest);
            cli.addData("IsUniqueUsernameResponseDTO", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
            logger.fatal("UserNameServiceProxy#checkUserName",e);
            cli.setStatus(e);
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }

        return response;
    }

    /**
     * add or update user name
     * @param addUserNameRequest
     * @param tnContext
     * @return
     */
    @ProxyDebugLog
    @Throttling
    public  SaveUserNameResponse saveUserName(SaveUserNameRequest addUserNameRequest, TnContext tnContext)
    {
        SaveUserNameResponse addUserNameResponse = new SaveUserNameResponse();
        
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("addUserName");
        
        long userId = addUserNameRequest.getUserId();
        String userName = addUserNameRequest.getUserName();
        
        GetUserRequestDTO getUserRequest = UserNameServiceHelper.newGetUserRequestDTO(userId, tnContext);
        UserManagementService11Stub stub=null;
        try
        {
            //first, get user information from server
            GetUserResponseDTO getUserResponse = getUser(getUserRequest, tnContext);
            
            if(getUserResponse == null || getUserResponse.getUser() == null)
            {
            	addUserNameResponse.setStatusCode(StatusConstants.FAIL);
                addUserNameResponse.setStatusMessage("GetUserResponseDTO or user object in GetUserResponseDTO is null");
                cli.addData("GetUserResponseDTO", "GetUserResponseDTO or user object in GetUserResponseDTO is null");
            }
            else
            {
            	User user = getUserResponse.getUser();
                user.setUserName(userName);

                //second, update user name
                UpdateUserRequestDTO updateRequest = UserNameServiceHelper.newUpdateUserRequestDTO(user, tnContext);
                cli.addData("UpdateUserRequestDTO", ReflectionToStringBuilder.toString(updateRequest));
                stub=createStub(getWebServiceConfigInterface());
                UserManagementResponseDTO updateResponse = stub.updateUser(updateRequest);
                cli.addData("UserManagementResponseDTO", ReflectionToStringBuilder.toString(updateResponse));

                if (updateResponse != null)
                {
                    addUserNameResponse.setStatusCode(StatusConstants.SUCCESS);
                }
                else
                {
                    addUserNameResponse.setStatusCode(StatusConstants.FAIL);
                    addUserNameResponse.setStatusMessage("UserManagementResponseDTO is null");
                }
            }
        }
        catch (Exception e)
        {
            logger.fatal("UserNameServiceProxy#saveUserName",e);
            cli.setStatus(e);
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
        

        return addUserNameResponse;
    }
    
    @ProxyDebugLog
    @Throttling
    public SaveRegisterInfoResponse saveRegisterInfo(SaveRegisterInfoRequest request, TnContext tc)
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("saveRegisterId");

        SaveRegisterInfoResponse resp = new SaveRegisterInfoResponse();

        // first getuser, this will triger xnav to save an user if not exist in their dataset
        GetUserRequestDTO getUserRequestDTO = UserNameServiceHelper.newGetUserRequestDTO(Long.parseLong(request.getUserProfile().getUserId()), tc);
        GetUserResponseDTO getUserResponseDTO = getUser(getUserRequestDTO, tc);

        if (getUserResponseDTO == null || (getUserResponseDTO != null && getUserResponseDTO.getUser() == null))
        {
            cli.addData("getuser failed.", "getUserResponseDTO is null or getUserResponseDTO.getUser() is null.");
        }
        else
        {
            User user = getUserResponseDTO.getUser();
            UpdateUserProfileRequestDTO updateUserProfileReq = 
                    UserNameServiceHelper.newUpdateUserProfileRequestDTOWithRegisterInfo(request.getUserProfile(), request.getRegisterId());
            updateUserProfileReq.setUser(user);
            UserManagementService11Stub stub=null;
            try
            {
                
                if(logger.isDebugEnabled())
                {
                    logger.debug("updateUserProfileReq"+ReflectionToStringBuilder.toString(updateUserProfileReq,ToStringStyle.MULTI_LINE_STYLE));
                }
                
                stub=createStub(getWebServiceConfigInterface());
                UserManagementResponseDTO userManagementResp=stub.updateUserProfile(updateUserProfileReq);
                
                if(logger.isDebugEnabled())
                {
                    logger.debug("userManagementResp"+ReflectionToStringBuilder.toString(userManagementResp,ToStringStyle.MULTI_LINE_STYLE));
                }
                
                if(userManagementResp!=null&&userManagementResp.getResponseStatus()!=null)
                {
                    cli.addData("userManagementResp status", userManagementResp.getResponseStatus().getStatusCode());
                    resp.setStatus(userManagementResp.getResponseStatus().getStatusCode());
                }
                else
                {
                    cli.addData("userManagementRespStatus", "userManagementResp is null or userManagementResp.getResponseStatus() is null.");
                }
            }
            catch (Exception e)
            {
                logger.fatal("saveRegisterId failed.");
                cli.setStatus(e);
            }
            finally
            {
                WebServiceUtils.cleanupStub(stub);
            }
        }
        
        cli.complete();
        
        return resp;
    }
    
    @ProxyDebugLog
    @Throttling
    public SaveUserAttributesResponse updateUserAttributesValue(String strUserId, Attribute[] attrs, TnContext tc)
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("updateOptinValue");
        
        long userId = Long.parseLong(strUserId);
        SaveUserAttributesResponse resp = new SaveUserAttributesResponse();
        
        GetUserRequestDTO getUserRequestDTO = UserNameServiceHelper.newGetUserRequestDTO(userId, tc);
        GetUserResponseDTO getUserResponseDTO = getUser(getUserRequestDTO, tc);

        if (getUserResponseDTO == null || (getUserResponseDTO != null && getUserResponseDTO.getUser() == null))
        {
        	cli.addData("getuser failed.", "getUserResponseDTO is null or getUserResponseDTO.getUser() is null.");
        	resp.setStatusMessage("getUserResponseDTO is null or getUserResponseDTO.getUser() is null.");
        }
        else
        {
        	User user = getUserResponseDTO.getUser();
        	UpdateUserProfileRequestDTO updateUserProfileReq = UserNameServiceHelper.newUpdateUserProfileRequestDTOWithAttributes(attrs);
        	updateUserProfileReq.setUser(user);
        	UserManagementService11Stub stub=null;
        	try
            { 
                if(logger.isDebugEnabled())
                {
                    logger.debug("updateUserProfileReq"+ReflectionToStringBuilder.toString(updateUserProfileReq,ToStringStyle.MULTI_LINE_STYLE));
                }
                stub=createStub(getWebServiceConfigInterface());
                UserManagementResponseDTO userManagementResp=stub.updateUserProfile(updateUserProfileReq);
                
                if(logger.isDebugEnabled())
                {
                    logger.debug("userManagementResp"+ReflectionToStringBuilder.toString(userManagementResp,ToStringStyle.MULTI_LINE_STYLE));
                }
                
                if(userManagementResp!=null&&userManagementResp.getResponseStatus()!=null)
                {
                    cli.addData("userManagementResp status", userManagementResp.getResponseStatus().getStatusCode());
                    resp.setStatusCode(StatusConstants.SUCCESS);
                }
                else
                {
                    cli.addData("userManagementRespStatus", "userManagementResp is null or userManagementResp.getResponseStatus() is null.");
                    resp.setStatusCode(StatusConstants.FAIL);
                    resp.setStatusMessage("UserManagementResponseDTO is null");
                }
            } 
        	catch (Exception e)
            {
                logger.fatal("updateOptinValue failed.");
                cli.setStatus(e);
            }
        	finally
        	{
        	    WebServiceUtils.cleanupStub(stub);
        	}
        }
        cli.complete();
        return resp;
    }
    
    @ProxyDebugLog
    @Throttling
    public GetAttributeResponseDTO getAttribute(String strUserId, String attributeName,TnContext tc) 
    {
    	CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getUserOptinAttribute");
        
        GetAttributeResponseDTO resp = null ;
        long userId = Long.parseLong(strUserId);
        
        GetUserRequestDTO getUserRequestDTO = UserNameServiceHelper.newGetUserRequestDTO(userId, tc);
        GetUserResponseDTO getUserResponseDTO = getUser(getUserRequestDTO, tc);

        if (getUserResponseDTO == null || (getUserResponseDTO != null && getUserResponseDTO.getUser() == null))
        {
            cli.addData("getuser failed.", "getUserResponseDTO is null or getUserResponseDTO.getUser() is null.");
        }
        else
        {
            User user = getUserResponseDTO.getUser();
            GetAttributeRequestDTO getAttrRequest = UserNameServiceHelper.newGetAttributeRequestDTO(attributeName);
            getAttrRequest.setUser(user);
            UserManagementService11Stub stub=null;
            try
            {
                
                if(logger.isDebugEnabled())
                {
                    logger.debug("getAttributeReq"+ReflectionToStringBuilder.toString(getAttrRequest,ToStringStyle.MULTI_LINE_STYLE));
                }
                stub=createStub(getWebServiceConfigInterface());
                resp = stub.getAttribute(getAttrRequest);
                
                if(logger.isDebugEnabled())
                {
                    logger.debug("getAttributeResp"+ReflectionToStringBuilder.toString(resp,ToStringStyle.MULTI_LINE_STYLE));
                }
                
                if(resp!=null&&resp.getResponseStatus()!=null)
                {
                    cli.addData("getAttributeResp status", resp.getResponseStatus().getStatusCode());
                    if(resp.getAttribute()!=null)
                    {
                    	cli.addData("attribute name", ""+resp.getAttribute().getName());
                    	cli.addData("attribute value", ""+resp.getAttribute().getValue()[0]);
                    }
                }
                else
                {
                    cli.addData("getAttributeRespStatus", "getAttributeResp is null or getAttributeResp.getResponseStatus() is null.");
                }
            }
            catch (Exception e)
            {
                logger.fatal("getUserOptinAttrbute failed.");
                cli.setStatus(e);
            }
            finally
            {
                WebServiceUtils.cleanupStub(stub);
            }
        }
        
        cli.complete();
        return resp;
    }
    
    @Override
    protected UserManagementService11Stub createStub(WebServiceConfigInterface ws) throws Exception
    {
        UserManagementService11Stub stub = null;
        try
        {
            stub = new UserManagementService11Stub(createContext(ws), ws.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout());

        }
        catch (Exception e)
        {
            logger.fatal("create ServiceProvisioningStub stub failed", e);
        }

        return stub;
    }

    @Override
    public String getProxyConfType()
    {
        return "USERNAME";
    }

}
