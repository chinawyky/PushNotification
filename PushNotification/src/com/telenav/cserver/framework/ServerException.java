package com.telenav.cserver.framework ;

/**
 * ServerException
 *
 * @author <a href="mailto:yqchen@telenav.cn">yqchen</a>
 * @version 1.00 2006-7-25 15:52:18
 */
public class ServerException extends Exception
{
	/**
	 * error code
	 */
	private int code;

	public ServerException(int code, String message)
	{
		super(message);
		this.code = code;
	}

	public ServerException(int code)
	{
		super(Integer.toString(code));
		this.code = code;
	}

	public ServerException()
	{
	}

	public ServerException(Throwable cause)
	{
		super(cause);
	}

	public ServerException(int code, Throwable cause)
	{
		super(Integer.toString(code), cause);
		this.code = code;
	}

	public ServerException(String message)
	{
		super(message);
	}

	public ServerException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ServerException(int code, String message, Throwable cause)
	{
		super(message, cause);
		this.code = code;
	}

	/**
	 * return error code
	 *
	 * @return
	 */
	public int getCode()
	{
		return code;
	}

}
