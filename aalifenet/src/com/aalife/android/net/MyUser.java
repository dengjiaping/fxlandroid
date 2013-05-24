package com.aalife.android.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class MyUser {
	private String userId = "";
	private String userName = "";
	private String userPass = "";
	private String userNickName = "";
	private String userImage = "";
	private String userGroupId = "";
	private String userGroupLive = "";
	private String userGroupSave = "";
	private String userEmail = "";
	private String userPhone = "";
	private ArrayList<String> userList = new ArrayList<String>(); 
	private ArrayList<Map<String, String>> userGroupList = new ArrayList<Map<String, String>>();
	
	public MyUser() {		
	}
	public MyUser(String userId, String userName, String userPass, String userNickName, String userImage, String userGroupId, String userGroupLive, String userGroupSave, String userEmail, String userPhone) {
		this.userId = userId;
		this.userName = userName;
		this.userPass = userPass;
		this.userNickName = userNickName;
		this.userImage = userImage;
		this.userGroupId = userGroupId;
		this.userGroupLive = userGroupLive;
		this.userGroupSave = userGroupSave;
		this.userEmail = userEmail;
		this.userPhone = userPhone;
	}
	
	public ArrayList<String> getUserList() {
		return userList;
	}
	public void setUserList(ArrayList<String> userList) {
		this.userList = userList;
	}
	
	public String getUserGroupLive() {
		return userGroupLive;
	}
	public void setUserGroupLive(String userGroupLive) {
		this.userGroupLive = userGroupLive;
	}	
	public ArrayList<Map<String, String>> getUserGroupList() {
		return userGroupList;
	}
	public void setUserGroupList(ArrayList<Map<String, String>> userGroupList) {
		this.userGroupList = userGroupList;
	}
	
	public String getUserListForLogin(String userName, String userPass) {
		String result = "";
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/Login.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", userName));
		params.add(new BasicNameValuePair("userpass", userPass));
		try {
			JSONObject jsonAll = new JSONObject(HttpHelper.post(url, params));
			result = jsonAll.getString("result");
			if(result.equals("ok")) {
				JSONArray jsonArray = jsonAll.getJSONArray("userlist");
				if(jsonArray.length() > 0) {
					JSONObject jsonObject = jsonArray.getJSONObject(0);
					userList.add(jsonObject.getString("userid"));
					userList.add(jsonObject.getString("username"));
					userList.add(jsonObject.getString("userpass"));
					userList.add(jsonObject.getString("usernickname"));
					userList.add(jsonObject.getString("userimage"));
					userList.add(jsonObject.getString("usergroupid"));
					userList.add(jsonObject.getString("useremail"));
					userList.add(jsonObject.getString("userphone"));
				}
			}
		} catch (Exception e) {
			result = "error";
			e.printStackTrace();
		}
		
		return result;
	}

	public String getUserGroupListForMsg(String userGroupId) {
		String result = "";
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/GetUserGroupList.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("usergroupid", userGroupId));
		try{
			JSONObject jsonAll = new JSONObject(HttpHelper.post(url, params));
			result = jsonAll.getString("result");
			JSONArray jsonArray = jsonAll.getJSONArray("usergrouplist");
			for(int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("userid", jsonObject.getString("userid"));
				map.put("username", jsonObject.getString("username"));
				map.put("userfrom", jsonObject.getString("userfrom"));
				map.put("usergroupid", jsonObject.getString("usergroupid"));
				map.put("usergrouplive", jsonObject.getString("usergrouplive"));
				userGroupList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String addUser() {
		String result = "";
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/AddUser.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", this.userName));
		params.add(new BasicNameValuePair("userpass", this.userPass));
		params.add(new BasicNameValuePair("usernickname", this.userNickName));
		params.add(new BasicNameValuePair("usergroupid", this.userGroupId));
		params.add(new BasicNameValuePair("userEmail", this.userEmail));
		params.add(new BasicNameValuePair("userphone", this.userPhone));
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

	public String updateUser() {
		String result = "";
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/UpdateUser.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", this.userId));
		params.add(new BasicNameValuePair("userpass", this.userPass));
		params.add(new BasicNameValuePair("usernickname", this.userNickName));
		params.add(new BasicNameValuePair("userimage", this.userImage));
		params.add(new BasicNameValuePair("usergroupid", this.userGroupId));
		params.add(new BasicNameValuePair("usergrouplive", this.userGroupLive));
		params.add(new BasicNameValuePair("usergroupsave", this.userGroupSave));
		params.add(new BasicNameValuePair("useremail", this.userEmail));
		params.add(new BasicNameValuePair("userphone", this.userPhone));
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
	
	public void getUser(String userId) {
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/GetUser.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userId));
		try {
			JSONObject jsonAll = new JSONObject(HttpHelper.post(url, params));
			JSONArray jsonArray = jsonAll.getJSONArray("userlist");
			if(jsonArray.length() > 0) {
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				userList.add(jsonObject.getString("userid"));
				userList.add(jsonObject.getString("username"));
				userList.add(jsonObject.getString("userpass"));
				userList.add(jsonObject.getString("usernickname"));
				userList.add(jsonObject.getString("userimage"));
				userList.add(jsonObject.getString("usergroupid"));
				userList.add(jsonObject.getString("useremail"));
				userList.add(jsonObject.getString("userphone"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String changeUserGroupLive(String userId, String userGroupId) {
		String result = "";
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/ChangeUserGroupLive.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userId));
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
	
	public String changeUserGroupCancel(String userId) {
		String result = "";
		String url = MyHelper.getWebUrl() +  "/AALifeWeb/ChangeUserGroupCancel.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userId));
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
	
	public Bitmap getUserImageFormUrl(String url) {
		Bitmap bitmap = HttpHelper.postBitmap(url);
		return bitmap;
	}
}
