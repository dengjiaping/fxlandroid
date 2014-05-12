package com.aalife.android;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private ImageButton btnTitleBack = null;
	private EditText etUserName = null;
	private EditText etUserPass = null;
	private Button btnUserLogin = null;
	private SQLiteOpenHelper sqlHelper = null;
	private ItemTableAccess itemAccess = null;
	//private static final String WEBURL = "http://192.168.0.1:81";
	//private static final String WEBURL = "http://10.0.2.2:81";
	private static final String WEBURL = "http://www.fxlweb.com";
	private SharedHelper sharedHelper = null;
	private MyHandler myHandler = new MyHandler(this);
	private int[] result = null;
	private ProgressBar pbUserLoading = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//数据库
		sqlHelper = new DatabaseHelper(this);
		sqlHelper.getWritableDatabase();
		sqlHelper.close();
		
		//初始化
		sharedHelper = new SharedHelper(this);
		etUserName = (EditText) super.findViewById(R.id.et_user_name);
		etUserPass = (EditText) super.findViewById(R.id.et_user_pass);
		pbUserLoading = (ProgressBar) super.findViewById(R.id.pb_user_loading);

		//返回按钮
		btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				LoginActivity.this.close();
			}			
		});
		
		//登录按钮
		btnUserLogin = (Button) super.findViewById(R.id.btn_user_login);
		btnUserLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final String userName = LoginActivity.this.etUserName.getText().toString().trim();
				if (userName.equals("")) {
					Toast.makeText(LoginActivity.this, getString(R.string.txt_user_username) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
					return;
				}

				final String userPass = LoginActivity.this.etUserPass.getText().toString().trim();
				if (userPass.equals("")) {
					Toast.makeText(LoginActivity.this, getString(R.string.txt_user_userpass) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
					return;
				}

				pbUserLoading.setVisibility(View.VISIBLE);
				
				new Thread(new Runnable(){
					@Override
					public void run() {						
						result = loginUser(userName, userPass);
						
						Message message = new Message();
						message.what = 1;
						myHandler.sendMessage(message);
					}
				}).start();
			}			
		});
	}

	//关闭this
	protected void close() {
		this.finish();
	}
	
	//登录用户
	public int[] loginUser(String userName, String userPass) {
		int[] result = new int[3];
		String url = WEBURL +  "/AALifeWeb/SyncLogin.aspx";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", userName));
		params.add(new BasicNameValuePair("userpass", userPass));
		try {
			JSONObject jsonObject = new JSONObject(HttpHelper.post(url, params));
			if(jsonObject.length() > 0) {
				result[0] = jsonObject.getInt("group");
				result[1] = jsonObject.getInt("userid");
				result[2] = jsonObject.getInt("hassync");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
		
	//多线程处理
	static class MyHandler extends Handler {
		WeakReference<LoginActivity> myActivity = null;
		MyHandler(LoginActivity activity) {
			myActivity = new WeakReference<LoginActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			LoginActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:
				activity.pbUserLoading.setVisibility(View.GONE);
				
				if(activity.result[0] > 0) {					
					String userName = activity.etUserName.getText().toString();
					activity.sharedHelper.setGroup(activity.result[0]);
					activity.sharedHelper.setUserId(activity.result[1]);
					activity.sharedHelper.setUserName(userName);
					
					if(activity.result[2] > 0) {
						activity.itemAccess = new ItemTableAccess(activity.sqlHelper.getReadableDatabase());
						activity.itemAccess.deleteAllItem();
						activity.itemAccess.close();
						
						activity.sharedHelper.setWebSync(true);
						activity.sharedHelper.setSyncStatus(activity.getString(R.string.txt_home_haswebsync));
					}

					activity.sharedHelper.setLogin(true);
					
					Toast.makeText(activity, activity.getString(R.string.txt_user_loginsuccess), Toast.LENGTH_SHORT).show();
					activity.close();
				} else {
					Toast.makeText(activity, activity.getString(R.string.txt_user_loginerror), Toast.LENGTH_SHORT).show();
				}
				
				break;
			}
		}			
	};
}
