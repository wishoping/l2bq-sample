package com.l2bq.rest.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Hourly Total Statistics Result Entity Class 
 *  - 일 단위 통계 결과 저장 클래스
 *  - RESTful Service 의 Response 에 활용
 * @author Junki Kim(wishoping@gmail.com)
 * @date 2013. 5. 20.
 */
@XmlRootElement
public class HourlyData extends Entity
{
	private String hour;
	
	private int loginCount;

	public String getHour()
	{
		return hour;
	}

	public void setHour(String hour)
	{
		this.hour = hour;
	}

	public int getLoginCount()
	{
		return loginCount;
	}

	public void setLoginCount(int loginCount)
	{
		this.loginCount = loginCount;
	}
	
	
	

}
