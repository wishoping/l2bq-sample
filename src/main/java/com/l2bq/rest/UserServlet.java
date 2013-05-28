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
	@Produces({MediaType.APPLICATION_JSON})
	public String getQueryResultByJSONP() {

		BigQueryManager manager = new BigQueryManager();
		JSONArrayResult result = new JSONArrayResult();
		
		String query = "select count(*) as totalUsers from [l2bq_test.applog_signup]";
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			if (queryResponse != null && queryResponse.getRows() != null) {
				JSONArray dataList = new JSONArray();
				Map<Integer, String> schemaMap = getSchemaMap(queryResponse);
				
				for (TableRow row : queryResponse.getRows()) {
					JSONObject item = new JSONObject();
					
					int idx = 0;
					for ( TableRow.F field : row.getF() ) {
						String type = queryResponse.getSchema().getFields().get(idx).getType();
						
						if ( type.equalsIgnoreCase("integer") ) {
							item.put(schemaMap.get(idx++), Long.parseLong(field.getV()) );
						} else if ( type.equalsIgnoreCase("string") ) {
							item.put(schemaMap.get(idx++), field.getV() );
						}
					}					
					
					dataList.put(item);
				}
				
				if ( dataList.length() != 0 ) {
					result.setList(dataList);
					result.setMsg("success");
					result.setSuccess(true);
					
					return result.toString();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		result.setList(null);
		result.setMsg("failed with null result");
		result.setSuccess(false);
		
//		return result.toString();
		return result.toString();
	}

	private Map<Integer, String> getSchemaMap(QueryResponse queryResponse) {
		Map<Integer, String> schemaMap = new HashMap<Integer, String>();
		
		for ( int fieldIdx = 0; fieldIdx < queryResponse.getSchema().getFields().size(); fieldIdx++ ) {
			schemaMap.put(fieldIdx, queryResponse.getSchema().getFields().get(fieldIdx).getName());
		}
		
		return schemaMap;
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

