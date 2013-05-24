package com.aalife.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class DayActivity extends Activity {
	private ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private DayListAdapter daylistAdapter = null;
	private ListView itemlistView = null;
	private SQLiteOpenHelper helper = null;
	private ItemTableAccess itemtableAccess = null;
	private ImageButton addBtn = null;
	private ImageButton calcBtn = null;
	private String date = null;
	private TextView nodataText = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.dayactivity);

		this.helper = new MyDatabaseHelper(this);
		
		String today = UtilityHelper.getDateNow("date");
		int day = Integer.parseInt(UtilityHelper.getDateNow("dd"));
		String week = UtilityHelper.getDateNow("ww");
		
		this.itemtableAccess = new ItemTableAccess(this.helper.getReadableDatabase());
		double price = Double.parseDouble(this.itemtableAccess.findAllTotalByMonth(today));
		
		int warnMonthNumber = UtilityHelper.getWarnMonthNumber(this);
		int weekNumber = warnMonthNumber / 4;
		if(day >= 22) {
			weekNumber = warnMonthNumber;
		} else if(day >= 15) {
			weekNumber *= 3;
		} else if(day > 7) {
			weekNumber *= 2; 
		}
		if(week.equals("1") && UtilityHelper.getOpenFlag(this)) {
			UtilityHelper.setWarnFlag(this, true);
			UtilityHelper.setOpenFlag(this, false);
		}
		if(price >= weekNumber && UtilityHelper.getWarnFlag(this)) {
			Dialog dialog = new AlertDialog.Builder(this)
				.setIcon(R.drawable.ic_info_d)
				.setTitle("提示")
				.setMessage(String.format(getString(R.string.txt_warn_info), UtilityHelper.formatDouble(price, "0.0##"), warnMonthNumber))
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				}).create();
			dialog.show();
			
			UtilityHelper.setWarnFlag(this, false);
		}
		
		Intent intent = super.getIntent();
		date = intent.getStringExtra("date");
		if (date == null) {
			UtilityHelper.setAppDate(this, UtilityHelper.getDateNow("date"));
			date = UtilityHelper.getAppDate(this);
		}
		
		this.addBtn = (ImageButton) super.findViewById(R.id.addbtn);
		this.addBtn.setOnClickListener(new BtnAddClickImpl());		
		this.calcBtn = (ImageButton) super.findViewById(R.id.calcbtn);
		this.calcBtn.setOnClickListener(new BtnCalcClickImpl());
		
		this.itemlistView = (ListView) super.findViewById(R.id.itemlistview);
		this.itemtableAccess = new ItemTableAccess(this.helper.getReadableDatabase());
		this.list = this.itemtableAccess.findAllItemMonth();
		if(!this.list.isEmpty() && !today.equals(this.list.get(0).get("itembuydate"))) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("itembuydate", today);
			map.put("itemprice", "0.0");
			this.list.add(0, map);
		}
		
		this.daylistAdapter = new DayListAdapter(this, this.list);
		this.itemlistView.setAdapter(this.daylistAdapter);

		this.nodataText = (TextView) super.findViewById(R.id.nodatatext);
		if(this.list.isEmpty()) {
			this.nodataText.setVisibility(1);
		}
	}
	
	// 添加按钮
	private class BtnAddClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			UtilityHelper.jumpActivity(DayActivity.this, AddActivity.class, "AddActivity", date, 4);
		}
	}
	
	// 计算器按钮
	private class BtnCalcClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			MyCalcView calcView = new MyCalcView(DayActivity.this);
			
			AlertDialog calcDialog = new AlertDialog.Builder(DayActivity.this).create();
			calcDialog.setIcon(R.drawable.ic_calc_d);
			calcDialog.setTitle("计算器");
			calcDialog.setView(calcView, 0, 0, 0, 0);
			calcDialog.show();
		}		
	}

	// 返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
