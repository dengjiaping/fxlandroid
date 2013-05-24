package com.aalife.android.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class MyItem {
	private String itemId = "";
	private String catName = "";
	private String catTypeId = "";
	private String itemPrice = "";
	private String itemBuyDate = "";
	private String userId = "";
	private String userGroupId = "";

	public MyItem(){		
	}	
	public MyItem(String itemId) {
		this.itemId = itemId;
	}	
	public MyItem(String catName, String catTypeId, String itemPrice, String itemBuyDate, String userId, String userGroupId) {
		this.catName = catName;
		this.catTypeId = catTypeId;
		this.itemPrice = itemPrice;
		this.itemBuyDate = itemBuyDate;
		this.userId = userId;
		this.userGroupId = userGroupId;		
	}
	public MyItem(String itemId, String catName, String catTypeId, String itemPrice, String itemBuyDate, String userId, String userGroupId) {
		this.itemId = itemId;
		this.catName = catName;
		this.catTypeId = catTypeId;
		this.itemPrice = itemPrice;
		this.itemBuyDate = itemBuyDate;
		this.userId = userId;
		this.userGroupId = userGroupId;		
	}

	public String addItem() {
		String result = "";
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/AddItem.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("catname", this.catName));
		params.add(new BasicNameValuePair("cattypeid", this.catTypeId));
		params.add(new BasicNameValuePair("itemprice", this.itemPrice));
		params.add(new BasicNameValuePair("itembuydate", this.itemBuyDate));
		params.add(new BasicNameValuePair("userid", this.userId));
		params.add(new BasicNameValuePair("usergroupid", this.userGroupId));
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
	
	public String editItem() {
		String result = "";
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/EditItem.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("itemid", this.itemId));
		params.add(new BasicNameValuePair("catname", this.catName));
		params.add(new BasicNameValuePair("cattypeid", this.catTypeId));
		params.add(new BasicNameValuePair("itemprice", this.itemPrice));
		params.add(new BasicNameValuePair("itembuydate", this.itemBuyDate));
		params.add(new BasicNameValuePair("userid", this.userId));
		params.add(new BasicNameValuePair("usergroupid", this.userGroupId));
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

	public String deleteItem() {
		String result = "";
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/DeleteItem.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("itemid", this.itemId));
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
