package com.junseo.l2bq.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.junseo.l2bq.rest.entity.DailyData;
import com.junseo.l2bq.rest.entity.DailyUserData;
import com.junseo.l2bq.rest.entity.HourlyData;
import com.junseo.l2bq.rest.entity.HourlyUserData;
import com.junseo.l2bq.rest.entity.ListResult;
import com.junseo.l2bq.rest.entity.Result;

/**
 * Daily Statistics Related RESTful Servlet Class 
 * @author Junki Kim(wishoping@gmail.com)
 * @date 2013. 5. 20.
 */
@Path("/dau")
public class DAUService
{

	/**
	 * Daily Total User Statistics
	 * @param month Target Month 
	 * @return 
	 */
	@GET
	@Path("/daily/total/{month}")
	@Produces({MediaType.APPLICATION_JSON})
	public ListResult getDailyTotalResult(@PathParam("month") int month)
	{
		List<DailyData> list = new ArrayList<DailyData>();
		
		// SELECT STRFTIME_UTC_USEC(time*1000, "%Y-%m-%d") as day, count(*) as loginCount FROM [l2bq_test.applog_login] group by day order by day;
		
		return null;
	}

	/**
	 * Hourly Total User Statistics 
	 * @param month Target Month 
	 * @return
	 */
	@GET
	@Path("/hourly/total/{month}")
	@Produces({MediaType.APPLICATION_JSON})
	public ListResult getDailyHourlyTotalResult(@PathParam("month") int month)
	{
		List<HourlyData> list = new ArrayList<HourlyData>();
		
		// SELECT STRFTIME_UTC_USEC(time*1000, "%Y-%m-%d %H") as day, count(*) as loginCount FROM [l2bq_test.applog_login] group by day order by day;

		return null;
	}
	
	/**
	 * Daily Total Each Users' Statistics
	 * @param month Target Month 
	 * @return
	 */
	@GET
	@Path("/daily/eachuser/total/{month}")
	@Produces({MediaType.APPLICATION_JSON})
	public ListResult getDailyEachUserTotalResult(@PathParam("month") int month)
	{
		List<DailyUserData> list = new ArrayList<DailyUserData>();
		
		// SELECT userId, STRFTIME_UTC_USEC(time*1000, "%Y-%m-%d") as day, count(userId) as loginCount FROM [l2bq_test.applog_login] group by userId, day order by userId, day;
		
		return null;
	}
	
	/**
	 * Hourly Total Each Users' Statistics
	 * @param month Target Month 
	 * @return
	 */
	@GET
	@Path("/hourly/eachuser/total/{month}")
	@Produces({MediaType.APPLICATION_JSON})
	public ListResult getDailyHourlyEachUserTotalResult(@PathParam("month") int month)
	{
		List<HourlyUserData> list = new ArrayList<HourlyUserData>();
		
		// SELECT userId, STRFTIME_UTC_USEC(time*1000, "%Y-%m-%d %H") as day, count(userId) as loginCount FROM [l2bq_test.applog_login] group by userId, day order by userId, day;
		
		return null;
	}
	
	/**
	 * Daily Total Specific User's Statistics
	 * @param userId Target User ID
	 * @param month Target Month
	 * @return
	 */
	@GET
	@Path("/daily/user/{userId}/{month}")
	@Produces({MediaType.APPLICATION_JSON})
	public ListResult getDailyUserTotalResult(@PathParam("userId") int userId, @PathParam("month") int month)
	{
		List<DailyUserData> list = new ArrayList<DailyUserData>();
		return null;
	}
	
	/**
	 * Hourly Total Specific User's Statistics
	 * @param userId Target User ID
	 * @param month Target Month
	 * @return
	 */
	@GET
	@Path("/hourly/user/{userId}/{month}")
	@Produces({MediaType.APPLICATION_JSON})
	public ListResult getDailyHourlyUserTotalResult(@PathParam("userId") int userId, @PathParam("month") int month)
	{
		List<HourlyUserData> list = new ArrayList<HourlyUserData>();
		return null;
	}
}
