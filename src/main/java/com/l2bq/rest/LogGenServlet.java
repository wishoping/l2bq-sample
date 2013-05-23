package com.l2bq.rest;

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
import com.l2bq.rest.entity.DAULog;
import com.l2bq.rest.entity.LoginData;
import com.l2bq.rest.entity.SignupData;

/**
 * Log Generator via log4j class 
 * @author Junki Kim (wishoping@gmail.com), Wooseok Seo (wooseok.seo@gmail.com)
 * @date 2013. 5. 22.
 *
 */
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
		
		List<Integer> idList = new ArrayList<Integer>();
		
		for ( int i = 0; i < 1000; i++ )
		{
			idx = rand.nextInt(100)+1;
			idList.add(idx);
			int minusVal = rand.nextBoolean() ? -1 : 1;
			SignupData data = new SignupData();
			data.setTime(System.currentTimeMillis() + (rand.nextInt(60) * rand.nextInt(60) * rand.nextInt(24) * rand.nextInt(2) * 1000 * minusVal) );
			data.setUserId(idx);
			data.setUserName(idx+"");
			data.setLangType(rand.nextInt(2));
			data.setOsType(rand.nextInt(2));
			data.setUserType(rand.nextInt(10)+"");
			data.setUtfOffset( rand.nextInt(24) );
			data.setPhone( data.getPhone() + String.format("%04d", rand.nextInt(10000)) ) ; 
			
			DAULog log = new DAULog();
			
			log.setType("signup");
			log.setData(data);
			
			APP_LOG( gson.toJson(log) );
			
			logs.add(log);
			
		}
		
		for ( int i = 0; i < 3000; i++ )
		{
//			idx = rand.nextInt(1000)+1;
			idx = idList.get( rand.nextInt(idList.size()) );
			LoginData data = new LoginData();
			data.setTime(System.currentTimeMillis() + (rand.nextInt(60) * rand.nextInt(60) * rand.nextInt(24) * rand.nextInt(2) * 1000) + (rand.nextInt(60) * rand.nextInt(60) * rand.nextInt(24) * rand.nextInt(31) * 1000 ) );
			data.setUserId(idx);
			data.setUserName(idx+"");
			data.setLangType(rand.nextInt(2));
			data.setOsType(rand.nextInt(2));
			data.setClientVer(String.format("%d.%d.%d", rand.nextInt(2), rand.nextInt(10), rand.nextInt(20)));
			
			DAULog log = new DAULog();
			
			log.setType("login");
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
