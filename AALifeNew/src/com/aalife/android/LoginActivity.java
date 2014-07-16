package com.aalife.android;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText etUserName = null;
	private EditText etUserPass = null;
	private SharedHelper sharedHelper = null;
	private MyHandler myHandler = new MyHandler(this);
	private String[] result = null;
	private ProgressBar pbUserLoading = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//标题变粗
		TextPaint textPaint = null;
		TextView tvUserName = (TextView) super.findViewById(R.id.tv_user_name);
		textPaint = tvUserName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvUserPass = (TextView) super.findViewById(R.id.tv_user_pass);
		textPaint = tvUserPass.getPaint();
		textPaint.setFakeBoldText(true);
		
		//初始化
		sharedHelper = new SharedHelper(this);
		etUserName = (EditText) super.findViewById(R.id.et_user_name);
		etUserPass = (EditText) super.findViewById(R.id.et_user_pass);
		pbUserLoading = (ProgressBar) super.findViewById(R.id.pb_user_loading);

		//返回按钮
		ImageButton btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				LoginActivity.this.close();
			}			
		});
		
		//登录按钮
		Button btnUserLogin = (Button) super.findViewById(R.id.btn_user_login);
		btnUserLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final String userName = etUserName.getText().toString().trim();
				if (userName.equals("")) {
					Toast.makeText(LoginActivity.this, getString(R.string.txt_user_username) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
					return;
				}

				final String userPass = etUserPass.getText().toString().trim();
				if (userPass.equals("")) {
					Toast.makeText(LoginActivity.this, getString(R.string.txt_user_userpass) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
					return;
				}

				pbUserLoading.setVisibility(View.VISIBLE);
				
				new Thread(new Runnable(){
					@Override
					public void run() {						
						result = UtilityHelper.loginUser(userName, userPass);
						
						if(!result[7].equals("")) {
							Boolean success = UtilityHelper.loadBitmap(LoginActivity.this, result[7]);
							if(success) {
								sharedHelper.setUserImage(result[7]);
							}
						}
						
						Message message = new Message();
						message.what = 1;
						myHandler.sendMessage(message);
					}
				}).start();
			}			
		});	
		
		//注册按钮
		ImageButton btnUserAdd = (ImageButton) super.findViewById(R.id.btn_user_add);
		btnUserAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}			
		});	
	}

	//关闭this
	protected void close() {
		this.finish();
	}
		
	//多线程处理
	static class MyHandler extends Handler {
		WeakReference<LoginActivity> myActivity = null;
		MyHandler(LoginActivity activity) {
			myActivity = new WeakReference<LoginActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			final LoginActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:
				activity.pbUserLoading.setVisibility(View.GONE);
				
				if(activity.result[3].equals("1")) {
					Toast.makeText(activity, activity.getString(R.string.txt_user_userrepeat), Toast.LENGTH_SHORT).show();
				} else if(activity.result[0].equals("0")) {
					Toast.makeText(activity, activity.getString(R.string.txt_user_loginerror), Toast.LENGTH_SHORT).show();
				} else {					
					String userName = activity.etUserName.getText().toString().trim();
					String userPass = activity.etUserPass.getText().toString().trim();
					activity.sharedHelper.setGroup(Integer.parseInt(activity.result[0]));
					activity.sharedHelper.setUserId(Integer.parseInt(activity.result[1]));
					activity.sharedHelper.setUserName(userName);
					activity.sharedHelper.setUserPass(userPass);
					activity.sharedHelper.setUserNickName(activity.result[4]);
					activity.sharedHelper.setUserEmail(activity.result[5]);

					if(!activity.sharedHelper.getHasRestore()) {
						activity.sharedHelper.setWebSync(true);
						activity.sharedHelper.setSyncStatus(activity.getString(R.string.txt_home_haswebsync));
					} else {
						if(activity.result[2].equals("1")) {
							activity.sharedHelper.setWebSync(true);
							activity.sharedHelper.setSyncStatus(activity.getString(R.string.txt_home_haswebsync));
						}
						activity.sharedHelper.setFirstSync(true);
					}

					activity.sharedHelper.setLogin(true);
					Toast.makeText(activity, activity.getString(R.string.txt_user_loginsuccess), Toast.LENGTH_SHORT).show();
					activity.close();
				}
				
				break;
			}
		}			
	};
}
