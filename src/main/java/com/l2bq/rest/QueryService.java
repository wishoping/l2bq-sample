package com.l2bq.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.l2bq.rest.entity.CustomQuery;
import com.l2bq.rest.entity.ListResult;
import com.l2bq.rest.entity.QueryResult;

/**
 * Custom Query RESTful Service Servlet Class 
 * @author Junki Kim (wishoping@gmail.com)
 * @date 2013. 5. 21.
 *
 */
@Path("/query")
public class QueryService {

	public QueryService() {
		// TODO Auto-generated constructor stub
	}
	
	@POST
	@Path("")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public QueryResult getQueryResult(CustomQuery query) {
		
		
		return null;
	}

}
