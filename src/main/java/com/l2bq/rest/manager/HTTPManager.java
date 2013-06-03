package com.l2bq.rest.manager;

import org.json.JSONArray;

import com.l2bq.rest.util.BigqueryUtil;

public class HTTPManager {

	private BigQueryManager manager;
	
	private final String CRITICAL_LEVEL = "Critical";
	private final String WARNING_LEVEL = "Warning";
	private final String INFO_LEVEL = "Info";
	
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
		String query = String.format("SELECT COUNT(*) as totalHttpLogs FROM [%s.http_access_log]", this.databaseName);
		
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

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

}
