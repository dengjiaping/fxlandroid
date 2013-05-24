package com.aalife.android.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class MyCategoryType {
	private List<Map<String, String>> catTypeList = new ArrayList<Map<String, String>>();
	
	public MyCategoryType() {
	}
	
	public List<Map<String, String>> getCatTypeList() {
		return this.catTypeList;
	}

	public void setCatTypeList(List<Map<String, String>> catTypeList) {
		this.catTypeList = catTypeList;
	}

	public void getCatTypeListForSpinner(String userGroupId) {
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/GetCatTypeList.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usergroupid", userGroupId));
		try{
			JSONObject jsonAll = new JSONObject(HttpHelper.post(url, params));
			JSONArray jsonArray = jsonAll.getJSONArray("cattypelist");
			for(int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("cattypeid", jsonObject.getString("cattypeid"));
				map.put("cattypename", jsonObject.getString("cattypename"));
				catTypeList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getCatTypePosition(String catTypeId) {
		for(int i=0; i<catTypeList.size(); i++) {
			Map<String, String> map = catTypeList.get(i);
			String _catTypeId = map.get("cattypeid");
			if(catTypeId.equals(_catTypeId)) {
				return i;
			}
		}
		
		return 0;
	}
	
	public String addCatType(String userGroupId, String catTypeName){
		String result = "";
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/AddCatType.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cattypename", catTypeName));
		params.add(new BasicNameValuePair("usergroupid", userGroupId));
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result = jsonObject.getString("result");
			}
		} catch(Exception e) {
			result = "error";
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String updateCatType(String catTypeId, String userGroupId, String catTypeName){
		String result = "";
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/UpdateCatType.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cattypename", catTypeName));
		params.add(new BasicNameValuePair("usergroupid", userGroupId));
		params.add(new BasicNameValuePair("cattypeid", catTypeId));
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result = jsonObject.getString("result");
			}
		} catch(Exception e) {
			result = "error";
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String deleteCatType(String catTypeId, String userGroupId, String catTypeName){
		String result = "";
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/DeleteCatType.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cattypename", catTypeName));
		params.add(new BasicNameValuePair("usergroupid", userGroupId));
		params.add(new BasicNameValuePair("cattypeid", catTypeId));
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result = jsonObject.getString("result");
			}
		} catch(Exception e) {
			result = "error";
			e.printStackTrace();
		}
				
		return result;
	}
}
