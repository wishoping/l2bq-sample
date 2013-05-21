package com.l2bq.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.l2bq.rest.entity.DailyData;
import com.l2bq.rest.entity.DailyUserData;
import com.l2bq.rest.entity.HourlyData;
import com.l2bq.rest.entity.HourlyUserData;
import com.l2bq.rest.entity.ListResult;
import com.l2bq.rest.entity.Result;
import com.l2bq.rest.manager.DAUManager;

/**
 * Daily Statistics Related RESTful Servlet Class 
 * @author Junki Kim(wishoping@gmail.com)
 * @date 2013. 5. 20.
 */
@Path("/dau")
public class DAUService
{
	private DAUManager dauMan = new DAUManager();
	
	/**
	 * Daily Total User Statistics
	 * @param month Target Month 
	 * @return 
	 */
	@GET
	@Path("/daily/total/{month}")
	@Produces({MediaType.APPLICATION_JSON})
	public ListResult<DailyData> getDailyTotalResult(@PathParam("month") int month)
	{
		List<DailyData> list = dauMan.getDailyTotalList(month);
		
		ListResult<DailyData> result = new ListResult<DailyData>();
		if ( list != null ) {
			result.setList(list);
			result.setMsg("success");
			result.setSuccess(true);
		} else {
			result.setList(null);
			result.setMsg("failed with null result");
			result.setSuccess(false);
		}
		
		return result;
	}

	/**
	 * Hourly Total User Statistics 
	 * @param month Target Month 
	 * @return
	 */
	@GET
	@Path("/hourly/total/{month}")
	@Produces({MediaType.APPLICATION_JSON})
	public ListResult<HourlyData> getDailyHourlyTotalResult(@PathParam("month") int month)
	{
		List<HourlyData> list = dauMan.getHourlyTotalList(month);
		
		ListResult<HourlyData> result = new ListResult<HourlyData>();
		if ( list != null ) {
			result.setList(list);
			result.setMsg("success");
			result.setSuccess(true);
		} else {
			result.setList(null);
			result.setMsg("failed with null result");
			result.setSuccess(false);
		}
		
		return result;
	}
	
	/**
	 * Daily Total Each Users' Statistics
	 * @param month Target Month 
	 * @return
	 */
	@GET
	@Path("/daily/eachuser/total/{month}")
	@Produces({MediaType.APPLICATION_JSON})
	public ListResult<DailyUserData> getDailyEachUserTotalResult(@PathParam("month") int month)
	{
		List<DailyUserData> list = dauMan.getDailyUserTotalList(month);
		
		ListResult<DailyUserData> result = new ListResult<DailyUserData>();
		if ( list != null ) {
			result.setList(list);
			result.setMsg("success");
			result.setSuccess(true);
		} else {
			result.setList(null);
			result.setMsg("failed with null result");
			result.setSuccess(false);
		}
		
		return result;
	}
	
	/**
	 * Hourly Total Each Users' Statistics
	 * @param month Target Month 
	 * @return
	 */
	@GET
	@Path("/hourly/eachuser/total/{month}")
	@Produces({MediaType.APPLICATION_JSON})
	public ListResult<HourlyUserData> getDailyHourlyEachUserTotalResult(@PathParam("month") int month)
	{
		List<HourlyUserData> list = dauMan.getHourlyUserTotalList(month);
		
		ListResult<HourlyUserData> result = new ListResult<HourlyUserData>();
		if ( list != null ) {
			result.setList(list);
			result.setMsg("success");
			result.setSuccess(true);
		} else {
			result.setList(null);
			result.setMsg("failed with null result");
			result.setSuccess(false);
		}
		
		return result;
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
	public ListResult<DailyUserData> getDailyUserTotalResult(@PathParam("userId") String userId, @PathParam("month") int month)
	{
		List<DailyUserData> list = dauMan.getDailySpecificUserTotalList(userId, month);
		
		ListResult<DailyUserData> result = new ListResult<DailyUserData>();
		if ( list != null ) {
			result.setList(list);
			result.setMsg("success");
			result.setSuccess(true);
		} else {
			result.setList(null);
			result.setMsg("failed with null result");
			result.setSuccess(false);
		}
		
		return result;
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
	public ListResult<HourlyUserData> getDailyHourlyUserTotalResult(@PathParam("userId") String userId, @PathParam("month") int month)
	{
		List<HourlyUserData> list = dauMan.getHourlySpecificUserTotalList(userId, month);
		
		ListResult<HourlyUserData> result = new ListResult<HourlyUserData>();
		if ( list != null ) {
			result.setList(list);
			result.setMsg("success");
			result.setSuccess(true);
		} else {
			result.setList(null);
			result.setMsg("failed with null result");
			result.setSuccess(false);
		}
		
		return result;
	}
}
