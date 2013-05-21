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

import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableRow;
import com.l2bq.rest.entity.CustomQuery;
import com.l2bq.rest.entity.HourlyData;
import com.l2bq.rest.entity.HourlyUserData;
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
	public ListResult<JSONObject> getQueryResult(CustomQuery query) {

		BigQueryManager manager = new BigQueryManager();
		ListResult<JSONObject> result = new ListResult<JSONObject>();
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query.getQuery());
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<JSONObject> dataList = new ArrayList<JSONObject>();
				Map<Integer, String> schemaMap = getSchemaMap(queryResponse);
				
				for (TableRow row : queryResponse.getRows()) {
					JSONObject item = new JSONObject();
					
					int idx = 0;
					for ( TableRow.F field : row.getF() ) {
						item.append(schemaMap.get(idx++), field.getV() );
					}					
					
					dataList.add(item);
				}
				
				if ( dataList.size() != 0 ) {
					result.setList(dataList);
					result.setMsg("success");
					result.setSuccess(true);
					
					return result;
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
		
		return result;
	}

	private Map<Integer, String> getSchemaMap(QueryResponse queryResponse) {
		Map<Integer, String> schemaMap = new HashMap<Integer, String>();
		
		for ( int fieldIdx = 0; fieldIdx < queryResponse.getSchema().getFields().size(); fieldIdx++ ) {
			schemaMap.put(fieldIdx, queryResponse.getSchema().getFields().get(fieldIdx).getName());
		}
		
		return schemaMap;
	}

	
}
