package com.aalife.android;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MonthTotalActivity extends Activity {
	private ImageButton backBtn = null;
	private ImageButton calcBtn = null;
	private TextView monthtotalpriceText = null;
	private SQLiteOpenHelper helper = null;
	private ItemTableAccess itemtableAccess = null;
	private TextView nodataText = null;

	private String allPrice = "";
	
	private List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
	private ExpandableListView explistView = null;
	private ExpandableListAdapter expAdapter = null;
	
	private ProgressDialog progressDialog = null;
	private MyHandler myHandler = new MyHandler(this);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.monthtotalactivity);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在打开...");
		progressDialog.show();		
		
		this.helper = new MyDatabaseHelper(this);

		this.backBtn = (ImageButton) super.findViewById(R.id.backbtn);
		this.backBtn.setOnClickListener(new BtnMonthBackClickImpl());

		this.calcBtn = (ImageButton) super.findViewById(R.id.calcbtn);
		this.calcBtn.setOnClickListener(new BtnCalcClickImpl());
		
		this.monthtotalpriceText = (TextView) super.findViewById(R.id.monthtotalpricetext);
		this.monthtotalpriceText.setText("");
		
		this.explistView = (ExpandableListView) super.findViewById(R.id.explistview);
		this.nodataText = (TextView) super.findViewById(R.id.nodatatext);
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				MonthTotalActivity.this.itemtableAccess = new ItemTableAccess(MonthTotalActivity.this.helper.getReadableDatabase());
				MonthTotalActivity.this.allPrice = MonthTotalActivity.this.itemtableAccess.findAll();
				
				MonthTotalActivity.this.itemtableAccess = new ItemTableAccess(MonthTotalActivity.this.helper.getReadableDatabase());
				MonthTotalActivity.this.groups = MonthTotalActivity.this.itemtableAccess.findAllByMonthTotal();

				MonthTotalActivity.this.expAdapter = new MyExpandableListAdapter(MonthTotalActivity.this, MonthTotalActivity.this.groups);
				
				Message message = new Message();
				message.what = 1;
				myHandler.sendMessage(message);
			}
		}).start();		
	}

	// 返回按钮
	private class BtnMonthBackClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			UtilityHelper.jumpActivity(MonthTotalActivity.this, DayActivity.class, "DayActivity", null, 0);
		}
	}
	
	// 计算器按钮
	private class BtnCalcClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			MyCalcView calcView = new MyCalcView(MonthTotalActivity.this);
			
			AlertDialog calcDialog = new AlertDialog.Builder(MonthTotalActivity.this).create();
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
			UtilityHelper.jumpActivity(MonthTotalActivity.this, DayActivity.class, "DayActivity", null, 0);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private class OnMonthGroupExpand implements OnGroupExpandListener {
		@Override
		public void onGroupExpand(int groupPosition) {
			for(int i=0; i<MonthTotalActivity.this.groups.size(); i++) {
				if(groupPosition != i) {
					MonthTotalActivity.this.explistView.collapseGroup(i);
				}
			}
			UtilityHelper.setGroupId(MonthTotalActivity.this, groupPosition);
		}
	}
	
	static class MyHandler extends Handler {
		WeakReference<MonthTotalActivity> myActivity = null;
		MyHandler(MonthTotalActivity activity) {
			myActivity = new WeakReference<MonthTotalActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			MonthTotalActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:		
				activity.monthtotalpriceText.setText("☆☆  总计：￥ " + activity.allPrice + "  ☆☆");
				
				activity.explistView.setAdapter(activity.expAdapter);
				activity.explistView.setOnGroupExpandListener(activity.new OnMonthGroupExpand());
						
				int groupId = UtilityHelper.getGroupId(activity);
				if(groupId != -1) {
					activity.explistView.expandGroup(groupId);
				}
				
				if (activity.groups.isEmpty()) {
					activity.nodataText.setVisibility(1);
				}
				
				activity.progressDialog.dismiss();
				
				break;
			}
		}			
	};	
}
