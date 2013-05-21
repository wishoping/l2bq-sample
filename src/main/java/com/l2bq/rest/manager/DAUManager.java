package com.l2bq.rest.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableRow;
import com.l2bq.rest.entity.DailyData;
import com.l2bq.rest.entity.DailyUserData;
import com.l2bq.rest.entity.HourlyData;
import com.l2bq.rest.entity.HourlyUserData;

/**
 * DAU 관련 데이터 Query Manager Class
 * @author Junki Kim (wishoping@gmail.com)
 * @date 2013. 5. 21.
 *
 */
public class DAUManager {

	private BigQueryManager manager;
	
	private final String SELECT_QUERY = "SELECT %s FROM [l2bq_test.applog_login]";
	
	private final String GROUP_BY = "GRUOP BY f_time";
	private final String GROUP_BY_USER = "GROUP BY userId, f_time";
	
	private final String ORDER_BY = "ORDER BY f_time";
	private final String ORDER_BY_USER = "ORDER BY userId, f_time";
	
	private final String FIELD_DAILY = "STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as f_time, count(*) as loginCount";
	private final String FIELD_HOURLY = "STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d %H\") as f_time, count(*) as loginCount";
	private final String FIELD_DAILY_USER = "userId, STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as f_time, count(userId) as loginCount";
	private final String FIELD_HOURLY_USER = "userId, STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d %H\") as f_time, count(userId) as loginCount";
	
	private final String WHERE_USER = "WHERE userId = '%s'";
	
	private final List<String> FIELDS = Arrays.asList("f_time", "loginCount");
	private final List<String> FIELDS_USER = Arrays.asList("userId", "f_time", "loginCount");
	
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
	private DailyData extractDailyData(TableRow row ) {
		if ( row == null )
			return null;
		
		DailyData data = new DailyData();
		int count = 0;
		for ( String name : FIELDS ) {
			if ( name.equals("f_time") ) {
				data.setDay( row.get(name).toString() );
				count++;
			} else if ( name.equals("loginCount")) {
				data.setLoginCount( (Integer)row.get(name) );
				count++;
			}
		}
		
		return count == 2 ? data : null;
	}
	
	/**
	 * Extract {@link DailyData} entity instance from {@link TableRow} instance
	 * @param row {@link TableRow} Instance from Bigquery Response data
	 * @return {@link DailyData} Instance
	 */
	private HourlyData extractHourlyData(TableRow row ) {
		if ( row == null )
			return null;
		
		HourlyData data = new HourlyData();
		int count = 0;
		for ( String name : FIELDS ) {
			if ( name.equals("f_time") ) {
				data.setHour( row.get(name).toString() );
				count++;
			} else if ( name.equals("loginCount")) {
				data.setLoginCount( (Integer)row.get(name) );
				count++;
			}
		}
		
		return count == 2 ? data : null;
	}
	
	/**
	 * Extract {@link DailyData} entity instance from {@link TableRow} instance
	 * @param row {@link TableRow} Instance from Bigquery Response data
	 * @return {@link DailyData} Instance
	 */
	private DailyUserData extractDailyUserData(TableRow row ) {
		if ( row == null )
			return null;
		
		DailyUserData data = new DailyUserData();
		int count = 0;
		for ( String name : FIELDS_USER ) {
			if ( name.equals("f_time") ) {
				data.setDay( row.get(name).toString() );
				count++;
			} else if ( name.equals("loginCount")) {
				data.setLoginCount( (Integer)row.get(name) );
				count++;
			} else if ( name.equals("userId") ) {
				data.setUserId( row.get(name).toString() );
				count++;
			}
		}
		
		return count == 3 ? data : null;
	}
	
	/**
	 * Extract {@link DailyData} entity instance from {@link TableRow} instance
	 * @param row {@link TableRow} Instance from Bigquery Response data
	 * @return {@link DailyData} Instance
	 */
	private HourlyUserData extractHourlyUserData(TableRow row ) {
		if ( row == null )
			return null;
		
		HourlyUserData data = new HourlyUserData();
		int count = 0;
		for ( String name : FIELDS_USER ) {
			if ( name.equals("f_time") ) {
				data.setHour( row.get(name).toString() );
				count++;
			} else if ( name.equals("loginCount")) {
				data.setLoginCount( (Integer)row.get(name) );
				count++;
			} else if ( name.equals("userId") ) {
				data.setUserId( row.get(name).toString() );
				count++;
			}
		}
		
		return count == 3 ? data : null;
	}
	
	public List<DailyData> getDailyTotalList(int month) {
		String query = String.format(SELECT_QUERY, FIELD_DAILY);
		query += " " + GROUP_BY;
		query += " " + ORDER_BY;
		
		System.out.print("QUERY : " + query);
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			
			if (queryResponse != null && queryResponse.getRows() != null) {
				System.out.print("Got Result from Bigquery!");
				List<DailyData> dataList = new ArrayList<DailyData>();
				for (TableRow row : queryResponse.getRows()) {
					DailyData data = extractDailyData(row);
					if ( data != null ) {
						dataList.add(data);
						System.out.print("--> " + data.toString());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public List<HourlyData> getHourlyTotalList(int month) {
		String query = String.format(SELECT_QUERY, FIELD_HOURLY);
		query += " " + GROUP_BY;
		query += " " + ORDER_BY;
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<HourlyData> dataList = new ArrayList<HourlyData>();
				for (TableRow row : queryResponse.getRows()) {
					
					HourlyData data = extractHourlyData(row);
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
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<DailyUserData> dataList = new ArrayList<DailyUserData>();
				for (TableRow row : queryResponse.getRows()) {
					
					DailyUserData data = extractDailyUserData(row);
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
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<HourlyUserData> dataList = new ArrayList<HourlyUserData>();
				for (TableRow row : queryResponse.getRows()) {
					
					HourlyUserData data = extractHourlyUserData(row);
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
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<DailyUserData> dataList = new ArrayList<DailyUserData>();
				for (TableRow row : queryResponse.getRows()) {
					
					DailyUserData data = extractDailyUserData(row);
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
		query += " " + GROUP_BY_USER;
		query += " " + String.format(WHERE_USER, userId);
		query += " " + ORDER_BY_USER;
		
		try {
			QueryResponse queryResponse = manager.syncQuery(query);
			
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<HourlyUserData> dataList = new ArrayList<HourlyUserData>();
				for (TableRow row : queryResponse.getRows()) {
					
					HourlyUserData data = extractHourlyUserData(row);
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
	
	

}
