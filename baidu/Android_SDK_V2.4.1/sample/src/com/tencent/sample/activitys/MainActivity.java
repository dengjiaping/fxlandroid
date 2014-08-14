package com.tencent.sample.activitys;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.sample.AppConstants;
import com.tencent.sample.R;
import com.tencent.sample.Util;
import com.tencent.sample.weiyun.WeiyunMainActivity;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.tencent.tauth.Tencent;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getName();
    public static String mAppid;
	private Button mNewLoginButton;
	private TextView mUserInfo;
	private ImageView mUserLogo;
    private UserInfo mInfo;
	private EditText mEtAppid = null;
	public static Tencent mTencent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "-->onCreate");
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		// 固定竖屏
		setContentView(R.layout.activity_main_new);
		initViews();
		//setBarTitle("demo菜单");
		mEtAppid = new EditText(this);
		mEtAppid.setText("222222");
		new AlertDialog.Builder(this).setTitle("请输入APP_ID")
				.setCancelable(false)
				.setIcon(android.R.drawable.ic_dialog_info).setView(mEtAppid)
				.setPositiveButton("Commit", mAppidCommitListener)
				.setNegativeButton("Use Default", mAppidCommitListener).show();

	}

	@Override
	protected void onStart() {
		Log.d(TAG, "-->onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "-->onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "-->onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "-->onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "-->onDestroy");
		super.onDestroy();
		mTencent = null;
	}

	private void initViews() {
		mNewLoginButton = (Button) findViewById(R.id.new_login_btn);

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_container);
		OnClickListener listener = new NewClickListener();
		for (int i = 0; i < linearLayout.getChildCount(); i++) {
			View view = linearLayout.getChildAt(i);
			if (view instanceof Button) {
				view.setOnClickListener(listener);
			}
		}
		mUserInfo = (TextView) findViewById(R.id.user_nickname);
		mUserLogo = (ImageView) findViewById(R.id.user_logo);
		updateLoginButton();
	}

	private void updateLoginButton() {
		if (mTencent != null && mTencent.isSessionValid()) {
			mNewLoginButton.setTextColor(Color.RED);
			mNewLoginButton.setText("退出帐号");
		} else {
			mNewLoginButton.setTextColor(Color.BLUE);
			mNewLoginButton.setText("登录");
		}
	}

	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {
				
				@Override
				public void onError(UiError e) {
					
				}
				
				@Override
				public void onComplete(final Object response) {
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					mHandler.sendMessage(msg);
					new Thread(){

						@Override
						public void run() {
							JSONObject json = (JSONObject)response;
							if(json.has("figureurl")){
								Bitmap bitmap = null;
								try {
									bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
								} catch (JSONException e) {
									
								}
								Message msg = new Message();
								msg.obj = bitmap;
								msg.what = 1;
								mHandler.sendMessage(msg);
							}
						}
						
					}.start();
				}
				
				@Override
				public void onCancel() {
					
				}
			};
			mInfo = new UserInfo(this, mTencent.getQQToken());
			mInfo.getUserInfo(listener);
			
		} else {
			mUserInfo.setText("");
			mUserInfo.setVisibility(android.view.View.GONE);
			mUserLogo.setVisibility(android.view.View.GONE);
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				JSONObject response = (JSONObject) msg.obj;
				if (response.has("nickname")) {
					try {
						mUserInfo.setVisibility(android.view.View.VISIBLE);
						mUserInfo.setText(response.getString("nickname"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}else if(msg.what == 1){
				Bitmap bitmap = (Bitmap)msg.obj;
				mUserLogo.setImageBitmap(bitmap);
				mUserLogo.setVisibility(android.view.View.VISIBLE);
			}
		}

	};

	private void onClickLogin() {
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
				    Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
					updateUserInfo();
					updateLoginButton();
				}
			};
			mTencent.login(this, "all", listener);
			Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
		} else {
		    mTencent.logout(this);
			updateUserInfo();
			updateLoginButton();
		}
	}

	public static boolean ready(Context context) {
		if (mTencent == null) {
			return false;
		}
		boolean ready = mTencent.isSessionValid()
				&& mTencent.getQQToken().getOpenId() != null;
		if (!ready)
			Toast.makeText(context, "login and get openId first, please!",
					Toast.LENGTH_SHORT).show();
		return ready;
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			Util.showResultDialog(MainActivity.this, response.toString(), "登录成功");
			doComplete((JSONObject)response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			Util.toastMessage(MainActivity.this, "onError: " + e.errorDetail);
			Util.dismissDialog();
		}

		@Override
		public void onCancel() {
			Util.toastMessage(MainActivity.this, "onCancel: ");
			Util.dismissDialog();
		}
	}

	private DialogInterface.OnClickListener mAppidCommitListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			mAppid = AppConstants.APP_ID;
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				// 用输入的appid
				String editTextContent = mEtAppid.getText().toString().trim();
				if (!TextUtils.isEmpty(editTextContent)) {
				    mAppid = editTextContent;
				}
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				// 默认appid
				break;
			}
			mTencent = Tencent.createInstance(mAppid, MainActivity.this);
		}
	};

	class NewClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Context context = v.getContext();
			Animation shake = AnimationUtils.loadAnimation(context,
					R.anim.shake);
			Class<?> cls = null;
			switch (v.getId()) {
			case R.id.new_login_btn:
				onClickLogin();
				v.startAnimation(shake);
				return;
			case R.id.main_sso_btn:
			    mTencent.isSupportSSOLogin(MainActivity.this);
			    return;
			case R.id.main_getInfo_btn:
				cls = AccountInfoActivity.class;
				break;
			case R.id.main_qqShare_btn:
				cls = QQShareActivity.class;
				break;
			case R.id.main_qzoneShare_btn:
				cls = QZoneShareActivity.class;
				break;
			case R.id.main_social_api_btn:
                cls = SocialApiActivity.class;
                break;
            //-----------------------------------
            //下面的注释请勿删除，编译lite版的时候需要删除[liteexludestart] [liteexludeend]区间的代码
            //[liteexludestart]
			case R.id.main_qzonePic_btn:
				cls = QzonePicturesActivity.class;
				break;
			case R.id.main_tqqInfo_btn:
				cls = TQQInfoActivity.class;
				break;
			case R.id.main_weiyun_btn:
				cls = WeiyunMainActivity.class;
				break;
			case R.id.main_wap_btn:
				cls = WPAActivity.class;
				break;
			case R.id.main_others_btn:
				cls = OtherApiActivity.class;
				break;
			case R.id.main_avatar_btn:
				cls = AvatarActivity.class;
				break;
			case R.id.main_appbar_btn:
				cls = SocialAppbarActivity.class;
				break;
			case R.id.main_qqgroup_btn:
                cls = QQGroupActivity.class;
                break;
            //[liteexludeend]
			}
			v.startAnimation(shake);
			if (cls != null) {
				Intent intent = new Intent(context, cls);
				context.startActivity(intent);
			}
		}
	}
}
