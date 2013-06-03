package com.l2bq.rest.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableRow;
import com.l2bq.rest.manager.BigQueryManager;

public class BigqueryUtil {

	public BigqueryUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Extract Result from QueryResponse
	 * @param query
	 * @return
	 */
	public static JSONArray extractResult(BigQueryManager manager, String query) {
		if ( manager == null || query == null )
			return null;
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			if (queryResponse != null && queryResponse.getRows() != null) {
				JSONArray dataList = new JSONArray();
				Map<Integer, String> schemaMap = getSchemaMap(queryResponse);
				
				if ( schemaMap == null )
					return null;
				
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
				
				return dataList;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static Map<Integer, String> getSchemaMap(QueryResponse queryResponse) {
		if ( queryResponse == null )
			return null;
		
		Map<Integer, String> schemaMap = new HashMap<Integer, String>();
		
		for ( int fieldIdx = 0; fieldIdx < queryResponse.getSchema().getFields().size(); fieldIdx++ ) {
			schemaMap.put(fieldIdx, queryResponse.getSchema().getFields().get(fieldIdx).getName());
		}
		
		return schemaMap;
	}

}
