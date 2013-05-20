package com.junseo.l2bq.rest.entity;

/**
 * Daily User Total Result
 * @author Junki Kim(wishoping@gmail.com)
 * @date 2013. 5. 20.
 */
public class HourlyUserData extends Entity
{
	private String userId;
	
	public HourlyUserData()
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
