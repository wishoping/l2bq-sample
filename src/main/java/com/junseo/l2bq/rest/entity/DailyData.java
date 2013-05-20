package com.junseo.l2bq.rest.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Daily Total Statistics Result Entity Class 
 *  - 일 단위 통계 결과 저장 클래스
 *  - RESTful Service 의 Response 에 활용
 * @author Junki Kim(wishoping@gmail.com)
 * @date 2013. 5. 20.
 */
@XmlRootElement
public class DailyData extends Entity
{
	private String day;
	
	private int loginCount;

	public String getDay()
	{
		return day;
	}

	public void setDay(String day)
	{
		this.day = day;
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
