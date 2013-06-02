package com.l2bq.rest.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableRow;
import com.l2bq.rest.entity.DailyData;
import com.l2bq.rest.entity.DailyUserData;
import com.l2bq.rest.entity.HourlyData;
import com.l2bq.rest.entity.HourlyUserData;
import com.sun.jersey.api.json.JSONWithPadding;

/**
 * DAU 관련 데이터 Query Manager Class
 * @author Junki Kim (wishoping@gmail.com), Wooseok Seo (wooseok.seo@gmail.com)
 * @date 2013. 5. 21.
 *
 */
public class DAUManager {

	private BigQueryManager manager;
	
	private final String SELECT_QUERY = "SELECT %s FROM [l2bq_test.applog_login]";
	
	private final String GROUP_BY = "GROUP BY f_time";
	private final String GROUP_BY_USER = "GROUP BY userId, f_time";
	
	private final String ORDER_BY = "ORDER BY f_time";
	private final String ORDER_BY_USER = "ORDER BY userId, f_time";
	
	private final String FIELD_DAILY = "STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as f_time, count(*) as loginCount";
	private final String FIELD_HOURLY = "STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d %H\") as f_time, count(*) as loginCount";
	private final String FIELD_DAILY_USER = "userId, STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as f_time, count(userId) as loginCount";
	private final String FIELD_HOURLY_USER = "userId, STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d %H\") as f_time, count(userId) as loginCount";
	
	private final String WHERE_USER = "WHERE userId = '%s'";
	
	/**
	 * Constructor
	 */
	public DAUManager() {
		// This is a test manager class. So we use default configuration for this test project
		this.manager = new BigQueryManager();
	}
	
	/**
	 * Extract {@link DailyData} entity instance from {@link TableRow} instance
	 * @param row {@link TableRow} Instance from Bigquery Response data
	 * @return {@link DailyData} Instance
	 */
	private DailyData extractDailyData(Map<Integer, String> schemaMap, TableRow row ) {
		if ( row == null )
			return null;
		
		DailyData data = new DailyData();
		
		int idx = 0;
		for ( TableRow.F field : row.getF() ) {
			String name = schemaMap.get(idx++);
			if ( name.equals("f_time") ) {
				data.setDay( field.getV() );
				System.out.print("Day : " + field.getV() );
			} else if (name.equals("loginCount") ) {
				data.setLoginCount( Integer.parseInt(field.getV()) );
				System.out.print("LoginCount : " + field.getV() );
			}
		}

		return idx == 2 ? data : null;
	}
	
	/**
	 * Extract {@link DailyData} entity instance from {@link TableRow} instance
	 * @param row {@link TableRow} Instance from Bigquery Response data
	 * @return {@link DailyData} Instance
	 */
	private HourlyData extractHourlyData(Map<Integer, String> schemaMap, TableRow row ) {
		if ( row == null )
			return null;
		
		HourlyData data = new HourlyData();
		int idx = 0;
		for ( TableRow.F field : row.getF() ) {
			String name = schemaMap.get(idx++);
			if ( name.equals("f_time") ) {
				data.setHour( field.getV() );
			} else if (name.equals("loginCount") ) {
				data.setLoginCount( Integer.parseInt(field.getV()) );
			}
		}

		return idx == 2 ? data : null;
	}
	
	/**
	 * Extract {@link DailyData} entity instance from {@link TableRow} instance
	 * @param row {@link TableRow} Instance from Bigquery Response data
	 * @return {@link DailyData} Instance
	 */
	private DailyUserData extractDailyUserData(Map<Integer, String> schemaMap, TableRow row ) {
		if ( row == null )
			return null;
		
		DailyUserData data = new DailyUserData();
		int idx = 0;
		for ( TableRow.F field : row.getF() ) {
			String name = schemaMap.get(idx++);
			if ( name.equals("userId") ) {
				data.setUserId( field.getV() );
			} else if ( name.equals("f_time") ) {
				data.setDay( field.getV() );
			} else if (name.equals("loginCount") ) {
				data.setLoginCount( Integer.parseInt(field.getV()) );
			}
		}
		
		return idx == 3 ? data : null;
	}
	
	/**
	 * Extract {@link DailyData} entity instance from {@link TableRow} instance
	 * @param row {@link TableRow} Instance from Bigquery Response data
	 * @return {@link DailyData} Instance
	 */
	private HourlyUserData extractHourlyUserData(Map<Integer, String> schemaMap, TableRow row ) {
		if ( row == null )
			return null;
		
		HourlyUserData data = new HourlyUserData();
		int idx = 0;
		for ( TableRow.F field : row.getF() ) {
			String name = schemaMap.get(idx++);
			if ( name.equals("userId") ) {
				data.setUserId( field.getV() );
			} else if ( name.equals("f_time") ) {
				data.setHour( field.getV() );
			} else if (name.equals("loginCount") ) {
				data.setLoginCount( Integer.parseInt(field.getV()) );
			}
		}
		
		return idx == 3 ? data : null;
	}
	
