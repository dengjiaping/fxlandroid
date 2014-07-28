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
	//private static final String WEBURL = "http://10.0.2.2:81";//
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
			CategoryTableAccess categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());			
			List<Map<String, String>> list = categoryAccess.findAllSyncCat();
			categoryAccess.close();
			if(list.size() > 0) {
				try {
					syncCategory(list);
				} catch(Exception e) {
					throw new Exception();
				}
			}

			ItemTableAccess itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
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
			itemAccess.close();
			if(list.size() > 0) {
				try {
					delSyncItem(list);
					itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
					itemAccess.clearDelTable();
				} catch(Exception e) {
					throw new Exception();
				} finally {
					itemAccess.close();
				}
			}

			sharedHelper.setLocalSync(false);
			sharedHelper.setSyncStatus(this.context.getString(R.string.txt_home_syncat) + " " + UtilityHelper.getSyncDate());
						
			//检查网络同步数据
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
				throw new Exception();
			}
			
			sharedHelper.setWebSync(false);
			sharedHelper.setSyncStatus(this.context.getString(R.string.txt_home_syncat) + " " + UtilityHelper.getSyncDate());
		}
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
	
	//同步用户名
	public int[] syncUserName(String userName) {
		int[] result = new int[] { 0, 0 };
		String url = WEBURL +  "/AALifeWeb/SyncUser.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", userName));
		params.add(new BasicNameValuePair("userpass", userName));
		params.add(new BasicNameValuePair("userfrom", context.getString(R.string.app_version)));

		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result[0] = jsonObject.getInt("group");
				result[1] = jsonObject.getInt("userid");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	//同步消费
	public void syncItem(List<Map<String, String>> list) throws Exception {
		boolean syncFlag = false;
		ItemTableAccess itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
		String result = "";
		String url = WEBURL +  "/AALifeWeb/SyncItemNew2.aspx";
		String userId = String.valueOf(sharedHelper.getUserId());
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		Iterator<Map<String, String>> it = list.iterator();
		while(it.hasNext() && sharedHelper.getSyncing()) {
			Map<String, String> map = it.next();
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
			params.add(new BasicNameValuePair("regionid", map.get("regionid")));
	
			try {
				JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
				if(jsonObject.length() > 0) {
					result = jsonObject.getString("result");
				}
			} catch(Exception e) {
				syncFlag = false;
				continue;
			}

			int itemId = Integer.parseInt(map.get("itemid"));
			int itemWebId = 0;
			if(!result.equals("0") || !result.equals("no")) {
				itemWebId = Integer.parseInt(result);
				itemAccess.updateSyncStatus(itemId, itemWebId);
			} else {
				syncFlag = true;
			}
		}
		itemAccess.close();
		
		if(syncFlag) {
			throw new Exception();
		}
	}
	
	//同步类别
	public void syncCategory(List<Map<String, String>> list) throws Exception {
		boolean syncFlag = false;
		CategoryTableAccess categoryAccess = new CategoryTableAccess(sqlHelper.getReadableDatabase());
		String result = "";
		String url = WEBURL +  "/AALifeWeb/SyncCategory.aspx";
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		Iterator<Map<String, String>> it = list.iterator();
		while(it.hasNext()) {
			Map<String, String> map = it.next();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("catid", map.get("catid")));
			params.add(new BasicNameValuePair("catname", map.get("catname")));
			params.add(new BasicNameValuePair("catdisplay", map.get("catdisplay")));
			params.add(new BasicNameValuePair("catlive", map.get("catlive")));
			params.add(new BasicNameValuePair("usergroupid", userGroupId));
	
			try {
				JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
				if(jsonObject.length() > 0) {
					result = jsonObject.getString("result");
				}
			} catch(Exception e) {
				syncFlag = true;
				continue;
			}
			
			if(result.equals("ok")) {
				int catId = Integer.parseInt(map.get("catid"));
				categoryAccess.updateSyncStatus(catId);
			} else {
				syncFlag = true;
			}
		}
		categoryAccess.close();
		
		if(syncFlag) {
			throw new Exception();
		}
	}
	
	//同步删除
	public void delSyncItem(List<Map<String, String>> list) throws Exception {
		boolean syncFlag = false;
		String result = "";
		String url = WEBURL +  "/AALifeWeb/DelSyncItemNew.aspx";
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		Iterator<Map<String, String>> it = list.iterator();
		while(it.hasNext()) {
			Map<String, String> map = it.next();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("itemid", map.get("itemid")));
			params.add(new BasicNameValuePair("itemwebid", map.get("itemwebid")));
			params.add(new BasicNameValuePair("usergroupid", userGroupId));
	
			try {
				JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
				if(jsonObject.length() > 0) {
					result = jsonObject.getString("result");
				}
			} catch(Exception e) {
				syncFlag = true;
				continue;
			}
			
			if(!result.equals("ok")) {
				syncFlag = true;
			}
		}	
		
		if(syncFlag) {
			throw new Exception();
		}	
	}

	//取首次同步网络消费
	public List<Map<String, String>> getSyncWebFirst() throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String url = WEBURL +  "/AALifeWeb/GetSyncWebFirstNew.aspx";
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
			map.put("regionid", jsonObject.getString("regionid"));
			list.add(map);
		}
		
		return list;
	}

	//取同步网络消费
	public List<Map<String, String>> getSyncWebItem() throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String url = WEBURL +  "/AALifeWeb/GetSyncWebItemNew.aspx";
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
			map.put("regionid", jsonObject.getString("regionid"));
			list.add(map);
		}
		
		return list;
	}

	//同步网络消费
	public void syncWebItem(List<Map<String, String>> list) throws Exception {
		boolean syncFlag = false;
		ItemTableAccess itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
		Iterator<Map<String, String>> it = list.iterator();
		while(it.hasNext() && sharedHelper.getSyncing()) {
			Map<String, String> map = (Map<String, String>) it.next();
			int itemId = Integer.parseInt(map.get("itemid"));
			int itemAppId = Integer.parseInt(map.get("itemappid"));
			String itemName = map.get("itemname");
			String itemPrice = map.get("itemprice");
			String itemBuyDate = map.get("itembuydate");
			int catId = Integer.parseInt(map.get("catid"));
			int recommend = Integer.parseInt(map.get("recommend"));
			int regionId = Integer.parseInt(map.get("regionid"));
			
			//用于首页实时更新
			if(UtilityHelper.compareDate(itemBuyDate)) {
				sharedHelper.setCurDate(UtilityHelper.formatDate(itemBuyDate, "y-m-d"));
			}
			
			boolean success = itemAccess.addWebItem(itemId, itemAppId, itemName, itemPrice, itemBuyDate, catId, recommend, regionId);
			if(!success) {
				syncFlag = true;
				continue;
			}
			
			//根据ItemWebID取ItemID
			if(itemAppId == 0) itemAppId = itemAccess.getItemId(itemId);
			
			if(!syncWebItemBack(itemId, itemAppId)) {
				syncFlag = true;
			}
		}
		itemAccess.close();
		
		if(syncFlag) {
			throw new Exception();
		}
	}

	//同步网络消费返回
	public boolean syncWebItemBack(int itemId, int itemAppId) {
		String result = "";
		String url = WEBURL +  "/AALifeWeb/SyncWebItemBackNew.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("itemid", String.valueOf(itemId)));
		params.add(new BasicNameValuePair("itemappid", String.valueOf(itemAppId)));
		
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result = jsonObject.getString("result");
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return result.equals("ok");
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
	
	//同步网络类别
	public void syncWebCategory(List<Map<String, String>> list) throws Exception {
		CategoryTableAccess categoryAccess = new CategoryTableAccess(this.sqlHelper.getReadableDatabase());
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

	//同步网络类别返回
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
		
		if(!result.equals("ok")) {
			throw new Exception();
		}
	}

	//取同步网络删除
	public List<Map<String, String>> getDelSyncWebItem() throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String url = WEBURL +  "/AALifeWeb/GetDelSyncWebItem.aspx";
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usergroupid", userGroupId));
		
		JSONObject jsonAll = new JSONObject(HttpHelper.post(url, params));
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
	
	//同步网络删除
	public void syncDelWebItem(List<Map<String, String>> list) throws Exception {
		ItemTableAccess itemAccess = new ItemTableAccess(this.sqlHelper.getReadableDatabase());
		Iterator<Map<String, String>> it = list.iterator();
		while(it.hasNext()) {
			Map<String, String> map = (Map<String, String>) it.next();
			int itemId = Integer.parseInt(map.get("itemid"));
			int itemAppId = Integer.parseInt(map.get("itemappid"));
			
			itemAccess.delWebItem(itemId, itemAppId);
		}
		itemAccess.close();
	}

	//同步网络删除返回
	public void syncDelWebItemBack() throws Exception {
		String result = "";
		String url = WEBURL +  "/AALifeWeb/SyncDelWebItemBack.aspx";
		String userGroupId = String.valueOf(sharedHelper.getGroup());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usergroupid", userGroupId));
		
		JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
		if(jsonObject.length() > 0) {
			result = jsonObject.getString("result");
		}
		
		if(!result.equals("ok")) {
			throw new Exception();
		}
	}

}
