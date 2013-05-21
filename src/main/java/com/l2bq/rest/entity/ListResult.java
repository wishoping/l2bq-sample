package com.l2bq.rest.entity;

import java.util.List;

/**
 * List 형태의 Result Set 을 포함하는 RESTful Service Response Class 
 * @author Junki Kim(wishoping@gmail.com)
 * @date 2013. 5. 20.
 */
public class ListResult<T> extends Result
{
	private List<T> list = null;
	
	public ListResult()
	{
	}

	public List<T> getList()
	{
		return list;
	}

	public void setList(List<T> list)
	{
		this.list = list;
	}
}
