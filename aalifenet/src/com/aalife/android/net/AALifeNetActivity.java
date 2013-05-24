package com.aalife.android.net;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AALifeNetActivity extends Activity {
	private EditText usernameEdit = null;
	private EditText userpassEdit = null;
	private Button loginBtn = null;
	private Button registerBtn = null;
	private ImageButton exitBtn = null;
	private ImageButton webBtn = null;
	private ImageButton aboutBtn = null;
	
	private ArrayList<String> userList = null;
	
	private MyHandler myHandler = new MyHandler(this);
	
	private ProgressDialog progressDialog = null;
	
	private final int FIRST_REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aalifenetactivity);
				
		if(android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		usernameEdit = (EditText) super.findViewById(R.id.usernameedit);
		userpassEdit = (EditText) super.findViewById(R.id.userpassedit);
		loginBtn = (Button) super.findViewById(R.id.loginbtn);
		registerBtn = (Button) super.findViewById(R.id.registerbtn);
		exitBtn = (ImageButton) super.findViewById(R.id.exitbtn);
		webBtn = (ImageButton) super.findViewById(R.id.webbtn);
		aboutBtn = (ImageButton) super.findViewById(R.id.aboutbtn);
		
		loginBtn.setOnClickListener(new OnLoginClickImpl());
		registerBtn.setOnClickListener(new OnRegisterClickImpl());
		exitBtn.setOnClickListener(new OnExitClickImpl());
		webBtn.setOnClickListener(new OnWebClickImpl());
		aboutBtn.setOnClickListener(new OnAboutClickImpl());
		
		String userName = MyHelper.getUserName(AALifeNetActivity.this);
		usernameEdit.setText(userName);
		if(userName.equals("")){
			usernameEdit.requestFocus();
		} else {
			userpassEdit.requestFocus();
		}

		if(!MyHelper.checkInternet(this)) {
			Toast.makeText(AALifeNetActivity.this, R.string.toast_interneterror, Toast.LENGTH_SHORT).show();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					finish();				
				}
			};			
			Timer timer = new Timer(true);
			timer.schedule(task, 1000);
		} else {		
			if(MyHelper.checkNewVersion(AALifeNetActivity.this)) {
				progressDialog = new ProgressDialog(AALifeNetActivity.this);
				progressDialog.setMessage(getString(R.string.alert_text_downing));
				progressDialog.show();
				
				new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							Uri uri = Uri.fromFile(MyHelper.getInstallFile());
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(uri, "application/vnd.android.package-archive");
							startActivity(intent);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						Message message = new Message();
						message.what = 2;
						myHandler.sendMessage(message);
					}
				}).start();
			}
		}
	}
	
	static class MyHandler extends Handler{
		WeakReference<AALifeNetActivity> myActivity = null;
		MyHandler(AALifeNetActivity activity) {
			myActivity = new WeakReference<AALifeNetActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			AALifeNetActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:
				String result = msg.getData().getString("result");
				
				if(result.equals("nogroup")) {
					Toast.makeText(activity, R.string.toast_logingrouperror, Toast.LENGTH_SHORT).show();
				} else if(result.equals("error")) {
					Toast.makeText(activity, R.string.toast_loginerror, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(activity, R.string.toast_loginok, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(activity, ItemListActivity.class);
					intent.putStringArrayListExtra("users", activity.userList);
					activity.startActivity(intent);
				}
				
				activity.loginBtn.setText(R.string.btn_login);
				activity.loginBtn.setEnabled(true);
				
				break;
			case 2:
				activity.progressDialog.dismiss();
				
				break;
			}
		}			
	};
	
	private class OnLoginClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {						
			MyHelper.hideInputMethod(AALifeNetActivity.this, view);
			
			final String usernameText = usernameEdit.getText().toString().trim();
			if(ValidateHelper.validateText(usernameText)) {
				Toast.makeText(AALifeNetActivity.this, R.string.toast_usernamenull, Toast.LENGTH_SHORT).show();
				return;
			}
			final String userpassText = userpassEdit.getText().toString().trim();
			if(ValidateHelper.validateText(userpassText)) {
				Toast.makeText(AALifeNetActivity.this, R.string.toast_userpassnull, Toast.LENGTH_SHORT).show();
				return;
			}

			MyHelper.setUserName(AALifeNetActivity.this, usernameText);
			
			loginBtn.setText(R.string.btn_text_logining);
			loginBtn.setEnabled(false);			
						
			new Thread(new Runnable(){
				@Override
				public void run() {
					MyUser myUser = new MyUser();
					String result = myUser.getUserListForLogin(usernameText, userpassText);
					userList = myUser.getUserList();
					
					Bundle bundle = new Bundle();
					bundle.putString("result", result);	
					Message message = new Message();
					message.what = 1;
					message.setData(bundle);
					myHandler.sendMessage(message);
				}
			}).start();
		}
	}
	
	private class OnRegisterClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			MyHelper.hideInputMethod(AALifeNetActivity.this, view);
			
			Intent intent = new Intent(AALifeNetActivity.this, RegisterActivity.class);
			startActivityForResult(intent, FIRST_REQUEST_CODE);
		}
	}
	
	private class OnExitClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			finish();
		}		
	}
	
	private class OnWebClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			Uri uri = Uri.parse(MyHelper.getWebUrl());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}
	
	private class OnAboutClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			Dialog dialog = new AlertDialog.Builder(AALifeNetActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(R.string.alert_title_about)
				.setMessage(R.string.alert_text_about)
				.setNegativeButton(R.string.alert_btn_close, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				}).create();
			dialog.show(); 
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FIRST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			String[] userinfo = data.getStringArrayExtra("userinfo");
			usernameEdit.setText(userinfo[0]);
			userpassEdit.setText(userinfo[1]);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == 0) {
			InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(AALifeNetActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		
		return super.onTouchEvent(event);		
	}
}
