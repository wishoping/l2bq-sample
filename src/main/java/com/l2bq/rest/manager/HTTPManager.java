package com.l2bq.rest.manager;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.l2bq.rest.util.BigqueryUtil;

public class HTTPManager {

	private BigQueryManager manager;
	
	private final String CRITICAL_LEVEL = "Critical";
	private final String WARNING_LEVEL = "Warning";
	private final String INFO_LEVEL = "Info";
	private final String DEBUG_LEVEL = "Debug";
	
	/**
	 * Database Name 
	 */
	private String databaseName = "l2bq_test";
	
	public HTTPManager() {
		// This is a test manager class. So we use default configuration for this test project
		this.manager = new BigQueryManager();
	}
	
	/**
	 * Get Total HTTP Logs Counts
	 * @return
	 */
	public JSONArray getTotalHttpLogCount()
	{
		String query = String.format("SELECT COUNT(*) as totalHttpLogs FROM [%s.http_access_log]", this.databaseName);
		
		return BigqueryUtil.extractResult(this.manager, query);
	}
	
	/**
	 * Get Total HTTP Level Statistics information 
	 * @return
	 */
	public JSONArray getHttpLogsStats()
	{
		String query = String.format("select level, count(*) as levelCount from [%s.http_access_log] group by level", this.databaseName);
		
		return BigqueryUtil.extractResult(this.manager, query);
	}
	
	/**
	 * Get HTTP Log by Timestamp
	 * @param index
	 * @return
	 */
	public JSONArray getDetailedHttpLogs(long index)
	{
		String query = String.format("select * from [%s.http_access_log] where timestamp = %d", this.databaseName, index);
		
		return BigqueryUtil.extractResult(this.manager, query);
	}
	
	public JSONArray getHTTPAppLogSearchResultByKeyword(String option, String keyword, int limit) {
		if ( keyword == null || keyword.isEmpty() || 
			 option == null || option.isEmpty() )
			return null;
		if ( limit <= 0 )
			return null;
		
		StringBuilder condition = new StringBuilder();
		if ( keyword.contains(" ") ) {
			boolean isFirst = true;
			for ( String k : keyword.split(" ")) {
				if ( !isFirst ) {
					condition.append("AND ");
				} else {
					isFirst = false;
				}
				condition.append(String.format( "REGEXP_MATCH(%s, r'.*%s.*')", option, k));
			}
		} else {
			condition.append(String.format( "REGEXP_MATCH(%s, r'.*%s.*')", option, keyword));
		}
		
		String query = String.format("select * from [%s.http_access_log] WHERE %s limit %d", this.databaseName, condition.toString(), limit);
		
		return BigqueryUtil.extractResult(this.manager, query);
	}
	
	/**
	 * Get HTTP Log Information 
	 * @return
	 */
	public JSONArray getHttpsLogsByLevel(String level, int limit)
	{
		if ( level == null || limit <= 0 )
			return null;
		
		String query = String.format("SELECT method, timestamp, resource, ip, versionId from [%s.http_access_log] where level = '%s' order by timestamp desc limit %d", this.databaseName, level, limit);
		
		return BigqueryUtil.extractResult(this.manager, query);
	}
	
	/**
	 * Get Critical Level HTTP Logs
	 * @param limit
	 * @return
	 */
	public JSONArray getHttpCriticalLogs(int limit)
	{
		return getHttpsLogsByLevel(CRITICAL_LEVEL, limit);
	}
	
	/**
	 * Get Warning Level HTTP Logs
	 * @param limit
	 * @return
	 */
	public JSONArray getHttpWarningLogs(int limit)
	{
		return getHttpsLogsByLevel(WARNING_LEVEL, limit);
	}
	
	/**
	 * Get Info Level HTTP Logs
	 * @param limit
	 * @return
	 */
	public JSONArray getHttpInfoLogs(int limit)
	{
		return getHttpsLogsByLevel(INFO_LEVEL, limit);
	}
	
	/**
	 * Get Debug Level HTTP Logs
	 * @param limit
	 * @return
	 */
	public JSONArray getHttpDebugLogs(int limit)
	{
		return getHttpsLogsByLevel(DEBUG_LEVEL, limit);
	}
	
	/**
	 * 검색 조건을 위한 HTTP Status 의 종류 Return
	 * @return
	 */
	public JSONArray getHttpStatusFacet() 
	{
		String query = String.format("select httpStatus from [%s.http_access_log] group by httpStatus order by httpStatus", this.databaseName);
		
		return BigqueryUtil.extractResult(this.manager, query);
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

}
