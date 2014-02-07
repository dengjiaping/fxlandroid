package com.aalife.android;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
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
	private Boolean checkSyncWeb = false;
	private Boolean checkVersion = false;

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
		pbHomeSync = (ProgressBar) super.findViewById(R.id.pb_home_sync);
		tvLabStatus = (TextView) super.findViewById(R.id.tv_lab_status);		
		listTotal = (ListView) super.findViewById(R.id.list_total);
				
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
		tempDate = UtilityHelper.getCurDate();
		curDate = sharedHelper.getDate();
		if(curDate.equals("")) {
			curDate = tempDate;
		}
		
		//今日
		todayDate = sharedHelper.getToday();
		if(todayDate.equals("")) {
			sharedHelper.setToday(tempDate);
			todayDate = tempDate;
			curDate = tempDate;	
		}
		if(!todayDate.equals(tempDate)) {
			curDate = tempDate;
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
				
		//登录按钮
		tvTitleLogin = (TextView) super.findViewById(R.id.tv_title_login);
		tvTitleLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent);
			}			
		});
		
		//隐藏登录按钮
		if(sharedHelper.getLogin()) {
			tvTitleLogin.setVisibility(View.GONE);
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
		if(sharedHelper.getSyncing()) {
			pbHomeSync.setVisibility(View.VISIBLE);
			sharedHelper.setSyncStatus(getString(R.string.txt_home_syncing));
		}
		String syncStatus = sharedHelper.getSyncStatus();
		if(syncStatus.equals("")) {
			syncStatus = getString(R.string.txt_home_nosync);
		}
		sharedHelper.setSyncStatus(syncStatus);
		tvLabStatus.setText(syncStatus);
		
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
		
		//检查网络同步
		if(!checkSyncWeb && sharedHelper.getLogin()) {
			if(!UtilityHelper.checkInternet(this)) {
				checkSyncWeb = true;
				Toast.makeText(MainActivity.this, getString(R.string.txt_nointernet), Toast.LENGTH_SHORT).show();
			} else {
				new Thread(new Runnable(){
					@Override
					public void run() {
						boolean result = false;
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
		}
		
		//检查新版本
		if(!checkVersion) {
			if(!UtilityHelper.checkInternet(this)) {
				checkVersion = true;
				Toast.makeText(MainActivity.this, getString(R.string.txt_nointernet), Toast.LENGTH_SHORT).show();
			} else {
				if(UtilityHelper.checkNewVersion(this)) {
					Toast.makeText(MainActivity.this, R.string.txt_newversion, Toast.LENGTH_SHORT).show();
					new Thread(new Runnable(){
						@Override
						public void run() {
							boolean result = false;
							try {
								Uri uri = Uri.fromFile(UtilityHelper.getInstallFile());
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setDataAndType(uri, "application/vnd.android.package-archive");
								startActivity(intent);
								result = true;
							} catch(Exception e) {
								result = false;
								e.printStackTrace();
							}
							
							Bundle bundle = new Bundle();
							bundle.putBoolean("result", result);	
							Message message = new Message();
							message.what = 3;
							message.setData(bundle);
							myHandler.sendMessage(message);
						}
					}).start();
				}
			}
		}
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
		setListData(curDate);
		tvLabStatus.setText(sharedHelper.getSyncStatus());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(syncFlag)
			Toast.makeText(this, getString(R.string.txt_home_syncexit), Toast.LENGTH_SHORT).show();
		else
			this.close();
			
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
			boolean result = false;
			switch(msg.what) {
			case 1:
				activity.pbHomeSync.setVisibility(View.GONE);
				activity.sharedHelper.setSyncing(false);
				
				String userName = activity.sharedHelper.getUserName();
				activity.tvLabUserName.setText(activity.getString(R.string.txt_lab_username) + userName);
			
				int group = activity.sharedHelper.getGroup();
				activity.tvLabGroup.setText(activity.getString(R.string.txt_lab_group) + String.valueOf(group));
				
				if(!activity.syncFlag) {
					String syncStatus = activity.getString(R.string.txt_home_hassync);
					activity.sharedHelper.setSyncStatus(syncStatus);
					activity.tvLabStatus.setText(syncStatus);
					
					Toast.makeText(activity, activity.getString(R.string.txt_home_syncerror), Toast.LENGTH_SHORT).show();
				} else {
					String syncStatus = activity.sharedHelper.getSyncStatus();
					activity.tvLabStatus.setText(syncStatus);
					
					activity.syncFlag = false;
				}
				
				activity.setListData(activity.curDate);				
				break;
			case 2:
				result = msg.getData().getBoolean("result");
				if(result) {
					String syncStatus = activity.getString(R.string.txt_home_haswebsync);
					activity.tvLabStatus.setText(syncStatus);
					activity.sharedHelper.setSyncStatus(syncStatus);
					
					activity.sharedHelper.setWebSync(true);
				} else {
					Toast.makeText(activity, activity.getString(R.string.txt_home_nowebsync), Toast.LENGTH_SHORT).show();
				}
				
				activity.checkSyncWeb = true;				
				break;
			case 3:
				result = msg.getData().getBoolean("result");
				if(!result) {
					Toast.makeText(activity, activity.getString(R.string.txt_updateerror), Toast.LENGTH_SHORT).show();
				}
				
				activity.checkVersion = true;				
				break;
			}
		}			
	};
	
}
