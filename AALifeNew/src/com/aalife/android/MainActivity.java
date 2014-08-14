package com.aalife.android;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private TextView tvLabUserName = null;
	private TextView tvLabNickName = null;
	private TextView tvLabStatus = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ProgressBar pbHomeSync = null;
	private SharedHelper sharedHelper = null;
	private MyHandler myHandler = new MyHandler(this);
	private ItemTableAccess itemAccess = null;
	private ListView listTotal = null;
	private List<Map<String, String>> list = null;
	private SimpleAdapter adapter = null;
	private TextView tvDateChoose = null;
	private String curDate = "";
	private String fromDate = "";
	private TextView tvTitleLogin = null;
	private TextView tvTitleUserEdit = null;
	private SyncHelper syncHelper = null;
	private ImageView ivUserImage = null;
	private final int FIRST_REQUEST_CODE = 1;
	private Runnable runnable = null;
	private final int DELAY_TIME = 5 * 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		//虚拟按键菜单
		try {
			getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//标题变粗
		TextPaint textPaint = null;
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
		ivUserImage = (ImageView) super.findViewById(R.id.iv_userimage);
	
		//数据库
		sqlHelper = new DatabaseHelper(this);

		//恢复备份数据
		if(!sharedHelper.getRestore()) {
			UtilityHelper.copyBackup(MainActivity.this);
			
			Dialog dialog = new AlertDialog.Builder(this)
			    .setCancelable(false)
				.setTitle(R.string.txt_tips)
				.setMessage(R.string.txt_restore)
				.setPositiveButton(R.string.txt_yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						int result = UtilityHelper.startRestore(MainActivity.this);
						if(result == 1) {
							itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
							boolean bool = itemAccess.hasSyncItem();
							itemAccess.close();
							if(bool) {
								sharedHelper.setLocalSync(true);
								sharedHelper.setSyncStatus(getString(R.string.txt_home_loginsync));
								tvLabStatus.setText(getString(R.string.txt_home_loginsync));
							}

							sharedHelper.setHasRestore(true);
							MainActivity.this.onResume();
						}
					}
				}).setNegativeButton(R.string.txt_no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						sharedHelper.setHasRestore(false);
						sharedHelper.setSyncStatus(getString(R.string.txt_home_loginsyncweb));
						tvLabStatus.setText(getString(R.string.txt_home_loginsyncweb));
						
						dialog.cancel();
					}
				}).create();
			dialog.show();			

			sharedHelper.setRestore(true);
		}
				
		//检查网络同步
		if(sharedHelper.getLogin() && !sharedHelper.getSyncing()) {
			new Thread(new Runnable(){
				@Override
				public void run() {
					int result = 0;
					result = syncHelper.checkSyncWeb();
					
					Bundle bundle = new Bundle();
					bundle.putInt("result", result);	
					Message message = new Message();
					message.what = 2;
					message.setData(bundle);
					myHandler.sendMessage(message);
				}
			}).start();
		}
				
		//当前日期
		curDate = UtilityHelper.getCurDate();
						
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
		
		//首页统计点击
		listTotal.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 0) {
					Intent intent = new Intent(MainActivity.this, DayDetailActivity.class);
					intent.putExtra("date", fromDate);
					startActivityForResult(intent, FIRST_REQUEST_CODE);
				}
			}			
		});
				
		//每日消费菜单
		ImageButton btnTabDay = (ImageButton)super.findViewById(R.id.btn_tab_day);
		btnTabDay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, DayActivity.class);
				startActivity(intent);				
			}
		});
		
		//每月消费菜单
		ImageButton btnTabMonth = (ImageButton)super.findViewById(R.id.btn_tab_month);
		btnTabMonth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, MonthActivity.class);
				startActivity(intent);				
			}
		});
		
		//消费排行菜单
		ImageButton btnTabRank = (ImageButton)super.findViewById(R.id.btn_tab_rank);
		btnTabRank.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, RankActivity.class);
				startActivity(intent);				
			}
		});

		//消费分析菜单
		ImageButton btnTabAnalyze = (ImageButton)super.findViewById(R.id.btn_tab_analyze);
		btnTabAnalyze.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, AnalyzeActivity.class);
				startActivity(intent);				
			}
		});
		
		//设置用户文本
		tvLabUserName = (TextView) super.findViewById(R.id.tv_lab_username);
		tvLabNickName = (TextView) super.findViewById(R.id.tv_lab_nickname);

		//设置同步文本
		String syncStatus = sharedHelper.getSyncStatus();
		if(syncStatus.equals("")) {
			syncStatus = getString(R.string.txt_home_nosync);
			sharedHelper.setSyncStatus(syncStatus);
		}
		
		//同步按钮
		ImageButton btnHomeSync = (ImageButton) super.findViewById(R.id.btn_home_sync);
		btnHomeSync.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				if(sharedHelper.getLocalSync() || sharedHelper.getWebSync()) {
					pbHomeSync.setVisibility(View.VISIBLE);
					tvLabStatus.setText(getString(R.string.txt_home_syncing));

					new Thread(new Runnable(){
						@Override
						public void run() {
							try {
								if(sharedHelper.getWebSync()) {
								    myHandler.postDelayed(runnable, DELAY_TIME);
								}
								
								sharedHelper.setSyncing(true);
								syncHelper.Start();
							} catch (Exception e) {
								sharedHelper.setSyncing(false);
								e.printStackTrace();
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
		ImageButton btnTitleAdd = (ImageButton) super.findViewById(R.id.btn_title_add);
		btnTitleAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, AddActivity.class);
				intent.putExtra("date", curDate);
				startActivityForResult(intent, FIRST_REQUEST_CODE);
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
		
		//用户编辑按钮
		tvTitleUserEdit = (TextView) super.findViewById(R.id.tv_title_useredit);
		tvTitleUserEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, UserEditActivity.class);
				startActivity(intent);
			}			
		});

		//定时刷新
		runnable = new Runnable() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 3;
				myHandler.sendMessage(message);
				
				myHandler.postDelayed(this, DELAY_TIME);
			}			
		};
	}
		
	//设置ListView	
	protected void setListData(String date) {
		fromDate = date;
		sharedHelper.setCurDate(date);
		tvDateChoose.setText(UtilityHelper.formatDate(date, "y-m-d-w2"));
		
		itemAccess = new ItemTableAccess(sqlHelper.getReadableDatabase());
		list = itemAccess.findHomeTotalByDate(date);
		itemAccess.close();
		adapter = new SimpleAdapter(this, list, R.layout.list_home_total, new String[] { "curlabel", "curprice", "prevlabel", "prevprice" }, new int[] { R.id.tv_curdate_label, R.id.tv_curdate_price, R.id.tv_prevdate_label, R.id.tv_prevdate_price });
		listTotal.setAdapter(adapter);
	}
	
	//关闭this
	protected void close() {
		this.finish();
	}
	
	//返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(sharedHelper.getSyncing()) {
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

	//菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.action_exits:
			if(UtilityHelper.startBackup(this)) {
				sharedHelper.setSyncing(false);
				this.close();
			}
			
			break;
		case R.id.action_settings:
			intent = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intent);
			
			break;
		case R.id.action_abouts:
			intent = new Intent(MainActivity.this, AboutsActivity.class);
			startActivity(intent);
			
			break;
		}
		
		return false;
	}
	
	//页面返回
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		setListData(curDate);
		
		String userName = sharedHelper.getUserName();
		String userNickName = sharedHelper.getUserNickName();
		tvLabUserName.setText(getString(R.string.txt_lab_username) + userName);
		tvLabNickName.setText(getString(R.string.txt_lab_nickname) + userNickName);
		
		//隐藏登录显示修改
		if(sharedHelper.getLogin()) {
			tvTitleLogin.setVisibility(View.GONE);
			tvTitleUserEdit.setVisibility(View.VISIBLE);
			
			String userImage = sharedHelper.getUserImage();
			if(!userImage.equals(""))
			    ivUserImage.setImageBitmap(UtilityHelper.getUserImage(this, userImage));
		}

		//设置同步状态文本
		if(sharedHelper.getSyncing()) {
			pbHomeSync.setVisibility(View.VISIBLE);
			sharedHelper.setSyncStatus(getString(R.string.txt_home_syncing));
		}
		
		tvLabStatus.setText(sharedHelper.getSyncStatus());
	}
	
	//返回处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			if(data != null) {
				curDate = data.getStringExtra("date");
			}
		}
	}
	
	//销毁处理
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		myHandler.removeCallbacks(runnable);
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
			boolean syncFlag = activity.sharedHelper.getSyncing();
			switch(msg.what) {
			case 1:
				activity.pbHomeSync.setVisibility(View.GONE);
				
				String userName = activity.sharedHelper.getUserName();
				String userNickName = activity.sharedHelper.getUserNickName();
				activity.tvLabUserName.setText(activity.getString(R.string.txt_lab_username) + userName);			
				activity.tvLabNickName.setText(activity.getString(R.string.txt_lab_nickname) + userNickName);
				
				if(activity.sharedHelper.getLogin()) {
					activity.tvTitleLogin.setVisibility(View.GONE);
					activity.tvTitleUserEdit.setVisibility(View.VISIBLE);
				}
				
				syncStatus = activity.sharedHelper.getSyncStatus();				
				if(!syncFlag) {
					Toast.makeText(activity, activity.getString(R.string.txt_home_syncerror), Toast.LENGTH_SHORT).show();
				} else {
                    activity.sharedHelper.setSyncing(false);
				}
				
				activity.tvLabStatus.setText(syncStatus);
				activity.setListData(activity.curDate);			
				
				break;
			case 2:
				int res = msg.getData().getInt("result");
				if(res == 1 && !syncFlag) {
					syncStatus = activity.getString(R.string.txt_home_haswebsync);
					activity.sharedHelper.setSyncStatus(syncStatus);
					activity.sharedHelper.setWebSync(true);
					activity.tvLabStatus.setText(syncStatus);
				}
							
				break;
			case 3:
				String date = activity.sharedHelper.getCurDate();
				if(!date.equals("")) {
				    activity.curDate = date;
				    activity.setListData(activity.curDate);
				}
				break;
			}
		}
	}

}
