package com.l2bq.rest.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Signup User Data Entity Class
 * @author Junki Kim (wishoping@gmail.com), Wooseok Seo (wooseok.seo@gmail.com)
 * @date 2013. 5. 22.
 *
 */
@XmlRootElement
public class SignupData extends Entity
{
	private long time;
	
	private int userId;
	
	private String userType; 
	
	private String userName;
	
	private int langType;
	
	private int osType;
	
	private String phone = "010-1234-";
	
	private long utfOffset;
	
	public SignupData()
	{
		
	}

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int id)
	{
		this.userId = id;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public int getLangType()
	{
		return langType;
	}

	public void setLangType(int langType)
	{
		this.langType = langType;
	}

	public int getOsType()
	{
		return osType;
	}

	public void setOsType(int osType)
	{
		this.osType = osType;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getUtfOffset() {
		return utfOffset;
	}

	public void setUtfOffset(long utfOffset) {
		this.utfOffset = utfOffset;
	}

}
