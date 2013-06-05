package com.l2bq.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.l2bq.rest.entity.JSONArrayResult;
import com.l2bq.rest.manager.DAUManager;
import com.l2bq.rest.manager.HTTPManager;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/http")
public class HTTPServlet {

	private static final String APPLOG_TAG = "AppLog";
	public static final Logger LOG = Logger.getLogger(APPLOG_TAG);
	
	public HTTPServlet() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Get Total User Count from signup table 
	 * @param callback javascript callback function name from client 
	 * @return
	 */
	@GET
	@Path("/totalCount")
	@Produces("application/x-javascript")
	public JSONWithPadding getTotalUserCountByJSONP(@QueryParam("callback") String callback) {
		HTTPManager man = new HTTPManager();
		JSONArrayResult result = new JSONArrayResult();
		
		JSONArray dataList = man.getTotalHttpLogCount(); 
		
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
	 * Get HTTP Logs Statistics information 
	 * @param callback javascript callback function name from client 
	 * @return
	 */
	@GET
	@Path("/stats")
	@Produces("application/x-javascript")
	public JSONWithPadding getHttpLogStatsByJSONP(@QueryParam("callback") String callback) {
		HTTPManager man = new HTTPManager();
		JSONArrayResult result = new JSONArrayResult();
		
		JSONArray dataList = man.getHttpLogsStats(); 
		
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
	 * Get Critical Level HTTP Logs information 
	 * @param callback javascript callback function name from client 
	 * @return
	 */
	@GET
	@Path("/logs/critical/{limit}")
	@Produces("application/x-javascript")
	public JSONWithPadding getCriticalLevelHttpLogByJSONP(@QueryParam("callback") String callback, @PathParam("limit") int limit) {
		HTTPManager man = new HTTPManager();
		JSONArrayResult result = new JSONArrayResult();
		
		JSONArray dataList = man.getHttpCriticalLogs(limit);
		
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
	 * Get Warning Level HTTP Logs information 
	 * @param callback javascript callback function name from client 
	 * @return
	 */
	@GET
	@Path("/logs/warning/{limit}")
	@Produces("application/x-javascript")
	public JSONWithPadding getWarningLevelHttpLogByJSONP(@QueryParam("callback") String callback, @PathParam("limit") int limit) {
		HTTPManager man = new HTTPManager();
		JSONArrayResult result = new JSONArrayResult();
		
		JSONArray dataList = man.getHttpWarningLogs(limit);
		
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
	 * Get Info Level HTTP Logs information 
	 * @param callback javascript callback function name from client 
	 * @return
	 */
	@GET
	@Path("/logs/info/{limit}")
	@Produces("application/x-javascript")
	public JSONWithPadding getInfoLevelHttpLogByJSONP(@QueryParam("callback") String callback, @PathParam("limit") int limit) {
		HTTPManager man = new HTTPManager();
		JSONArrayResult result = new JSONArrayResult();
		
		JSONArray dataList = man.getHttpInfoLogs(limit);
		
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
	 * Get detailed HTTP Logs information 
	 * @param callback javascript callback function name from client 
	 * @return
	 */
	@GET
	@Path("/detail/{index}")
	@Produces("application/x-javascript")
	public JSONWithPadding getDetailedHttpLogByJSONP(@QueryParam("callback") String callback, @PathParam("index") long index) {
		HTTPManager man = new HTTPManager();
		JSONArrayResult result = new JSONArrayResult();
		
		JSONArray dataList = man.getDetailedHttpLogs(index);
		
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
	 * Get Search Result by Keyword and limit count
	 * @param callback javascript callback function name from client 
	 * @param keyword Search keyword( If contain spaces, we will use splited keyword by spaces as 'and' condition
	 * @param limit row limit count
	 * @return
	 */
	@GET
	@Path("/search/{keyword}/{limit}")
	@Produces("application/x-javascript")
	public JSONWithPadding getHTTPAppLogSearchResultByKeyword(@QueryParam("callback") String callback, @PathParam("keyword") String keyword, @PathParam("limit") int limit) {
		HTTPManager man = new HTTPManager();
		JSONArrayResult result = new JSONArrayResult();
		
		JSONArray dataList = man.getHTTPAppLogSearchResultByKeyword(keyword, limit);
		
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

	
}
