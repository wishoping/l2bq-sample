package com.junseo.l2bq.rest.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginData extends Entity
{
	private long time;
	
	private int userId;
	
	private String userName;
	
	private int langType;
	
	private String clientVer;
	
	private int osType;
	
	public LoginData()
	{
		
	}

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int id)
	{
		this.userId = id;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public int getLangType()
	{
		return langType;
	}

	public void setLangType(int langType)
	{
		this.langType = langType;
	}

	public String getClientVer()
	{
		return clientVer;
	}

	public void setClientVer(String clientVer)
	{
		this.clientVer = clientVer;
	}

	public int getOsType()
	{
		return osType;
	}

	public void setOsType(int osType)
	{
		this.osType = osType;
	}

}
