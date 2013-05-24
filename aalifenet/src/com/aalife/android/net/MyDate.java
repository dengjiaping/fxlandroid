package com.aalife.android.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyDate {
	private Calendar c = null;
	
	public MyDate() {
		c = Calendar.getInstance();
	}
	
	public String getDate() {
		Date d = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		return sdf.format(d);
	}
	
	public String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
		return sdf.format(new Date());
	}
	
	public void setDate(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date date = null;
		try {
			date = sdf.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
	}
	
	public int getYear() {
		return c.get(Calendar.YEAR);
	}
	
	public int getMonth() {
		return c.get(Calendar.MONTH);
	}
	
	public int getDay() {
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	public String getWeekStr() {
		return "周" + formatWeek(c.get(Calendar.DAY_OF_WEEK)-1);
	}
	
	public void getNextDay() {
		c.add(Calendar.DATE, -1);
	}
	
	public void getPreviousDay() {
		c.add(Calendar.DATE, +1);
	}
	
	public void getNextMonth() {
		c.add(Calendar.MONTH, +1);
	}
	
	public void getPreviousMonth() {
		c.add(Calendar.MONTH, -1);
	}
	
	public String formatWeek(int x) {
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
	
}
