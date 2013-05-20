package com.l2bq.rest.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Base class of RESTful Service Response
 * @author Junki Kim(wishoping@gmail.com)
 * @date 2013. 5. 20.
 */
@XmlRootElement
public class Result
{
	private boolean isSuccess = false;
	
	private String msg = null;
	
	public Result()
	{
	}
	
	public boolean isSuccess()
	{
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

}
