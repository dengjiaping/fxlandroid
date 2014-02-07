package com.aalife.android;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {
    private SharedPreferences setting = null;
    
	public SharedHelper(Context context) {
		setting = context.getSharedPreferences("setting", 0);
	}

	//当前日期
	public String getDate() {
		return setting.getString("date", "");
	}

	public void setDate(String date) {
		setting.edit().putString("date", date).commit();
	}
	
	//今日
	public String getToday() {
		return setting.getString("today", "");
	}

	public void setToday(String date) {
		setting.edit().putString("today", date).commit();
	}
	
	//用户名
	public String getUserName() {
		return setting.getString("username", "");
	}

	public void setUserName(String userName) {
		setting.edit().putString("username", userName).commit();
	}

	//群组
	public int getGroup() {
		return setting.getInt("group", 0);
	}

	public void setGroup(int group) {
		setting.edit().putInt("group", group).commit();
	}

	//用户ID
	public int getUserId() {
		return setting.getInt("userid", 0);
	}

	public void setUserId(int userId) {
		setting.edit().putInt("userid", userId).commit();
	}
	
	//当前Category
	public int getCategory() {
		return setting.getInt("category", 0);
	}

	public void setCategory(int category) {
		setting.edit().putInt("category", category).commit();
	}

	//同步状态
	public String getSyncStatus() {
		return setting.getString("sync", "");
	}

	public void setSyncStatus(String status) {
		setting.edit().putString("sync", status).commit();
	}

	//本地同步
	public boolean getLocalSync() {
		return setting.getBoolean("localsync", false);
	}

	public void setLocalSync(Boolean flag) {
		setting.edit().putBoolean("localsync", flag).commit();
	}
	
	//网络同步
	public boolean getWebSync() {
		return setting.getBoolean("websync", false);
	}

	public void setWebSync(Boolean flag) {
		setting.edit().putBoolean("websync", flag).commit();
	}

	//备份恢复
	public boolean getRestore() {
		return setting.getBoolean("restore", false);
	}

	public void setRestore(Boolean flag) {
		setting.edit().putBoolean("restore", flag).commit();
	}

	//用户登录
	public boolean getLogin() {
		return setting.getBoolean("login", false);
	}

	public void setLogin(Boolean flag) {
		setting.edit().putBoolean("login", flag).commit();
	}

	//首次同步网络
	public boolean getFirstSync() {
		return setting.getBoolean("firstsync", false);
	}

	public void setFirstSync(Boolean flag) {
		setting.edit().putBoolean("firstsync", flag).commit();
	}

	//同步中
	public boolean getSyncing() {
		return setting.getBoolean("syncing", false);
	}

	public void setSyncing(Boolean flag) {
		setting.edit().putBoolean("syncing", flag).commit();
	}
	
}
