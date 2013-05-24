package com.aalife.android;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class UtilityHelper {

	public static void hideInputMethod(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public static String getDateNow(String format) {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String monthfull = formatFull(c.get(Calendar.MONTH) + 1);
		String dayfull = formatFull(c.get(Calendar.DAY_OF_MONTH));
		String week = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		
		String s = "";
		if(format.equals("yyyy")) {
			s = year;
		} else if(format.equals("MM")) {
			s = month;
		} else if(format.equals("MM")) {
			s = monthfull;
		} else if(format.equals("dd")) {
			s = day;
		} else if(format.equals("ddd")) {
			s = dayfull;
		} else if(format.equals("yyyy-MM")) {
			s = year + "-" + monthfull;
		} else if(format.equals("MM-dd")) {
			s = monthfull + "-" + dayfull;
		} else if(format.equals("date")) {
			s = year + "-" + monthfull + "-" + dayfull;
		} else if(format.equals("ww")) {
			s = week;
		} 
		return s;
	}
	
	public static String formatDate(String date, String format) {
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String monthfull = formatFull(c.get(Calendar.MONTH) + 1);
		String dayfull = formatFull(c.get(Calendar.DAY_OF_MONTH));		
		int week = c.get(Calendar.DAY_OF_WEEK);

		String s = "";
		if(format.equals("yyyy")) {
			s = year;
		} else if(format.equals("MM")) {
			s = month;
		} else if(format.equals("MMM")) {
			s = monthfull;
		} else if(format.equals("dd")) {
			s = day;
		} else if(format.equals("ddd")) {
			s = dayfull;
		} else if(format.equals("yyyy-MM")) {
			s = year + "-" + monthfull;
		} else if(format.equals("MM-dd")) {
			s = monthfull + "-" + dayfull;
		} else if(format.equals("date")) {
			s = year + "-" + monthfull + "-" + dayfull;
		} else if(format.equals("WW")) {
			s = "星期" + formatWeek(week-1);
		} else if(format.equals("ww")) {
			s = "周" + formatWeek(week-1);
		}

		return s;
	}
	
	public static String formatWeek(int x) {
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
	
	public static String formatFull(int x) {
		String s = "" + x;
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}
	
	public static String formatDouble(double d, String format){
		DecimalFormat df = null;
		df = new DecimalFormat(format);
		return df.format(d);
	}
	
	// 编辑用返回方法，传数组和日期和页面
	public static void jumpActivity(Activity from, Class<?> to, String id, String[] items, String date, String page, int mid) {
		AALifeActivity act = (AALifeActivity) from.getParent();
		act.setFocus(mid);
		
		LinearLayout container = (LinearLayout) ((ActivityGroup) from.getParent()).getWindow().findViewById(R.id.contentlayout);
		container.removeAllViews();
		Intent intent = new Intent(from, to);
		intent.putExtra("items", items);
		intent.putExtra("date", date);
		intent.putExtra("page", page);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window subActivity = ((ActivityGroup) from.getParent()).getLocalActivityManager().startActivity(id, intent);
		container.addView(subActivity.getDecorView());
		
		container.setAnimation(AnimationUtils.loadAnimation(from, R.anim.right_in));
	}
	
	// 返回方法传日期
	public static void jumpActivity(Activity from, Class<?> to, String id, String date, int mid) {
		AALifeActivity act = (AALifeActivity) from.getParent();
		act.setFocus(mid);
		
		LinearLayout container = (LinearLayout) ((ActivityGroup) from.getParent()).getWindow().findViewById(R.id.contentlayout);
		container.removeAllViews();
		Intent intent = new Intent(from, to);
		intent.putExtra("date", date);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window subActivity = ((ActivityGroup) from.getParent()).getLocalActivityManager().startActivity(id, intent);
		container.addView(subActivity.getDecorView());
		
		container.setAnimation(AnimationUtils.loadAnimation(from, R.anim.right_in));
	}
	
	// 返回方法传日期和名称
	public static void jumpActivity(Activity from, Class<?> to, String id, String date, String name, int mid) {
		AALifeActivity act = (AALifeActivity) from.getParent();
		act.setFocus(mid);
		
		LinearLayout container = (LinearLayout) ((ActivityGroup) from.getParent()).getWindow().findViewById(R.id.contentlayout);
		container.removeAllViews();
		Intent intent = new Intent(from, to);
		intent.putExtra("date", date);
		intent.putExtra("name", name);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window subActivity = ((ActivityGroup) from.getParent()).getLocalActivityManager().startActivity(id, intent);
		container.addView(subActivity.getDecorView());
		
		container.setAnimation(AnimationUtils.loadAnimation(from, R.anim.right_in));
	}
	
	public static View getListView(View view, MotionEvent ev) {
		ListView list = (ListView) view;
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		int position = list.pointToPosition(x, y);
		int firstVisiblePosition = list.getFirstVisiblePosition();
		return list.getChildAt(position - firstVisiblePosition);
	}
	
	public static int findCatPosition(List<CharSequence> list, String catName) {
		int catpos = 0;
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).equals(catName)) {
				catpos = i;
			}
		}
		return catpos;
	}
	
	public static int getDisplayMetrics(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.densityDpi;
	}
	
	public static int getScreenWidth(Context context) {
		int sw = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
		return sw;
	}
	
	public static int getSubHeight(Context context) {
		int displayMetrics = UtilityHelper.getDisplayMetrics(context);
		int screenWidth = UtilityHelper.getScreenWidth(context);
		int subHeight = 0;
		if (displayMetrics == 320) {
			subHeight = 64;
		} else if (displayMetrics == 240) {
			subHeight = 48;
		} else if (displayMetrics == 160) {
			subHeight = 32;
			if (screenWidth >= 480) {
				subHeight = 45;
			}
		} else {
			subHeight = 24;
		}
		
		return subHeight;
	}
	
	public static void setTheme(Context context, RelativeLayout layout, int theme) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putInt("theme", theme).commit();
		if(theme == 1) {
			layout.setBackgroundResource(R.drawable.ic_bg_1);
		} else {
			layout.setBackgroundResource(R.drawable.ic_bg_2);
		}
	}
	
	public static int getTheme(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		return setting.getInt("theme", 1);
	}
	
	public static String getMonthPrice(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		return setting.getString("monthPrice", "0.0");
	}

	public static void setMonthPrice(Context context) {
		SQLiteOpenHelper helper = new MyDatabaseHelper(context);
		ItemTableAccess itemtableAccess = new ItemTableAccess(helper.getReadableDatabase());
		String today = getDateNow("date");
		String price = itemtableAccess.findAllTotalByMonth(today);
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putString("monthPrice", price).commit();
	}
	
	public static int getWarnNumber(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		return setting.getInt("warnNumber", 30);
	}

	public static void setWarnNumber(Context context, int warnNumber) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putInt("warnNumber", warnNumber).commit();
	}
	
	public static int getWarnMonthNumber(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		return setting.getInt("warnMonthNumber", 1000);
	}

	public static void setWarnMonthNumber(Context context, int warnMonthNumber) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putInt("warnMonthNumber", warnMonthNumber).commit();
	}
	
	public static String getAppDate(Context context) {
		return ((AALifeApplication) context.getApplicationContext()).getAppDate();
	}
	
	public static void setAppDate(Context context, String appDate) {
		((AALifeApplication) context.getApplicationContext()).setAppDate(appDate);
	}

	public static String getCatName(Context context) {
		return ((AALifeApplication) context.getApplicationContext()).getCatName();
	}
	
	public static void setCatName(Context context, String catName) {
		((AALifeApplication) context.getApplicationContext()).setCatName(catName);
	}	
	
	public static int getGroupId(Context context) {
		return ((AALifeApplication) context.getApplicationContext()).getGroupId();
	}
	
	public static void setGroupId(Context context, int groupId) {
		((AALifeApplication) context.getApplicationContext()).setGroupId(groupId);
	}
	
	public static boolean getWarnFlag(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		return setting.getBoolean("warn-flag", true);
	}
	
	public static void setWarnFlag(Context context, boolean warnFlag) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putBoolean("warn-flag", warnFlag).commit();
	}	
	
	public static boolean getOpenFlag(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		return setting.getBoolean("open-flag", true);
	}
	
	public static void setOpenFlag(Context context, boolean openFlag) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putBoolean("open-flag", openFlag).commit();
	}	
	
	public static boolean getBackupFlag(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		return setting.getBoolean("backup-flag", false);
	}
	
	public static void setBackupFlag(Context context, boolean backupFlag) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putBoolean("backup-flag", backupFlag).commit();
	}

	public static boolean getTongJiFlag(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		return setting.getBoolean("tongji-flag", true);
	}
	
	public static void setTongJiFlag(Context context, boolean tongjiFlag) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putBoolean("tongji-flag", tongjiFlag).commit();
	}

	public static boolean getRankFlag(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		return setting.getBoolean("rank-flag", true);
	}
	
	public static void setRankFlag(Context context, boolean rankFlag) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putBoolean("rank-flag", rankFlag).commit();
	}
	
	public static String getSharedDate(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		String s = setting.getString("date", "");
		if(s.equals("")) {
			s = getDateNow("date");
			setSharedDate(context, s);
		}
		return s;
	}

	public static void setSharedDate(Context context, String date) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putString("date", date).commit();
	}
	
	public static long getAppInstallTime(Context context) {
		long l = 0;
		try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.aalife.android", 0);
			String s = info.sourceDir;
			l = new File(s).lastModified();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
		return l; 
	}
	
	public static long getSharedInstallTime(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		long l = setting.getLong("time", 0);
		if(l == 0) {
			l = getAppInstallTime(context);
			setSharedInstallTime(context, l);
		}
		return l;
	}

	public static void setSharedInstallTime(Context context, long time) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putLong("time", time).commit();
	}
	
	public static String getPassword(Context context) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		return setting.getString("password", "");
	}

	public static void setPassword(Context context, String pass) {
		SharedPreferences setting = context.getSharedPreferences("setting", 0);
		setting.edit().putString("password", pass).commit();
	}
}
