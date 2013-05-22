package com.l2bq.rest.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * {@link CustomQuery} Response Result class 
 * @author Junki Kim (wishoping@gmail.com), Wooseok Seo (wooseok.seo@gmail.com)
 * @date 2013. 5. 21.
 *
 */
@XmlRootElement
public class QueryResult extends Result {
	private CustomQuery result;
	
	public QueryResult() {
		// TODO Auto-generated constructor stub
	}

	public CustomQuery getResult() {
		return result;
	}

	public void setResult(CustomQuery result) {
		this.result = result;
	}

}
