package com.example.qq_loging_dome;

import java.io.IOException;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract.Constants;
import android.text.NoCopySpan.Concrete;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qq_login_dome.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class MainActivity extends Activity {
	private Button btn, btn_qq;
	private TextView userName;
	private ImageView userImage;
	private Tencent tencent;
	private static QQAuth qqAuth;
	private UserInfo info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button) findViewById(R.id.btn);
		btn_qq = (Button) findViewById(R.id.btn_QQ);
		userName = (TextView) findViewById(R.id.userName);
		userImage = (ImageView) findViewById(R.id.userImage);

		updateLoginButton();
		
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				QQLogin();  登录
				Intent intent = new Intent(MainActivity.this,ShareQzone.class);
				startActivity(intent);
			}
		});
		btn_qq.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,ShareQzone.class);
				startActivity(intent);
			}
		});
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class BaseIuilistener implements IUiListener {

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(Object arg0) {
			// TODO Auto-generated method stub
			doComplete((JSONObject) arg0);
		}

		protected void doComplete(JSONObject arg1) {
			Log.d("dwad", arg1.toString());
		}

		@Override
		public void onError(UiError arg0) {
			// TODO Auto-generated method stub

		}

	}

	public void QQLogin() {
		if(tencent == null){
			tencent = Tencent.createInstance("你的 AppID",
					this.getApplicationContext());
		}
		if (!tencent.isSessionValid()) {
			
			
			IUiListener listener = new BaseIuilistener() {
				@Override
				protected void doComplete(JSONObject arg1) {
					// TODO Auto-generated method stub
					updateLoginButton();
					updateUserInfo();
					super.doComplete(arg1);
				}
				
			};
//			tencent.login(MainActivity.this, "all", listener);
			tencent.loginWithOEM(this,"all",listener,"10000144", "10000144","xxxx");
		} else {
			tencent.logout(MainActivity.this);
			updateLoginButton();
			updateUserInfo();
			
		}
	}

	private void updateLoginButton() {
		if (tencent != null && tencent.isSessionValid()) {
			btn.setTextColor(Color.RED);
			btn.setText("�˳�dwadawaʺ�");
			
		} else {
			btn.setTextColor(Color.BLUE);
			btn.setText("��wadawda¼");
			
		}
	}

	private void updateUserInfo() {
		if (tencent != null && tencent.isSessionValid()) {
			IUiListener uiListener = new IUiListener() {

				@Override
				public void onError(UiError arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onComplete(final Object arg0) {
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.obj = arg0;
					msg.what = 0;
					mHandler.sendMessage(msg);
					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Bitmap bitmap = null;
							JSONObject josn=(JSONObject) arg0;
							Log.d("QQ_json",josn.toString());
							Toast.makeText(MainActivity.this, josn.toString(), 10000*9).show();
							if(josn.has("figureurl")){
							try {
								bitmap=getbitmap(josn.getString("figureurl_qq_2"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message msg = new Message();
							msg.obj = bitmap;
							msg.what = 1;
							mHandler.sendMessage(msg);
							super.run();
							}
						}
					}.start();   
					        
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub

				}
			};
			info = new UserInfo(this, tencent.getQQToken());
			info.getUserInfo(uiListener);
			
		}else {
			userName.setText("");
			userImage.setVisibility(android.view.View.GONE);
			userName.setVisibility(android.view.View.GONE);
		}
	}
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				JSONObject response = (JSONObject) msg.obj;
				if (response.has("nickname")) {
					try {
						userName.setVisibility(android.view.View.VISIBLE);
						userName.setText(response.getString("nickname"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else if(msg.what == 1){
				Bitmap bitmap = (Bitmap)msg.obj;
				userImage.setImageBitmap(bitmap);
				userImage.setVisibility(android.view.View.VISIBLE);
			}
		}

	};
	/**
	 * ���һ����������(String)��ȡbitmapͼ��
	 * 
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getbitmap(String imageUri) {
		Log.v("�û�ͷ���url", "getbitmap:" + imageUri);
		// ��ʾ�����ϵ�ͼƬ
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();

			Log.v("��ȡ�û�ͷ��", "image download finished." + imageUri);
		} catch (IOException e) {
			e.printStackTrace();
			Log.v("dwdwdadwwa", "getbitmap bmp fail---");
			return null;
		}
		return bitmap;
	}
}
