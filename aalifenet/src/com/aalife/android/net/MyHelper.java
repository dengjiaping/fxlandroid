package com.aalife.android.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MyHelper {
	
	public MyHelper() {
	}
	
	public static String getVersionFromWeb() {
		String version = "";
		String url = getWebUrl() + "/AALifeWeb/GetVersion.aspx";
		try {
			JSONObject jsonAll = new JSONObject(HttpHelper.post(url));
			version = jsonAll.getString("version");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return version;
	}
	
	public static String getVersionFromApp(Context context) {
		String version = "";
		
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			version = pi.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return version;
	}
	
	public static boolean checkNewVersion(Context context) {
		String version = getVersionFromWeb();
		if(version.equals("")){
			return false;
		}
		if(version.equals(getVersionFromApp(context))) {
			return false;
		}
		
		return true;
	}
	
	public static File getInstallFile() {
		String url = getWebUrl() +  "/app/AALifeNet.apk";
		File file = null;		
		URL myUrl = null;
		try {
			myUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
			InputStream is = conn.getInputStream();
			
			file = new File(Environment.getExternalStorageDirectory() + File.separator + "aalifenet" + File.separator, "AALifeNet.apk");
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			
			fos.close();
			bis.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return file;
	}
	
	public static boolean checkInternetFull(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info == null || !cm.getBackgroundDataSetting()) {
			return false;
		}
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if(netType == ConnectivityManager.TYPE_WIFI) {
			return info.isConnected();
		} else if(netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS && !tm.isNetworkRoaming()) {
			return info.isConnected();
		} else {
			return false;
		}
	}
	
	public static boolean checkInternet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null) {
			return info.isAvailable();
		}
		
		return false;
	}
	
	public static void hideInputMethod(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public static String getWebUrl(){
		//return "http://10.0.2.2:60"
		return "http://www.fxlweb.com";
	}
	
	public static int getCatTypeId(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		return setting.getInt("cattypeid", 0);
	}

	public static void setCatTypeId(Context context, int catTypeId) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putInt("cattypeid", catTypeId).commit();
	}	
	
	public static int findCatType(List<Map<String, String>> list, int catTypeId) {
		int pos = 0;
		for(int i=0; i<list.size(); i++) {
			Map<String, String> map = list.get(i);
			if(map.get("cattypeid").equals(String.valueOf(catTypeId))) {
				pos = i;
			}
		}
		return pos;
	}	
	
	public static String getUserName(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		return setting.getString("username", "");
	}

	public static void setUserName(Context context, String userName) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putString("username", userName).commit();
	}	
	
}
