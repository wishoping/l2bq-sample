package com.l2bq.rest.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Daily Active User Log Entity Class
 * @author Junki Kim (wishoping@gmail.com), Wooseok Seo (wooseok.seo@gmail.com)
 * @date 2013. 5. 22.
 *
 */
@XmlRootElement
public class DAULog
{
	private String type;

	private Entity data;
	
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

	public Entity getData()
	{
		return data;
	}

	public void setData(Entity data)
	{
		this.data = data;
	}

}
