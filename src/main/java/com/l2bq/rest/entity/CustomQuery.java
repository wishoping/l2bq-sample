package com.l2bq.rest.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * CustomQuery POST param POJO Class 
 * @author Junki Kim (wishoping@gmail.com)
 * @date 2013. 5. 21.
 *
 */
@XmlRootElement
public class CustomQuery {
	
	private String query;
	
	public CustomQuery() {
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
