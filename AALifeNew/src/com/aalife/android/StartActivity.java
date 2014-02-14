package com.aalife.android;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartActivity extends Activity {
	private SharedHelper sharedHelper = null;
	private SyncHelper syncHelper = null;
	private MyHandler myHandler = new MyHandler(this);
	private TextView tvStartLabel = null;
	private LinearLayout layLock = null;
	private EditText etLockText = null;
	private ImageButton btnLockGo = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		//初始化
		sharedHelper = new SharedHelper(this);
		syncHelper = new SyncHelper(this);
		tvStartLabel = (TextView) super.findViewById(R.id.tv_start_lable);
		layLock = (LinearLayout) super.findViewById(R.id.lay_lock);
		etLockText = (EditText) super.findViewById(R.id.et_lock_text);
		btnLockGo = (ImageButton) super.findViewById(R.id.btn_lock_go);
		
		String welcome = sharedHelper.getWelcomeText();
		if(welcome.equals("")) {
			welcome = getString(R.string.app_welcome);
			sharedHelper.setWelcomeText(welcome);
		}
		
		tvStartLabel.setText(welcome);

		//恢复备份数据
		if(!sharedHelper.getRestore()) {
			int result = UtilityHelper.startRestore(this);
			if(result == 1) {
				sharedHelper.setLocalSync(true);
				sharedHelper.setSyncStatus(getString(R.string.txt_home_hassync));
			}
			sharedHelper.setRestore(true);
		}
		
		//如果网络开启
		if(UtilityHelper.checkInternet(this)) {
			//检查网络同步
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
	
			//检查新版本
			if(!sharedHelper.getSyncing()) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						boolean result = false;
						try {
							result = UtilityHelper.checkNewVersion(StartActivity.this);
						} catch (Exception e) {
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
	
	// 返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(UtilityHelper.startBackup(this)) {
			this.finish();
		}
		return true;
	}
		
	//多线程处理
	static class MyHandler extends Handler {
		WeakReference<StartActivity> myActivity = null;
		MyHandler(StartActivity activity) {
			myActivity = new WeakReference<StartActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			boolean result = false;
			final StartActivity activity = myActivity.get();
			switch(msg.what) {
			case 2:
				int res = msg.getData().getInt("result");
				if(res == 1) {
					String syncStatus = activity.getString(R.string.txt_home_haswebsync);
					activity.sharedHelper.setSyncStatus(syncStatus);
					activity.sharedHelper.setWebSync(true);
				}
							
				break;
			case 3:
				result = msg.getData().getBoolean("result");
				if(result) {
					activity.tvStartLabel.setText(R.string.txt_newdowning);
					activity.downNewFile();
				} else {
					String lock = activity.sharedHelper.getLockText();
					if(lock.equals("")) {
						activity.finish();
					}
					activity.jumpActivity();
				}

				break;
			case 4:
				result = msg.getData().getBoolean("result");
				if(result) {
					activity.finish();
				} else {
					activity.tvStartLabel.setText(R.string.txt_updateerror);
					activity.jumpActivity();
				}

				break;				
			}				
		}			
	};
	
	//跳转
	protected void jumpActivity() {
		final String lock = sharedHelper.getLockText();
		if(!lock.equals("")) {
			tvStartLabel.setText(R.string.txt_lock);
			layLock.setVisibility(View.VISIBLE);
			
			btnLockGo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					String text = etLockText.getText().toString();
					if(text.equals(lock)) {
						Intent intent = new Intent(StartActivity.this, MainActivity.class);
						startActivity(intent);  
						StartActivity.this.finish();
					} else {
						tvStartLabel.setText(R.string.txt_lock_error);
					}
				}				
			});
		} else {		
			Intent intent = new Intent(StartActivity.this, MainActivity.class);
			startActivity(intent);  
			this.finish();
		}
	}
	
	//升级
	protected void downNewFile() {
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
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Bundle bundle = new Bundle();
				bundle.putBoolean("result", result);	
				Message message = new Message();
				message.what = 4;
				message.setData(bundle);
				myHandler.sendMessage(message);
			}
		}).start();
	}
}
