package com.aalife.android;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;


public class UtilityHelper {
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
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
		else if(type.equals("y"))
			return year;
		else if(type.equals("y-m"))
			return year + "-" + month;
		else if(type.equals("y2-m2"))
			return year + "年" + month + "月";
		else if(type.equals("ys-m"))
			return year.substring(2) + "-" + month;
		else if(type.equals("ys-m-d"))
			return year.substring(2) + "-" + month + "-" + day;
		else if(type.equals("m-d"))
			return month + "-" + day;
		else if(type.equals("m"))
			return month + "月";
		else if(type.equals("m-d-w"))
			return month + "-" + day + " " + week;
		else if(type.equals("y-m-d-w"))
			return year + "-" + month + "-" + day + "  " + week;
		else if(type.equals("y-m-d-w2"))
			return year + "-" + month + "-" + day + "  周" + week;
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
				ItemTableAccess itemAccess = new ItemTableAccess(new DatabaseHelper(context).getReadableDatabase());
				Boolean flag = itemAccess.restoreDataBase(list);
				itemAccess.close();
				if(flag) 
					result = 1;
				else 
					result = 0;
			} else {
				result = 2;
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	//备份数据
	public static boolean startBackup(Context context) {
		ItemTableAccess itemAccess = new ItemTableAccess(new DatabaseHelper(context).getReadableDatabase());
		List<CharSequence> itemList = itemAccess.bakDataBase();
		itemAccess.close();
		
		FileOutputStream output = null;
		try {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + "aalife.bak");
				if(!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				
				output = new FileOutputStream(file, false);
			} else {
				output = context.openFileOutput("aalife.bak", Activity.MODE_PRIVATE);
			}

			PrintStream out = new PrintStream(output);
			Iterator<CharSequence> it = itemList.iterator();
			while(it.hasNext()) {
				out.println(it.next());
			}

			out.close();
			output.close();
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}	

		return true;
	}

	//获取用户
	public static String[] getUser(int userId) {
		String[] result = new String[] { "", "", "", "", "" };
		String url = WEBURL +  "/AALifeWeb/GetUser.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", String.valueOf(userId)));
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result[0] = jsonObject.getString("username");
				result[1] = jsonObject.getString("userpass");
				result[2] = jsonObject.getString("nickname");
				result[3] = jsonObject.getString("userimage");
				result[4] = jsonObject.getString("useremail");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	//修改资料方法
	public static int editUser(int userId, String userName, String userPass, String userNickName, String userImage, String userEmail) {
		int result = 0;
		String url = WEBURL +  "/AALifeWeb/SyncUserEdit.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", userName));
		params.add(new BasicNameValuePair("userpass", userPass));
		params.add(new BasicNameValuePair("nickname", userNickName));
		params.add(new BasicNameValuePair("userimage", userImage));
		params.add(new BasicNameValuePair("useremail", userEmail));
		params.add(new BasicNameValuePair("userid", String.valueOf(userId)));
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result = jsonObject.getInt("result");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return result;
	}
	
	//登录用户
	public static String[] loginUser(String userName, String userPass) {
		String[] result = new String[] { "0", "0", "0", "0", "", "", "", "" };
		String url = WEBURL +  "/AALifeWeb/SyncLogin.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", userName));
		params.add(new BasicNameValuePair("userpass", userPass));
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result[0] = jsonObject.getString("group");
				result[1] = jsonObject.getString("userid");
				result[2] = jsonObject.getString("hassync");
				result[3] = "0";
				result[4] = jsonObject.getString("nickname");
				result[5] = jsonObject.getString("useremail");
				result[6] = jsonObject.getString("userpass");
				result[7] = jsonObject.getString("userimage");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
		
	//注册用户
	public static String[] addUser(String userName, String userPass) {
		String[] result = new String[] { "0", "0", "0", "0", "", "", "", "" };
		String url = WEBURL +  "/AALifeWeb/SyncUser.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", userName));
		params.add(new BasicNameValuePair("userpass", userPass));
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result[0] = jsonObject.getString("group");
				result[1] = jsonObject.getString("userid");
				result[2] = "0";
				result[3] = jsonObject.getString("repeat");
				result[4] = "";
				result[5] = "";
				result[6] = "";
				result[7] = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return result;
	}
	
	//登录后保存头像
	public static boolean loadBitmap(Context context, String fileName) {
		String url = WEBURL + "/Images/Users/" + fileName;
		FileOutputStream output = null;
		try {
			URL myUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
			if(conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + fileName);
					if(!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					
					output = new FileOutputStream(file, false);
				} else {
					output = context.openFileOutput(fileName, Activity.MODE_PRIVATE);
				}
				
		        byte[] buffer = new byte[1024];
		        int len = -1;
		        while((len = is.read(buffer)) != -1 ){
		            output.write(buffer, 0, len);
		        }

				output.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//选择头像选择图片保存
	public static boolean saveBitmap(Context context, Bitmap bitmap, String fileName) {
		FileOutputStream output = null;
		try {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + fileName);
				if(!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				
				output = new FileOutputStream(file, false);
			} else {
				output = context.openFileOutput(fileName, Activity.MODE_PRIVATE);
			}
			
			if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, output))
				output.flush();

			output.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//取文件扩展名
	public static String getFileExtName(String fileName) {
		int start = fileName.lastIndexOf(".");
		return fileName.substring(start);
	}
	
	//显示用户头像
	public static Bitmap getUserImage(Context context, String fileName) {
		File file = null;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + fileName);
		} else {
			file = context.getFileStreamPath(fileName);
		}
		
		return BitmapFactory.decodeFile(file.getPath());
	}

	//改变头像大小
	public static Bitmap resizeBitmap(Bitmap bitmap, int newSize) {
		float width = bitmap.getWidth();
		float height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = (float)newSize / width;
		float scaleHeight = (float)newSize / height;
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bitmap, 0, 0, (int)width, (int)height, matrix, true);
	}
	
	//同步用户头像
	public static boolean postBitmap(Context context, String fileName) {
        String url = WEBURL + "/AALifeWeb/SyncUserImage.ashx";
		try {
			String lineEnd = "\r\n";  
	        String twoHyphens = "--";  
	        String boundary = "*****";
	        
			URL myUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();

			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

	        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
	        dos.writeBytes(twoHyphens + boundary + lineEnd);
	        dos.writeBytes("Content-Disposition:form-data; " + "name=\"file1\"; filename=\"" + fileName + "\"" + lineEnd);
	        dos.writeBytes(lineEnd);  
	        
	        File file = null;
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + fileName);
			} else {
				file = context.getFileStreamPath(fileName);
			}
			
	        FileInputStream fis = new FileInputStream(file.getPath());
	        byte[] buffer = new byte[1024];
	        int len = -1;
	        while((len = fis.read(buffer)) != -1) {
	            dos.write(buffer, 0, len);
	        }
	        dos.writeBytes(lineEnd);
	        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

	        fis.close();
	        dos.flush();
            dos.close();
            
            InputStream is = conn.getInputStream();
            int ch;
            StringBuffer sb = new StringBuffer();
            while ((ch = is.read()) != -1) {
                sb.append((char) ch);
            }
            
            return sb.toString().equals("1");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//修改用户头像
	public static int editUserImage(String userImage, String userId) {
		int result = 0;
		String url = WEBURL +  "/AALifeWeb/SyncUserImage.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userimage", userImage));
		params.add(new BasicNameValuePair("userid", userId));
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result = jsonObject.getInt("result");
			}
		} catch (Exception e) {
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
		if(info != null && info.isConnected()) {
			if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE || info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA)
				return false;
			else
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
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url));
			if(jsonObject.length() > 0) {
				version = jsonObject.getString("version");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return version;
	}
	
	//取安装文件
	public static File getInstallFile(Context context) throws Exception {
		String url = WEBURL +  "/app/AALifeNew.apk";
		File file = null;
		try {
			URL myUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
			InputStream is = conn.getInputStream();
						
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "aalife" + File.separator + "AALifeNew.apk");
			} else {
				file = context.getFileStreamPath("AALifeNew.apk");
			}
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