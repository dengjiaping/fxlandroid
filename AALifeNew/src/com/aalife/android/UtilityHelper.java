package com.aalife.android;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;


public class UtilityHelper {
	//private static final String WEBURL = "http://192.168.0.1:81";
	//private static final String WEBURL = "http://10.0.2.2:81";
	private static final String WEBURL = "http://www.fxlweb.com";
	
	public UtilityHelper() {
		
	}

	//取当前日期
	public static String getCurDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar c = Calendar.getInstance();
		return sdf.format(c.getTime());
	}

	//取同步日期
	public static String getSyncDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:ss", Locale.CHINA);
		Calendar c = Calendar.getInstance();
		return sdf.format(c.getTime());
	}
	
	//取每日消费导航日期
	public static String getNavDate(String date, int value, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date d = new Date();
		try {
			d = sdf.parse(date);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		if(type.equals("d"))
			c.add(Calendar.DATE, value);
		else if(type.equals("m"))
			c.add(Calendar.MONTH, value);
		
		return sdf.format(c.getTime());
	}
	
	//格式化导航日期
	public static String formatDate(String date, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date d = new Date();
		try {
			d = sdf.parse(date);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(d);

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = formatFull(c.get(Calendar.MONTH) + 1);
		String day = formatFull(c.get(Calendar.DAY_OF_MONTH));	
		String week = formatWeek(c.get(Calendar.DAY_OF_WEEK) - 1);
		
		if(type.equals("m-d-w"))
			return month + "-" + day + "  " + week;
		else if(type.equals("y-m"))
			return year + "-" + month;
		else if(type.equals("m-d"))
			return month + "-" + day;
		else if(type.equals("y-m-d-w"))
			return year + "-" + month + "-" + day + "  " + week;
		else
			return year + "-" + month + "-" + day;
	}
	
	//日期补0
	protected static String formatFull(int x) {
		String s = "" + x;
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

	//取星期
	protected static String formatWeek(int x) {
		String s = "";
		switch(x) {
		case 0:
			s = "日";
			break;
		case 1:
			s = "一";
			break;
		case 2:
			s = "二";
			break;
		case 3:
			s = "三";
			break;
		case 4:
			s = "四";
			break;
		case 5:
			s = "五";
			break;
		case 6:
			s = "六";
			break;
		}
		return s;
	}

	//创建随机用户名
	public static String createUserName() {
		Random r = new Random();
		int max = 99999;
		int min = 10000;
		return "aa" + String.valueOf(r.nextInt(max)%(max-min+1) + min);
	}
	
	//恢复数据 0失败1成功2无数据
	public static int startRestore(Context context) {
		int result = 0;
		FileInputStream input = null;
		File file = null;
		try {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + "aalife.bak");
			} else {
				file = context.getFileStreamPath("aalife.bak");
			}
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if(!file.exists()) {
				file.createNewFile();
			}
			
			input = new FileInputStream(file);			
			List<CharSequence> list = new ArrayList<CharSequence>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String str = "";
			while((str = reader.readLine()) != null) {
				list.add(str);
			}
			reader.close();
			input.close();
			
			if(list.size() > 0) {
				SQLiteOpenHelper sqlHelper = new DatabaseHelper(context);
				sqlHelper.close();
				ItemTableAccess itemAccess = new ItemTableAccess(sqlHelper.getWritableDatabase());
				Boolean flag = itemAccess.restoreItemTable(list);
				itemAccess.close();
				if(flag) result = 1;
				else result = 0;
			} else {
				result = 2;
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	//格式化金额
	public static String formatDouble(double d, String format){
		DecimalFormat df = null;
		df = new DecimalFormat(format);
		return df.format(d);
	}
	
	//检查网络
	public static boolean checkInternet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null) {
			return info.isAvailable();
		}
		
		return false;
	}

	//检查新版本
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
	
	//取APP版本
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
	
	//取网络版本
	public static String getVersionFromWeb() {
		String version = "";
		String url = WEBURL + "/AALifeWeb/GetWebVersion.aspx";
		try {
			JSONObject jsonAll = new JSONObject(HttpHelper.post(url));
			version = jsonAll.getString("version");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return version;
	}
	
	//取安装文件
	public static File getInstallFile() throws Exception {
		String url = WEBURL +  "/app/AALifeNew.apk";
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
			throw new Exception();
		}
		
		return file;
	}
}
