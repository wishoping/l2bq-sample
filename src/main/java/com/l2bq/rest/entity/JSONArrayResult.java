package com.l2bq.rest.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class JSONArrayResult extends Result {
	private JSONArray list;
	
	public JSONArrayResult() {
		// TODO Auto-generated constructor stub
	}

	public JSONArray getList() {
		return list;
	}

	public void setList(JSONArray list) {
		this.list = list;
	}

	@Override
	public String toString() {
		String jsonStr = new Gson().toJson(this);
		try {
			JSONObject json = new JSONObject(jsonStr);
			
			json.remove("list");
			if ( list == null ) {
				json.put("list", "[]");
			} else {
				json.put("list", list);
			}
			
			return json.toString();
		} catch (JSONException e) {
			return "";
		}
		
	}
}
