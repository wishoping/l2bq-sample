package com.l2bq.rest.entity;

import java.util.List;

/**
 * List 형태의 Result Set 을 포함하는 RESTful Service Response Class 
 * @author Junki Kim(wishoping@gmail.com)
 * @date 2013. 5. 20.
 */
public class ListResult extends Result
{
	private List<Entity> list = null;
	
	public ListResult()
	{
	}

	public List<Entity> getList()
	{
		return list;
	}

	public void setList(List<Entity> list)
	{
		this.list = list;
	}

	
}
