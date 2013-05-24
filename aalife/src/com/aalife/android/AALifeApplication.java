package com.aalife.android;

import android.app.Application;

public class AALifeApplication extends Application {
	private String appDate = "";
	private String catName = "";
	private int groupId = -1;
	private long appInstallTime = 0;

	public String getAppDate() {
		return this.appDate;
	}

	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}

	public String getCatName() {
		return this.catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public long getAppInstallTime() {
		return this.appInstallTime;
	}

	public void setAppInstallTime(long appInstallTime) {
		this.appInstallTime = appInstallTime;
	}

	public int getGroupId() {
		return this.groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.appDate = UtilityHelper.getDateNow("date");
	}

}