	private Map<Integer, String> getSchemaMap(QueryResponse queryResponse) {
		Map<Integer, String> schemaMap = new HashMap<Integer, String>();
		
		for ( int fieldIdx = 0; fieldIdx < queryResponse.getSchema().getFields().size(); fieldIdx++ ) {
			schemaMap.put(fieldIdx, queryResponse.getSchema().getFields().get(fieldIdx).getName());
		}
		
		return schemaMap;
	}
	
	public List<DailyData> getDailyTotalList(int month) {
		String query = String.format(SELECT_QUERY, FIELD_DAILY);
		query += " " + GROUP_BY;
		query += " " + ORDER_BY;
		query += ";";
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<DailyData> dataList = new ArrayList<DailyData>();
				Map<Integer, String> schemaMap = getSchemaMap(queryResponse);
				
				for (TableRow row : queryResponse.getRows()) {
					DailyData data = extractDailyData(schemaMap, row);
					if ( data != null ) {
						dataList.add(data);
					}
				}
				
				return dataList;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return null;
		
	}
	
	public List<HourlyData> getHourlyTotalList(int month) {
		String query = String.format(SELECT_QUERY, FIELD_HOURLY);
		query += " " + GROUP_BY;
		query += " " + ORDER_BY;
		query += ";";
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<HourlyData> dataList = new ArrayList<HourlyData>();
				Map<Integer, String> schemaMap = getSchemaMap(queryResponse);
				for (TableRow row : queryResponse.getRows()) {
					
					HourlyData data = extractHourlyData(schemaMap, row);
					if ( data != null )
						dataList.add(data);
				}
				
				return dataList;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<DailyUserData> getDailyUserTotalList(int month) {
		String query = String.format(SELECT_QUERY, FIELD_DAILY_USER);
		query += " " + GROUP_BY_USER;
		query += " " + ORDER_BY_USER;
		query += ";";
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<DailyUserData> dataList = new ArrayList<DailyUserData>();
				Map<Integer, String> schemaMap = getSchemaMap(queryResponse);
				for (TableRow row : queryResponse.getRows()) {
					
					DailyUserData data = extractDailyUserData(schemaMap, row);
					if ( data != null )
						dataList.add(data);
				}
				return dataList;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<HourlyUserData> getHourlyUserTotalList(int month) {
		String query = String.format(SELECT_QUERY, FIELD_HOURLY_USER);
		query += " " + GROUP_BY_USER;
		query += " " + ORDER_BY_USER;
		query += ";";
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<HourlyUserData> dataList = new ArrayList<HourlyUserData>();
				Map<Integer, String> schemaMap = getSchemaMap(queryResponse);
				for (TableRow row : queryResponse.getRows()) {
					
					HourlyUserData data = extractHourlyUserData(schemaMap, row);
					if ( data != null )
						dataList.add(data);
				}
				return dataList;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<DailyUserData> getDailySpecificUserTotalList(String userId, int month) {
		String query = String.format(SELECT_QUERY, FIELD_DAILY_USER);
		query += String.format(WHERE_USER, userId);
		query += " " + GROUP_BY_USER;
		query += " " + ORDER_BY_USER;
		query += ";";
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<DailyUserData> dataList = new ArrayList<DailyUserData>();
				Map<Integer, String> schemaMap = getSchemaMap(queryResponse);
				for (TableRow row : queryResponse.getRows()) {
					
					DailyUserData data = extractDailyUserData(schemaMap, row);
					if ( data != null )
						dataList.add(data);
				}
				return dataList;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<HourlyUserData> getHourlySpecificUserTotalList(String userId, int month) {
		String query = String.format(SELECT_QUERY, FIELD_HOURLY_USER);
		query += " " + String.format(WHERE_USER, userId);
		query += " " + GROUP_BY_USER;
		query += " " + ORDER_BY_USER;
		query += ";";
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<HourlyUserData> dataList = new ArrayList<HourlyUserData>();
				Map<Integer, String> schemaMap = getSchemaMap(queryResponse);
				for (TableRow row : queryResponse.getRows()) {
					
					HourlyUserData data = extractHourlyUserData(schemaMap, row);
					if ( data != null )
						dataList.add(data);
				}
				return dataList;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONArray getTotalUserCountInfo()
	{
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
				
				return dataList;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
