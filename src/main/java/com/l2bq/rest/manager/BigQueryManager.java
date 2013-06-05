package com.l2bq.rest.manager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.Bigquery.Jobs.Insert;
import com.google.api.services.bigquery.BigqueryScopes;
import com.google.api.services.bigquery.model.GetQueryResultsResponse;
import com.google.api.services.bigquery.model.Job;
import com.google.api.services.bigquery.model.JobConfiguration;
import com.google.api.services.bigquery.model.JobConfigurationLoad;
import com.google.api.services.bigquery.model.JobConfigurationQuery;
import com.google.api.services.bigquery.model.JobReference;
import com.google.api.services.bigquery.model.QueryRequest;
import com.google.api.services.bigquery.model.QueryResponse;

import com.google.api.services.bigquery.model.TableRow;

/**
 * BigQuery Manager class 
 * @author Junki Kim (wishoping@gmail.com), Wooseok Seo (wooseok.seo@gmail.com)
 * @date 2013. 5. 21.
 *
 */
public class BigQueryManager {
	
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	private static final List<String> SCOPES = Arrays.asList(BigqueryScopes.BIGQUERY);
	private static final AppIdentityCredential credential = new AppIdentityCredential(SCOPES);
	
	private Bigquery bigqueryService = null;
	
	private String projectId = "l2bq-sample";
	private String applicationName = "l2bq Query";
	
	public BigQueryManager() {
		// Initialize Bigquery Client Instance
		this.bigqueryService = new Bigquery.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
								.setApplicationName(getApplicationName())
								.setHttpRequestInitializer(credential)
								.build();
	}
	
	/**
	 * Runs a synchronous BigQuery query and displays the result.
	 * 
	 * @param service An authorized BigQuery client.
	 * @param projectNumber The current Project number.
	 * @param query A String containing a BigQuery SQL statement.
	 * @throws IOException
	 */
	public QueryResponse syncQuery(String query) throws IOException {
		System.out.format("\nInserting Query Job: %s\n", query);
		
		QueryRequest queryInfo = new QueryRequest().setQuery(query);

		Bigquery.Jobs.Query queryRequest = this.bigqueryService.jobs()
																.query(this.getProjectId(), queryInfo);
		QueryResponse queryResponse = queryRequest.execute();
		
		
		// retrieve example
//		if (queryResponse.getRows() != null) {
//			for (TableRow row : queryResponse.getRows()) {
//				for (TableRow.F field : row.getF()) {
//					System.out.printf("%s-30s", field.getV());
//				}
//				System.out.println();
//			}
//		}
		
		return queryResponse;
	}
	
	/**
	   * Creates a Query Job for a particular query on a dataset
	   *
	   * @param bigquery  an authorized BigQuery client
	   * @param projectId a String containing the current Project ID
	   * @param querySql  the actual query string
	   * @return a reference to the inserted query Job
	   * @throws IOException
	   */
	public JobReference asynchronousQuery(String querySql) throws IOException {
		System.out.format("\nInserting Query Job: %s\n", querySql);

		Job job = new Job();
		JobConfiguration config = new JobConfiguration();
		JobConfigurationQuery queryConfig = new JobConfigurationQuery();
		config.setQuery(queryConfig);

		job.setConfiguration(config);
		queryConfig.setQuery(querySql);

		Insert insert = this.bigqueryService.jobs().insert(getProjectId(), job);
		insert.setProjectId(getProjectId());
		JobReference jobId = insert.execute().getJobReference();

		System.out.format("\nJob ID of Query Job is: %s\n", jobId.getJobId());

		return jobId;
	}

	  /**
	   * Polls the status of a BigQuery job, returns Job reference if "Done"
	   *
	   * @param bigquery  an authorized BigQuery client
	   * @param projectId a string containing the current project ID
	   * @param jobId     a reference to an inserted query Job
	   * @return a reference to the completed Job
	   * @throws IOException
	   * @throws InterruptedException
	   */
	private Job checkQueryResults(JobReference jobId) throws IOException, InterruptedException {
		// Variables to keep track of total query time
		long startTime = System.currentTimeMillis();
		long elapsedTime;

		while (true) {
			Job pollJob = this.bigqueryService.jobs()
												.get(getProjectId(), jobId.getJobId())
												.execute();
			elapsedTime = System.currentTimeMillis() - startTime;
			
			System.out.format("Job status (%dms) %s: %s\n", elapsedTime,
					jobId.getJobId(), pollJob.getStatus().getState());
			
			if (pollJob.getStatus().getState().equals("DONE")) {
				return pollJob;
			}
			
			// Pause execution for one second before polling job status again,
			// to
			// reduce unnecessary calls to the BigQUery API and lower overall
			// application bandwidth.
			Thread.sleep(1000);
		}
	}

	  /**
	   * Makes an API call to the BigQuery API
	   *
	   * @param bigquery     an authorized BigQuery client
	   * @param projectId    a string containing the current project ID
	   * @param completedJob to the completed Job
	   * @throws IOException
	   */
	  private void displayQueryResults(Job completedJob) throws IOException {
		GetQueryResultsResponse queryResult = this.bigqueryService
				.jobs()
				.getQueryResults(getProjectId(), completedJob.getJobReference().getJobId())
				.execute();
		List<TableRow> rows = queryResult.getRows();
		System.out.print("\nQuery Results:\n------------\n");
		for (TableRow row : rows) {
			for (TableRow.F field : row.getF()) {
				System.out.printf("%-20s", field.getV());
			}
			System.out.println();
		}
	  }

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
}
