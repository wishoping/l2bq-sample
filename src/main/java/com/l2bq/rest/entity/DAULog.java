package com.l2bq.rest.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DAULog
{
	private String type;

	private LoginData data;
	
	public DAULog()
	{

	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public LoginData getData()
	{
		return data;
	}

	public void setData(LoginData data)
	{
		this.data = data;
	}

}
