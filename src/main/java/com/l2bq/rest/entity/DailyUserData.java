package com.l2bq.rest.entity;

/**
 * Daily User Total Result
 * @author Junki Kim(wishoping@gmail.com)
 * @date 2013. 5. 20.
 */
public class DailyUserData extends Entity
{
	private String userId;
	
	public DailyUserData()
	{
		// TODO Auto-generated constructor stub
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

}