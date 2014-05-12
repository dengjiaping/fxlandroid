package com.aalife.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class SyncHelper {
	private SharedHelper sharedHelper = null;
	private Context context = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private CategoryTableAccess categoryAccess = null;
	//private static final String WEBURL = "http://10.0.2.2:81";
	private static final String WEBURL = "http://www.fxlweb.com";
	
	public SyncHelper(Context context) {
		this.context = context;
		sharedHelper = new SharedHelper(this.context);
		sqlHelper = new DatabaseHelper(this.context);
		sqlHelper.close();
	}
	
	//开始同步
	public void Start() throws Exception {
		//同步用户
		if(sharedHelper.getUserId() == 0) {
			String userName = UtilityHelper.createUserName();
			
			int[] result = syncUserName(userName);
			if(result[0] > 0) {
				sharedHelper.setGroup(result[0]);
				sharedHelper.setUserId(result[1]);
				sharedHelper.setUserName(userName);
				sharedHelper.setUserPass(userName);
				sharedHelper.setLogin(true);
			} else {
				throw new Exception();
			}
		}
		//同步本地
		if(sharedHelper.getLocalSync()) {
			categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());			
			List<Map<String, String>> list = categoryAccess.findAllSyncCat();
			categoryAccess.close();
			if(list.size() > 0) {
				try {
					syncCategory(list);
				} catch(Exception e) {
					throw new Exception();
				}
			}

			itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
			list = itemAccess.findSyncItem();
			itemAccess.close();
			if(list.size() > 0) {
				try {
					syncItem(list);
				} catch(Exception e) {
					throw new Exception();
				}
			}
			itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
			list = itemAccess.findDelSyncItem();
			if(list.size() > 0) {
				try {
					delSyncItem(list);
					itemAccess.clearDelTable();
				} catch(Exception e) {
					itemAccess.close();
					throw new Exception();
				}
			}
			itemAccess.close();

			sharedHelper.setLocalSync(false);
			sharedHelper.setFirstSync(true);
			sharedHelper.setSyncStatus(this.context.getString(R.string.txt_home_syncat) + " " + UtilityHelper.getSyncDate());
						
			//检查网络
			if(checkSyncWeb() == 1) {
				sharedHelper.setWebSync(true);
			}
		}		
		//同步网络
		if(sharedHelper.getWebSync()) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			try {
				list = getSyncWebCategory();
				if(list.size() > 0) {
					syncWebCategory(list);
					syncWebCategoryBack();
				}			
				
				if(!sharedHelper.getFirstSync()) {
					list = getSyncWebFirst();
					sharedHelper.setFirstSync(true);
				} else {
					list = getSyncWebItem();
				}
				if(list.size() > 0) {
					syncWebItem(list);
				}
				
				list = getDelSyncWebItem();
				if(list.size() > 0) {
					syncDelWebItem(list);
					syncDelWebItemBack();
				}
			} catch (Exception e) {
				sharedHelper.setSyncStatus(this.context.getString(R.string.txt_home_haswebsync));
				throw new Exception();
			}
			
			sharedHelper.setWebSync(false);
			sharedHelper.setSyncStatus(this.context.getString(R.string.txt_home_syncat) + " " + UtilityHelper.getSyncDate());
		}
	}
	
	//同步用户名
	public int[] syncUserName(String userName) throws Exception {
		int[] result = new int[2];
		String url = WEBURL +  "/AALifeWeb/SyncUser.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", userName));
		params.add(new BasicNameValuePair("userpass", userName));

		JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
		if(jsonObject.length() > 0) {
			result[0] = jsonObject.getInt("group");
			result[1] = jsonObject.getInt("userid");
		}
		
		return result;
	}
	
	//同步消费
	public void syncItem(List<Map<String, String>> list) throws Exception {
		itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
		String result = "";
		String url = WEBURL +  "/AALifeWeb/SyncItem.aspx";
		String userId = String.valueOf(sharedHelper.getUserId());
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		for(int i=0; i<list.size(); i++) {
			Map<String, String> map = list.get(i);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("itemid", map.get("itemid")));
			params.add(new BasicNameValuePair("itemname", map.get("itemname")));
			params.add(new BasicNameValuePair("catid", map.get("catid")));
			params.add(new BasicNameValuePair("itemprice", map.get("itemprice")));
			params.add(new BasicNameValuePair("itembuydate", map.get("itembuydate")));
			params.add(new BasicNameValuePair("userid", userId));
			params.add(new BasicNameValuePair("usergroupid", userGroupId));
			params.add(new BasicNameValuePair("itemwebid", map.get("itemwebid")));
			params.add(new BasicNameValuePair("recommend", map.get("recommend")));
	
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result = jsonObject.getString("result");
			}
			
			if(result.equals("ok")) {
				int itemId = Integer.parseInt(map.get("itemid"));
				itemAccess.updateSyncStatus(itemId);
			} else {
				itemAccess.close();
				throw new Exception();
			}
		}
		itemAccess.close();
	}

	//同步消费//作废：由于一次性发送数据过大会造成超时
	public void syncItem(String json) throws Exception {
		String url = WEBURL +  "/AALifeWeb/SyncItemJson.aspx";
		String userId = String.valueOf(sharedHelper.getUserId());
		String userGroupId = String.valueOf(sharedHelper.getGroup());

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("json", json));
		params.add(new BasicNameValuePair("userid", userId));
		params.add(new BasicNameValuePair("usergroupid", userGroupId));

		String result = HttpHelper.post(url, params);
		if(result.equals("")) {
			throw new Exception();
		}
	}
	
	//同步类别
	public void syncCategory(List<Map<String, String>> list) throws Exception {
		categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());
		String result = "";
		String url = WEBURL +  "/AALifeWeb/SyncCategory.aspx";
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		for(int i=0; i<list.size(); i++) {
			Map<String, String> map = list.get(i);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("catid", map.get("catid")));
			params.add(new BasicNameValuePair("catname", map.get("catname")));
			params.add(new BasicNameValuePair("catdisplay", map.get("catdisplay")));
			params.add(new BasicNameValuePair("catlive", map.get("catlive")));
			params.add(new BasicNameValuePair("usergroupid", userGroupId));
	
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result = jsonObject.getString("result");
			}
			
			if(result.equals("ok")) {
				int catId = Integer.parseInt(map.get("catid"));
				categoryAccess.updateSyncStatus(catId);
			} else {
				categoryAccess.close();
				throw new Exception();
			}
		}
		categoryAccess.close();
	}
	
	//删除同步消费
	public void delSyncItem(List<Map<String, String>> list) throws Exception {
		String result = "";
		String url = WEBURL +  "/AALifeWeb/DelSyncItem.aspx";
		for(int i=0; i<list.size(); i++) {
			Map<String, String> map = list.get(i);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("itemid", map.get("itemid")));
			params.add(new BasicNameValuePair("itemwebid", map.get("itemwebid")));
	
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result = jsonObject.getString("result");
			}
			
			if(result.equals("no")) {
				throw new Exception();
			}
		}		
	}
	
	//取同步网络消费
	public List<Map<String, String>> getSyncWebItem() throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String url = WEBURL +  "/AALifeWeb/GetSyncWebItem.aspx";
		String userId = String.valueOf(sharedHelper.getUserId());
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userId));
		params.add(new BasicNameValuePair("usergroupid", userGroupId));

		JSONObject jsonAll = new JSONObject(HttpHelper.post(url, params));
		JSONArray jsonArray = jsonAll.getJSONArray("itemlist");
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("itemid", jsonObject.getString("itemid"));
			map.put("itemappid", jsonObject.getString("itemappid"));
			map.put("itemname", jsonObject.getString("itemname"));
			map.put("catid", jsonObject.getString("catid"));
			map.put("itemprice", jsonObject.getString("itemprice"));
			map.put("itembuydate", jsonObject.getString("itembuydate"));
			map.put("recommend", jsonObject.getString("recommend"));
			list.add(map);
		}
		
		return list;
	}

	//取第一次同步网络消费
	public List<Map<String, String>> getSyncWebFirst() throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String url = WEBURL +  "/AALifeWeb/GetSyncWebFirst.aspx";
		String userId = String.valueOf(sharedHelper.getUserId());
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userId));
		params.add(new BasicNameValuePair("usergroupid", userGroupId));

		JSONObject jsonAll = new JSONObject(HttpHelper.post(url, params));
		JSONArray jsonArray = jsonAll.getJSONArray("itemlist");
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("itemid", jsonObject.getString("itemid"));
			map.put("itemappid", jsonObject.getString("itemappid"));
			map.put("itemname", jsonObject.getString("itemname"));
			map.put("catid", jsonObject.getString("catid"));
			map.put("itemprice", jsonObject.getString("itemprice"));
			map.put("itembuydate", jsonObject.getString("itembuydate"));
			map.put("recommend", jsonObject.getString("recommend"));
			list.add(map);
		}
		
		return list;
	}
	
	//取同步网络类别
	public List<Map<String, String>> getSyncWebCategory() throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String url = WEBURL +  "/AALifeWeb/GetSyncWebCategory.aspx";
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usergroupid", userGroupId));

		JSONObject jsonAll = new JSONObject(HttpHelper.post(url, params));
		JSONArray jsonArray = jsonAll.getJSONArray("catlist");
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("catid", jsonObject.getString("catid"));
			map.put("catname", jsonObject.getString("catname"));
			map.put("catdisplay", jsonObject.getString("catdisplay"));
			map.put("catlive", jsonObject.getString("catlive"));
			list.add(map);
		}
		
		return list;
	}
	
	//取删除同步网络消费
	public List<Map<String, String>> getDelSyncWebItem() throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String url = WEBURL +  "/AALifeWeb/GetDelSyncWebItem.aspx";

		JSONObject jsonAll = new JSONObject(HttpHelper.post(url));
		JSONArray jsonArray = jsonAll.getJSONArray("deletelist");
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("itemid", jsonObject.getString("itemid"));
			map.put("itemappid", jsonObject.getString("itemappid"));
			list.add(map);
		}
		
		return list;
	}
	
	//检查同步网络消费
	public int checkSyncWeb() {
		String result = "";
		String url = WEBURL +  "/AALifeWeb/CheckSyncWeb.aspx";
		String userId = String.valueOf(sharedHelper.getUserId());
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userId));
		params.add(new BasicNameValuePair("usergroupid", userGroupId));
		
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result = jsonObject.getString("result");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result.equals("ok") ? 1 : result.equals("error") ? 0 : 2;
	}
	
	//同步网络消费
	public void syncWebItem(List<Map<String, String>> list) throws Exception {
		itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
		Iterator<Map<String, String>> it = list.iterator();
		while(it.hasNext()) {
			Map<String, String> map = (Map<String, String>) it.next();
			int itemId = Integer.parseInt(map.get("itemid"));
			int itemAppId = Integer.parseInt(map.get("itemappid"));
			String itemName = map.get("itemname");
			String itemPrice = map.get("itemprice");
			String itemBuyDate = map.get("itembuydate");
			int catId = Integer.parseInt(map.get("catid"));
			int recommend = Integer.parseInt(map.get("recommend"));
			
			boolean result = itemAccess.addWebItem(itemId, itemAppId, itemName, itemPrice, itemBuyDate, catId, recommend);
			if(result) {
				if(!syncWebItemBack(itemId)) {
					itemAccess.delWebItem(itemId, itemAppId);
					itemAccess.close();
					throw new Exception();
				}
			}
		}
		itemAccess.close();
	}

	//同步网络类别
	public void syncWebCategory(List<Map<String, String>> list) throws Exception {
		categoryAccess = new CategoryTableAccess(this.sqlHelper.getReadableDatabase());
		Iterator<Map<String, String>> it = list.iterator();
		while(it.hasNext()) {
			Map<String, String> map = (Map<String, String>) it.next();
			int catId = Integer.parseInt(map.get("catid"));
			String catName = map.get("catname");
			int catDisplay = Integer.parseInt(map.get("catdisplay"));
			int catLive = Integer.parseInt(map.get("catlive"));
			
			try {
				categoryAccess.saveWebCategory(catId, catName, catDisplay, catLive);
			} catch(Exception e) {
				categoryAccess.close();
				throw new Exception();
			}
		}
		categoryAccess.close();
	}
	
	//同步删除网络消费
	public void syncDelWebItem(List<Map<String, String>> list) {
		itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
		Iterator<Map<String, String>> it = list.iterator();
		while(it.hasNext()) {
			Map<String, String> map = (Map<String, String>) it.next();
			int itemId = Integer.parseInt(map.get("itemid"));
			int itemAppId = Integer.parseInt(map.get("itemappid"));
			
			try {
				itemAccess.delWebItem(itemId, itemAppId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		itemAccess.close();
	}
	
	//同步网络消费返回
	public boolean syncWebItemBack(int itemId) {
		String result = "";
		String url = WEBURL +  "/AALifeWeb/SyncWebItemBack.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("itemid", String.valueOf(itemId)));
		
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result = jsonObject.getString("result");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return result.equals("ok");
	}

	//同步删除网络消费返回
	public void syncDelWebItemBack() throws Exception {
		String result = "";
		String url = WEBURL +  "/AALifeWeb/SyncDelWebItemBack.aspx";
		
		JSONObject jsonObject = new JSONObject(HttpHelper.post(url));
		if(jsonObject.length() > 0) {
			result = jsonObject.getString("result");
		}
		
		if(result.equals("no")) {
			throw new Exception();
		}
	}

	//同步删除网络类别返回
	public void syncWebCategoryBack() throws Exception {
		String result = "";
		String url = WEBURL +  "/AALifeWeb/SyncWebCategoryBack.aspx";
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usergroupid", userGroupId));
		
		JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
		if(jsonObject.length() > 0) {
			result = jsonObject.getString("result");
		}
		
		if(result.equals("no")) {
			throw new Exception();
		}
	}

	//同步删除网络类别返回
	public void syncWebItemBackAll(List<Map<String, String>> list) throws Exception {
		String result = "";
		String url = WEBURL +  "/AALifeWeb/SyncWebItemBackAll.aspx";
		String userId = String.valueOf(sharedHelper.getUserId());
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userId));
		params.add(new BasicNameValuePair("usergroupid", userGroupId));
		
		JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
		if(jsonObject.length() > 0) {
			result = jsonObject.getString("result");
		}
		
		if(result.equals("no")) {
			throw new Exception();
		}
	}
	
}
