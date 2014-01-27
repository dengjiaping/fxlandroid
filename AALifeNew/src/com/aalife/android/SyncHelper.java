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
	private SharedHelper setting = null;
	private Context context = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	private CategoryTableAccess categoryAccess = null;
	
	public SyncHelper(Context context) {
		this.context = context;
		setting = new SharedHelper(this.context);
		sqlHelper = new DatabaseHelper(this.context);
		sqlHelper.close();
	}
	
	//开始同步
	public void Start() throws Exception {
		if(setting.getUserId() == 0) {
			String userName = UtilityHelper.createUserName();
			
			int[] result = syncUserName(userName);
			if(result[0] > 0) {
				setting.setGroup(result[0]);
				setting.setUserId(result[1]);
				setting.setUserName(userName);
			} else {
				throw new Exception();
			}
		}
		
		if(setting.getLocalSync()) {
			itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
			categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());
			
			List<Map<String, String>> list = categoryAccess.findAllSyncCat();
			if(list.size() > 0) {
				try {
					syncCategory(list);
				} catch(Exception e) {
					categoryAccess.close();
					e.printStackTrace();
					throw new Exception();
				}
			}
			
			list = itemAccess.findSyncItem();
			if(list.size() > 0) {
				try {
					syncItem(list);
				} catch(Exception e) {
					itemAccess.close();
					e.printStackTrace();
					throw new Exception();
				}
			}
			
			list = itemAccess.findDelSyncItem();			
			if(list.size() > 0) {
				try {
					delSyncItem(list);
					itemAccess.clearDelTable();
				} catch(Exception e) {
					itemAccess.close();
					e.printStackTrace();
					throw new Exception();
				}
			}

			setting.setLocalSync(false);
			setting.setSyncStatus(this.context.getString(R.string.txt_home_syncat) + " " + UtilityHelper.getSyncDate());
			
			itemAccess.close();
			categoryAccess.close();
		}		
		
		if(setting.getWebSync() || checkSyncWeb()) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			list = getSyncWebCategory();
			if(list.size() > 0) {
				syncWebCategory(list);
				syncWebCategoryBack();
			}			
			
			if(!setting.getFirstSync()) {
				list = getSyncWebItem(0);
				setting.setFirstSync(true);
			} else {
				list = getSyncWebItem(1);
			}
			if(list.size() > 0) {
				syncWebItem(list);
			}
			
			list = getDelSyncWebItem();
			if(list.size() > 0) {
				syncDelWebItem(list);
				syncDelWebItemBack();
			}

			setting.setWebSync(false);
			setting.setSyncStatus(this.context.getString(R.string.txt_home_syncat) + " " + UtilityHelper.getSyncDate());
		}
	}
	
	//同步用户名
	public int[] syncUserName(String userName) {
		int[] result = new int[2];
		String url = getWebUrl() +  "/AALifeWeb/SyncUser.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", userName));
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result[0] = jsonObject.getInt("group");
				result[1] = jsonObject.getInt("userid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	//同步消费
	public void syncItem(List<Map<String, String>> list) throws Exception {
		String url = getWebUrl() +  "/AALifeWeb/SyncItem.aspx";
		String userId = String.valueOf(setting.getUserId());
		String userGroupId = String.valueOf(setting.getGroup());
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
	
			String result = HttpHelper.post(url, params);
			if(!result.equals("")) {
				int itemId = Integer.parseInt(map.get("itemid"));
				itemAccess.updateSyncStatus(itemId);
			}
		}
	}
	
	//同步类别
	public void syncCategory(List<Map<String, String>> list) throws Exception {
		String url = getWebUrl() +  "/AALifeWeb/SyncCategory.aspx";
		String userGroupId = String.valueOf(setting.getGroup());
		for(int i=0; i<list.size(); i++) {
			Map<String, String> map = list.get(i);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("catid", map.get("catid")));
			params.add(new BasicNameValuePair("catname", map.get("catname")));
			params.add(new BasicNameValuePair("catdisplay", map.get("catdisplay")));
			params.add(new BasicNameValuePair("catlive", map.get("catlive")));
			params.add(new BasicNameValuePair("usergroupid", userGroupId));
	
			String result = HttpHelper.post(url, params);
			if(!result.equals("")) {
				int catId = Integer.parseInt(map.get("catid"));
				categoryAccess.updateSyncStatus(catId);
			}
		}
	}
	
	//删除同步消费
	public void delSyncItem(List<Map<String, String>> list) throws Exception {
		String url = getWebUrl() +  "/AALifeWeb/DelSyncItem.aspx";
		for(int i=0; i<list.size(); i++) {
			Map<String, String> map = list.get(i);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("itemid", map.get("itemid")));
			params.add(new BasicNameValuePair("itemwebid", map.get("itemwebid")));
	
			HttpHelper.post(url, params);
		}		
	}
	
	//取同步网络消费
	public List<Map<String, String>> getSyncWebItem(int synchronize) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String url = getWebUrl() +  "/AALifeWeb/GetSyncWebItem.aspx";
		String userId = String.valueOf(setting.getUserId());
		String userGroupId = String.valueOf(setting.getGroup());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userId));
		params.add(new BasicNameValuePair("usergroupid", userGroupId));
		params.add(new BasicNameValuePair("synchronize", String.valueOf(synchronize)));

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
		String url = getWebUrl() +  "/AALifeWeb/GetSyncWebCategory.aspx";
		String userGroupId = String.valueOf(setting.getGroup());
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
		String url = getWebUrl() +  "/AALifeWeb/GetDelSyncWebItem.aspx";

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
	public boolean checkSyncWeb() {
		String result = "";
		String url = getWebUrl() +  "/AALifeWeb/CheckSyncWeb.aspx";
		String userId = String.valueOf(setting.getUserId());
		String userGroupId = String.valueOf(setting.getGroup());
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
		
		return result.equals("ok");
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
			
			Boolean result = itemAccess.addWebItem(itemId, itemAppId, itemName, itemPrice, itemBuyDate, catId, recommend);
			if(result) {
				Boolean bool = syncWebItemBack(itemId);
				if(bool) {
					itemAccess.delWebItem(itemId, itemAppId);
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
			
			categoryAccess.saveWebCategory(catId, catName, catDisplay, catLive);			
		}
		categoryAccess.close();
	}
	
	//同步删除网络消费
	public void syncDelWebItem(List<Map<String, String>> list) throws Exception {
		itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
		Iterator<Map<String, String>> it = list.iterator();
		while(it.hasNext()) {
			Map<String, String> map = (Map<String, String>) it.next();
			int itemId = Integer.parseInt(map.get("itemid"));
			int itemAppId = Integer.parseInt(map.get("itemappid"));
			
			itemAccess.delWebItem(itemId, itemAppId);
		}
		itemAccess.close();
	}
	
	//同步网络消费返回
	public boolean syncWebItemBack(int itemId) {
		String result = "";
		String url = getWebUrl() +  "/AALifeWeb/SyncWebItemBack.aspx";
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
		
		return result.equals("");
	}

	//同步删除网络消费返回
	public void syncDelWebItemBack() throws Exception {
		String url = getWebUrl() +  "/AALifeWeb/SyncDelWebItemBack.aspx";
		HttpHelper.post(url);
	}

	//同步删除网络类别返回
	public void syncWebCategoryBack() throws Exception {
		String url = getWebUrl() +  "/AALifeWeb/SyncWebCategoryBack.aspx";
		HttpHelper.post(url);
	}
	
	public String getWebUrl() {
		//return "http://10.0.2.2:81";
		return "http://www.fxlweb.com";
	}
	
}
