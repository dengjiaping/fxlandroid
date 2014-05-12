package com.aalife.android.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class MyHelper {
	
	public MyHelper() {
		
	}
	
	public static String getVersionFromWeb() {
		String version = "";
		String url = getWebUrl() + "/AALifeWeb/GetVersion.aspx";
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url));
			if(jsonObject.length() > 0) {
				version = jsonObject.getString("version");
			}
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
		String versionWeb = getVersionFromWeb();
		String versionApp = getVersionFromApp(context);

		if(versionWeb.equals("") || versionApp.equals(""))
			return false;
		
		if(!versionWeb.equals(versionApp))
			return true;
		else
			return false;
	}
	
	public static File getInstallFile() {
		String url = getWebUrl() +  "/app/AALifeWeb.apk";
		File file = null;	
		try {
			URL myUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
			InputStream is = conn.getInputStream();
			
			file = new File(Environment.getExternalStorageDirectory() + File.separator + "aalifeweb" + File.separator, "AALifeWeb.apk");
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
	
	public static boolean checkInternet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null) {
			return info.isAvailable();
		}
		
		return false;
	}
	
	public static String getWebUrl(){
		//return "http://10.0.2.2:60"
		return "http://www.fxlweb.com";
	}
	
}
