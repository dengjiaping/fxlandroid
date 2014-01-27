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
import android.os.StrictMode;
import android.view.Menu;
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
	private TextView tvTitleLogin = null;
	private TextView tvTitleLogout = null;
	private SyncHelper syncHelper = null;
	private Boolean checkSyncWeb = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//网络错误代码
		if(android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		//初始化setting
		sharedHelper = new SharedHelper(this);
		syncHelper = new SyncHelper(MainActivity.this);
				
		//恢复备份数据
		if(!sharedHelper.getRestore()) {
			int result = UtilityHelper.startRestore(this);
			if(result == 1) {
				Toast.makeText(this, R.string.txt_home_restoresuccess, Toast.LENGTH_SHORT).show();
				sharedHelper.setRestore(true);
				sharedHelper.setLocalSync(true);
				sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
			} else if(result == 2) {
				Toast.makeText(this, R.string.txt_home_norestore, Toast.LENGTH_SHORT).show();
				sharedHelper.setRestore(true);
			}
		}
		
		//数据库
		sqlHelper = new DatabaseHelper(this);
		sqlHelper.getWritableDatabase();
		sqlHelper.close();
		
		//当前日期
		curDate = sharedHelper.getDate();
		if(curDate.equals(""))
			curDate = UtilityHelper.getCurDate();
		
		listTotal = (ListView) super.findViewById(R.id.list_total);
						
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
				
		//登录按钮
		tvTitleLogin = (TextView) super.findViewById(R.id.tv_title_login);
		tvTitleLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent);
			}			
		});
		
		//登出按钮
		tvTitleLogout = (TextView) super.findViewById(R.id.tv_title_logout);
		tvTitleLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				sharedHelper.setLogin(false);
				onCreate(null);
			}			
		});

		//显示登录退出按钮
		if(sharedHelper.getLogin()) {
			tvTitleLogin.setVisibility(View.GONE);
			tvTitleLogout.setVisibility(View.VISIBLE);
		}
		
		//设置ListView
		setListData(curDate);
		
		//每日消费
		ImageButton btnTabDay = (ImageButton)super.findViewById(R.id.btn_tab_day);
		btnTabDay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, DayActivity.class);
				startActivity(intent);				
			}
		});
		
		//每月消费
		ImageButton btnTabMonth = (ImageButton)super.findViewById(R.id.btn_tab_month);
		btnTabMonth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, MonthActivity.class);
				startActivity(intent);				
			}
		});

		//检查网络同步
		if(!checkSyncWeb && sharedHelper.getLogin()) {
			new Thread(new Runnable(){
				@Override
				public void run() {
					Boolean result = false;
					try {
						result = syncHelper.checkSyncWeb();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					Bundle bundle = new Bundle();
					bundle.putBoolean("result", result);	
					Message message = new Message();
					message.what = 2;
					message.setData(bundle);
					myHandler.sendMessage(message);
				}
			}).start();
		}
		
		//设置用户文本
		String userName = sharedHelper.getUserName();
		if(userName.equals("")) {
			userName = getString(R.string.txt_home_nouser);
			sharedHelper.setUserName(userName);
		}
		tvLabUserName = (TextView) super.findViewById(R.id.tv_lab_username);
		tvLabUserName.setText(getString(R.string.txt_lab_username) + userName);

		int group = sharedHelper.getGroup();
		tvLabGroup = (TextView) super.findViewById(R.id.tv_lab_group);
		tvLabGroup.setText(getString(R.string.txt_lab_group) + String.valueOf(group));
		
		//设置同步文本
		String syncStatus = sharedHelper.getSyncStatus();
		if(syncStatus.equals("")) {
			syncStatus = getString(R.string.txt_home_nosync);
		}
		sharedHelper.setSyncStatus(syncStatus);
		tvLabStatus = (TextView) super.findViewById(R.id.tv_lab_status);
		tvLabStatus.setText(syncStatus);
		
		//同步按钮
		pbHomeSync = (ProgressBar) super.findViewById(R.id.pb_home_sync);
		btnHomeSync = (ImageButton) super.findViewById(R.id.btn_home_sync);
		btnHomeSync.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				if(sharedHelper.getLocalSync() || sharedHelper.getWebSync() || syncHelper.checkSyncWeb()) {
					pbHomeSync.setVisibility(View.VISIBLE);
					tvLabStatus.setText(getString(R.string.txt_home_syncing));
					
					new Thread(new Runnable(){
						@Override
						public void run() {
							try {
								syncFlag = true;
								syncHelper.Start();
							} catch (Exception e) {
								e.printStackTrace();
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
		
	}
	
	//设置ListView	
	protected void setListData(String date) {
		curDate = date;
		tvDateChoose.setText(UtilityHelper.formatDate(curDate, "y-m-d-w"));
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		onCreate(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
			switch(msg.what) {
			case 1:
				activity.pbHomeSync.setVisibility(View.GONE);
				if(!activity.syncFlag) {
					activity.sharedHelper.setSyncStatus(activity.getString(R.string.txt_home_hassync));
					activity.onCreate(null);
					
					Toast.makeText(activity, activity.getString(R.string.txt_home_syncerror), Toast.LENGTH_SHORT).show();
				} else {					
					String userName = activity.sharedHelper.getUserName();
					activity.tvLabUserName.setText(activity.getString(R.string.txt_lab_username) + userName);
					
					int group = activity.sharedHelper.getGroup();
					activity.tvLabGroup.setText(activity.getString(R.string.txt_lab_group) + String.valueOf(group));
					
					String syncStatus = activity.sharedHelper.getSyncStatus();
					activity.tvLabStatus.setText(syncStatus);
					
					activity.setListData(activity.curDate);
				}
				
				break;
			case 2:
				Boolean result = msg.getData().getBoolean("result");
				if(result) {
					activity.sharedHelper.setSyncStatus(activity.getString(R.string.txt_home_haswebsync));
					activity.sharedHelper.setWebSync(true);
				}
				
				activity.checkSyncWeb = true;
				activity.onCreate(null);
				
				break;
			}
		}			
	};
	
}
