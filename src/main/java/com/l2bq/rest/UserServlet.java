package com.l2bq.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableRow;
import com.google.gson.Gson;
import com.l2bq.rest.entity.DAULog;
import com.l2bq.rest.entity.JSONArrayResult;
import com.l2bq.rest.entity.LoginData;
import com.l2bq.rest.entity.SignupData;
import com.l2bq.rest.manager.BigQueryManager;
import com.l2bq.rest.manager.DAUManager;
import com.sun.jersey.api.json.JSONWithPadding;

/**
 * Log Generator via log4j class 
 * @author Junki Kim (wishoping@gmail.com), Wooseok Seo (wooseok.seo@gmail.com)
 * @date 2013. 5. 22.
 *
 */
@Path("/user")
public class UserServlet
{
	private static final String APPLOG_TAG = "AppLog";
	public static final Logger LOG = Logger.getLogger(APPLOG_TAG);
	
	public UserServlet()
	{
		// TODO Auto-generated constructor stub
	}
	
	@GET
	@Path("/users")
	@Produces("application/x-javascript")
	public JSONWithPadding getTotalUserCountByJSONP(@QueryParam("callback") String callback) {
		DAUManager man = new DAUManager();
		JSONArrayResult result = new JSONArrayResult();
		
		JSONArray dataList = man.getTotalUserCountInfo(); 
		
		if ( dataList != null ) {
			result.setList(dataList);
			result.setMsg("success");
			result.setSuccess(true);
			
			return new JSONWithPadding(result.toString(),callback);
		}
		
		result.setList(null);
		result.setMsg("failed with null result");
		result.setSuccess(false);
		
		return new JSONWithPadding(result.toString(),callback);
	}

	
	/**
	 * Application Log write via Log4j 
	 * @param msg Log Message for Application
	 */
	public void APP_LOG(String msg)
	{
		LOG.info(msg);
	}
}

