package com.junseo.l2bq.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.junseo.l2bq.rest.entity.LoginData;
import com.junseo.l2bq.rest.entity.DAULog;

@Path("/log")
public class LogGenServlet
{
	private static final String APPLOG_TAG = "AppLog";
	public static final Logger LOG = Logger.getLogger(APPLOG_TAG);
	
	public LogGenServlet()
	{
		// TODO Auto-generated constructor stub
	}
	
	@GET
	@Path("/gen")
//	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Object genDAULogData(@Context UriInfo info)
	{
		JSONObject obj = null;
		
		generateDAULog();
		
		return obj;
	}

	
	private List<DAULog> generateDAULog()
	{
		List<DAULog> logs = new ArrayList<DAULog>();
	
		int idx = 1;
		
		Gson gson = new Gson();
		
		Random rand = new Random();
		
		for ( int i = 0; i < Integer.MAX_VALUE; i++ )
		{
			idx = rand.nextInt(100);
			int minusVal = rand.nextBoolean() ? -1 : 1;
			LoginData data = new LoginData();
			data.setTime(System.currentTimeMillis() + (rand.nextInt(60) * rand.nextInt(60) * rand.nextInt(12) * 1000 * minusVal) );
			data.setUserId(idx);
			data.setUserName(idx+"");
			data.setLangType(rand.nextInt(2));
			data.setOsType(rand.nextInt(2));
			data.setClientVer(String.format("%d.%d.%d", rand.nextInt(2), rand.nextInt(10), rand.nextInt(20)));
			
			DAULog log = new DAULog();
			
			log.setType("dau");
			log.setData(data);
			
			APP_LOG( gson.toJson(log) );
			
			logs.add(log);
			
		}
		
		return logs;
	}
	
	/**
	 * Application Log write via Log4j 
	 * @param msg Log Message for Application
	 */
	public void APP_LOG(String msg)
	{
		LOG.info(msg);
	}
}
