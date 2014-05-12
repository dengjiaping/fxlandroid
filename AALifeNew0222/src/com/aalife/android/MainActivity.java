package com.aalife.android;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private TextView tvLabUserName = null;
	private TextView tvLabGroup = null;
	private TextView tvLabStatus = null;
	private ImageButton btnHomeSync = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ProgressBar pbHomeSync = null;
	private SharedHelper sharedHelper = null;
	private MyHandler myHandler = new MyHandler(this);
	private ImageButton btnTitleAdd = null;
	private Boolean syncFlag = false;
	private ItemTableAccess itemAccess = null;
	private ListView listTotal = null;
	private List<Map<String, String>> list = null;
	private SimpleAdapter adapter = null;
	private TextView tvDateChoose = null;
	private String curDate = null;
	private String todayDate = null;
	private String tempDate = null;
	private TextView tvTitleLogin = null;
	private SyncHelper syncHelper = null;
	private TextPaint textPaint = null;
	private ImageButton btnTabDay = null;
	private ImageButton btnTabMonth = null;
	private ImageButton btnTabRank = null;
	private ImageButton btnTabAnalyze = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		//标题变粗
		TextView tvTitleWelcome = (TextView) super.findViewById(R.id.tv_title_welcome);
		textPaint = tvTitleWelcome.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvTitleTotal = (TextView) super.findViewById(R.id.tv_title_total);
		textPaint = tvTitleTotal.getPaint();
		textPaint.setFakeBoldText(true);
		
		//初始化
		sharedHelper = new SharedHelper(this);
		syncHelper = new SyncHelper(this);
		pbHomeSync = (ProgressBar) super.findViewById(R.id.pb_home_sync);
		tvLabStatus = (TextView) super.findViewById(R.id.tv_lab_status);		
		listTotal = (ListView) super.findViewById(R.id.list_total);
				
		//数据库
		sqlHelper = new DatabaseHelper(this);
		sqlHelper.getWritableDatabase();
		sqlHelper.close();
		
		//检查网络同步
		if(UtilityHelper.checkInternet(this)) {
			if(sharedHelper.getLogin() && !sharedHelper.getSyncing()) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						int result = 0;
						try {
							result = syncHelper.checkSyncWeb();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						Bundle bundle = new Bundle();
						bundle.putInt("result", result);	
						Message message = new Message();
						message.what = 2;
						message.setData(bundle);
						myHandler.sendMessage(message);
					}
				}).start();
			}
		}
				
		//当前日期
		tempDate = UtilityHelper.getCurDate();
		curDate = sharedHelper.getDate();
		if(curDate.equals("")) {
			curDate = tempDate;
			sharedHelper.setDate(curDate);
		}		
		//今日
		todayDate = sharedHelper.getToday();
		if(todayDate.equals("")) {
			todayDate = tempDate;
			curDate = tempDate;	
			sharedHelper.setToday(todayDate);
		}
		if(!todayDate.equals(tempDate)) {
			curDate = tempDate;
			sharedHelper.setDate(curDate);
			sharedHelper.setToday(curDate);
		}
						
		//首页日期
		tvDateChoose = (TextView) super.findViewById(R.id.tv_date_choose);
		tvDateChoose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String[] array = curDate.split("-");
				DatePickerDialog dateDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day) {
						String date = UtilityHelper.formatDate(year + "-" + (month + 1) + "-" + day, "");
						setListData(date);
					}					
				}, Integer.parseInt(array[0]), Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
				dateDialog.show();
			}
		});
				
		//每日消费菜单
		btnTabDay = (ImageButton)super.findViewById(R.id.btn_tab_day);
		btnTabDay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, DayActivity.class);
				startActivity(intent);				
			}
		});
		
		//每月消费菜单
		btnTabMonth = (ImageButton)super.findViewById(R.id.btn_tab_month);
		btnTabMonth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, MonthActivity.class);
				startActivity(intent);				
			}
		});
		
		//消费排行菜单
		btnTabRank = (ImageButton)super.findViewById(R.id.btn_tab_rank);
		btnTabRank.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, RankActivity.class);
				startActivity(intent);				
			}
		});

		//消费分析菜单
		btnTabAnalyze = (ImageButton)super.findViewById(R.id.btn_tab_analyze);
		btnTabAnalyze.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, AnalyzeRecommendActivity.class);
				startActivity(intent);				
			}
		});
		
		//设置用户文本
		tvLabUserName = (TextView) super.findViewById(R.id.tv_lab_username);
		tvLabGroup = (TextView) super.findViewById(R.id.tv_lab_group);
		String userName = sharedHelper.getUserName();
		if(userName.equals("")) {
			userName = getString(R.string.txt_home_nouser);
			sharedHelper.setUserName(userName);
		}
		
		//设置同步文本
		String syncStatus = sharedHelper.getSyncStatus();
		if(syncStatus.equals("")) {
			syncStatus = getString(R.string.txt_home_nosync);
			sharedHelper.setSyncStatus(syncStatus);
		}
		
		//同步按钮
		btnHomeSync = (ImageButton) super.findViewById(R.id.btn_home_sync);
		btnHomeSync.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				if(sharedHelper.getLocalSync() || sharedHelper.getWebSync()) {
					pbHomeSync.setVisibility(View.VISIBLE);
					tvLabStatus.setText(getString(R.string.txt_home_syncing));
					sharedHelper.setSyncing(true);
					
					new Thread(new Runnable(){
						@Override
						public void run() {
							try {
								syncFlag = true;
								syncHelper.Start();
							} catch (Exception e) {
								syncFlag = false;
							}
							
							Message message = new Message();
							message.what = 1;
							myHandler.sendMessage(message);
						}
					}).start();
				} else {
					Toast.makeText(MainActivity.this, getString(R.string.txt_home_nosync), Toast.LENGTH_SHORT).show();
				}
			}
		});

		//添加按钮
		btnTitleAdd = (ImageButton) super.findViewById(R.id.btn_title_add);
		btnTitleAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, AddActivity.class);
				startActivity(intent);
			}			
		});
				
		//登录按钮
		tvTitleLogin = (TextView) super.findViewById(R.id.tv_title_login);
		tvTitleLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent);
			}			
		});
	}
	
	//设置ListView	
	protected void setListData(String date) {
		curDate = date;
		tvDateChoose.setText(UtilityHelper.formatDate(curDate, "y-m-d-w2"));
		sharedHelper.setDate(date);
		
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		list = itemAccess.findHomeTotalByDate(curDate);
		itemAccess.close();
		adapter = new SimpleAdapter(this, list, R.layout.list_total, new String[] { "curlabel", "curprice", "prevlabel", "prevprice" }, new int[] { R.id.tv_curdate_label, R.id.tv_curdate_price, R.id.tv_prevdate_label, R.id.tv_prevdate_price });
		listTotal.setAdapter(adapter);
				
		//System.out.println(date);
	}
	
	//关闭this
	protected void close() {
		this.finish();
	}
	
	// 返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(syncFlag) {
				Toast.makeText(this, getString(R.string.txt_home_syncexit), Toast.LENGTH_SHORT).show();
				return false;
			} else {
				if(UtilityHelper.startBackup(this)) {
					this.close();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_exits:
			if(UtilityHelper.startBackup(this)) {
				this.close();
			}
			
			break;
		case R.id.action_settings:
			Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intent);
			
			break;
		}
		
		return false;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		setListData(sharedHelper.getDate());
		
		tvLabUserName.setText(getString(R.string.txt_lab_username) + sharedHelper.getUserName());
		tvLabGroup.setText(getString(R.string.txt_lab_group) + sharedHelper.getGroup());
		
		//隐藏登录按钮
		if(sharedHelper.getLogin()) {
			tvTitleLogin.setVisibility(View.GONE);
		}

		//设置同步状态文本
		if(sharedHelper.getSyncing()) {
			pbHomeSync.setVisibility(View.VISIBLE);
			sharedHelper.setSyncStatus(getString(R.string.txt_home_syncing));
		}
		
		tvLabStatus.setText(sharedHelper.getSyncStatus());
	}
	
	//多线程处理
	static class MyHandler extends Handler {
		WeakReference<MainActivity> myActivity = null;
		MyHandler(MainActivity activity) {
			myActivity = new WeakReference<MainActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			MainActivity activity = myActivity.get();
			String syncStatus = "";
			switch(msg.what) {
			case 1:
				activity.pbHomeSync.setVisibility(View.GONE);
				activity.sharedHelper.setSyncing(false);
				
				String userName = activity.sharedHelper.getUserName();
				activity.tvLabUserName.setText(activity.getString(R.string.txt_lab_username) + userName);
			
				int group = activity.sharedHelper.getGroup();
				activity.tvLabGroup.setText(activity.getString(R.string.txt_lab_group) + String.valueOf(group));
				
				if(!activity.syncFlag) {					
					Toast.makeText(activity, activity.getString(R.string.txt_home_syncerror), Toast.LENGTH_SHORT).show();
				} else {					
					activity.syncFlag = false;
				}

				syncStatus = activity.sharedHelper.getSyncStatus();
				activity.tvLabStatus.setText(syncStatus);
				activity.setListData(activity.curDate);			
				
				break;
			case 2:
				int res = msg.getData().getInt("result");
				if(res == 1) {
					syncStatus = activity.getString(R.string.txt_home_haswebsync);
					activity.sharedHelper.setSyncStatus(syncStatus);
					activity.sharedHelper.setWebSync(true);
					activity.tvLabStatus.setText(syncStatus);
				}
							
				break;
			}
		}			
	};
	
}
