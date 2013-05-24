package com.aalife.android.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class MyItemList {
	private String totalPrice = "";
	private String monthPrice = "";
	private List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
	private ArrayList<CharSequence> catNameList = new ArrayList<CharSequence>();
	private String userGroupId = "";
	private String itemBuyDate = "";
	
	public MyItemList() {		
	}	
	public MyItemList(String userGroupId, String itemBuyDate) {
		this.userGroupId = userGroupId;
		this.itemBuyDate = itemBuyDate;
	}
	
	public String getMonthPrice() {
		return monthPrice;
	}
	public void setMonthPrice(String monthPrice) {
		this.monthPrice = monthPrice;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<Map<String, String>> getItemList() {
		return itemList;
	}
	public void setItemList(List<Map<String, String>> itemList) {
		this.itemList = itemList;
	}	
	public List<Map<String, String>> getUserList() {
		return userList;
	}
	public void setUserList(List<Map<String, String>> userList) {
		this.userList = userList;
	}	
	public ArrayList<CharSequence> getCatNameList() {
		return catNameList;
	}
	public void setCatNameList(ArrayList<CharSequence> catNameList) {
		this.catNameList = catNameList;
	}
	
	public void getItemListForListView() {
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/GetItemList.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usergroupid", this.userGroupId));
		params.add(new BasicNameValuePair("itembuydate", this.itemBuyDate));
		try {
			JSONObject jsonAll = new JSONObject(HttpHelper.post(url, params));
			if(!jsonAll.isNull("monthprice")) {
				this.monthPrice = jsonAll.getString("monthprice");
				if(!jsonAll.isNull("totalprice")) {
					this.totalPrice = jsonAll.getString("totalprice");
					JSONArray jsonArray = jsonAll.getJSONArray("itemlist");
					for(int i=0; i<jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("id", jsonObject.getString("id"));
						map.put("itemid", jsonObject.getString("itemid"));
						map.put("catname", jsonObject.getString("catname"));
						map.put("cattypeid", jsonObject.getString("cattypeid"));
						map.put("itemprice", MyFormat.formatDouble(jsonObject.getString("itemprice"), "0.0#", true));
						map.put("userid", jsonObject.getString("userid"));
						map.put("username", jsonObject.getString("username"));
						itemList.add(map);
					}
					jsonArray = jsonAll.getJSONArray("userlist");
					for(int i=0; i<jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("username", jsonObject.getString("username"));
						map.put("usertotal", jsonObject.getString("usertotal"));
						userList.add(map);
					}					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getCatNameListForAuto(String userGroupId) {
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/GetCatNameList.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usergroupid", userGroupId));
		try {
			JSONObject jsonAll = new JSONObject(HttpHelper.post(url, params));
			JSONArray jsonArray = jsonAll.getJSONArray("catnamelist");
			for(int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				catNameList.add(jsonObject.getString("categoryname"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
