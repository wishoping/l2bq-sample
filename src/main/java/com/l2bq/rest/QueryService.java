package com.l2bq.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableRow;
import com.google.gson.Gson;
import com.l2bq.rest.entity.CustomQuery;
import com.l2bq.rest.entity.HourlyData;
import com.l2bq.rest.entity.HourlyUserData;
import com.l2bq.rest.entity.JSONArrayResult;
import com.l2bq.rest.entity.ListResult;
import com.l2bq.rest.entity.QueryResult;
import com.l2bq.rest.manager.BigQueryManager;

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

	/*
	 * Anonymous query without paying attention to the kind of tables.
	 */
	@POST
	@Path("")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public String getQueryResult(CustomQuery query) {

		BigQueryManager manager = new BigQueryManager();
		JSONArrayResult result = new JSONArrayResult();
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query.getQuery());
			if (queryResponse != null && queryResponse.getRows() != null) {
				JSONArray dataList = new JSONArray();
				Map<Integer, String> schemaMap = getSchemaMap(queryResponse);
				
				for (TableRow row : queryResponse.getRows()) {
					JSONObject item = new JSONObject();
					
					int idx = 0;
					for ( TableRow.F field : row.getF() ) {
						item.put(schemaMap.get(idx++), field.getV() );
					}					
					
					dataList.put(item);
				}
				
				//String return = String.format("{\"msg\":\"success\",\"isSuccess\":true,\"list\":%s", new Gson().toJson(dataList));
				if ( dataList.length() != 0 ) {
					result.setList(dataList);
					result.setMsg("success");
//					result.setMsg(dataList.toString());
					result.setSuccess(true);
					
//					return new Gson().toJson(result);
					return result.toString();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result.setList(null);
		result.setMsg("failed with null result");
		result.setSuccess(false);
		
		return result.toString();
	}

	private Map<Integer, String> getSchemaMap(QueryResponse queryResponse) {
		Map<Integer, String> schemaMap = new HashMap<Integer, String>();
		
		for ( int fieldIdx = 0; fieldIdx < queryResponse.getSchema().getFields().size(); fieldIdx++ ) {
			schemaMap.put(fieldIdx, queryResponse.getSchema().getFields().get(fieldIdx).getName());
		}
		
		return schemaMap;
	}

	
}
