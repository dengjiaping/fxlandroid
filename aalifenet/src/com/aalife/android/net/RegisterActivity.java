package com.aalife.android.net;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private EditText usernameEdit = null;
	private EditText userpassEdit = null;
	private EditText usernicknameEdit = null;
	private EditText usergroupidEdit = null;
	private RadioButton usergroupidRadio = null;
	private EditText useremailEdit = null;
	private EditText userphoneEdit = null;
	private Button registerBtn = null;
	private ImageButton backBtn = null;
	private LinearLayout loginZone = null;
		
	private MyHandler myHandler = new MyHandler(this);
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registeractivity);

		usernameEdit = (EditText) super.findViewById(R.id.usernameedit);
		userpassEdit = (EditText) super.findViewById(R.id.userpassedit);
		usernicknameEdit = (EditText) super.findViewById(R.id.usernicknameedit);
		usergroupidEdit = (EditText) super.findViewById(R.id.usergroupidedit);
		usergroupidRadio = (RadioButton) super.findViewById(R.id.usergroupidradio);
		useremailEdit = (EditText) super.findViewById(R.id.useremailedit);
		userphoneEdit = (EditText) super.findViewById(R.id.userphoneedit);
		registerBtn = (Button) super.findViewById(R.id.registerbtn);
		backBtn = (ImageButton) super.findViewById(R.id.backbtn);
		loginZone = (LinearLayout) super.findViewById(R.id.loginitem);
		
		registerBtn.setOnClickListener(new OnRegisterClickImpl());
		backBtn.setOnClickListener(new OnBackClickImpl());
		
		loginZone.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(RegisterActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		
		usergroupidEdit.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View view, boolean bool) {
				usergroupidRadio.setChecked(false);
			}
		});
	}
	
	static class MyHandler extends Handler{
		WeakReference<RegisterActivity> myActivity = null;
		MyHandler(RegisterActivity activity) {
			myActivity = new WeakReference<RegisterActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			RegisterActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:
				String result = msg.getData().getString("result");
				
				if(result.equals("repeat")) {
					Toast.makeText(activity, R.string.toast_userrepeaterror, Toast.LENGTH_SHORT).show();
				} else if(result.equals("nogroup")) {
					Toast.makeText(activity, R.string.toast_usergroupnull, Toast.LENGTH_SHORT).show();
				} else if(result.equals("error")) {
					Toast.makeText(activity, R.string.toast_registererror, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(activity, R.string.toast_registerok, Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent(activity, AALifeNetActivity.class);
					String[] userinfo = new String[]{ activity.usernameEdit.getText().toString(), activity.userpassEdit.getText().toString() };
					intent.putExtra("userinfo", userinfo);
					activity.setResult(Activity.RESULT_OK, intent);
					activity.finish();
				}
				
				activity.registerBtn.setText(R.string.btn_submitregister);
				activity.registerBtn.setEnabled(true);
				
				break;
			}
		}			
	};
	
	private class OnRegisterClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			MyHelper.hideInputMethod(RegisterActivity.this, view);
			
			final String userName = usernameEdit.getText().toString().trim();
			if(ValidateHelper.validateLength(userName, 3)) {
				Toast.makeText(RegisterActivity.this, R.string.toast_contenterror, Toast.LENGTH_SHORT).show();
				return;
			}
			final String userPass = userpassEdit.getText().toString().trim();
			if(ValidateHelper.validateLength(userPass, 3)) {
				Toast.makeText(RegisterActivity.this, R.string.toast_contenterror, Toast.LENGTH_SHORT).show();
				return;
			}
			final String userNickName = usernicknameEdit.getText().toString().trim();
			
			String _userGroupId = "";
			String userGroupIdText = usergroupidEdit.getText().toString().trim();
			if(usergroupidRadio.isChecked() || ValidateHelper.validateText(userGroupIdText)) {
				_userGroupId = "0";
			} else if(!ValidateHelper.validateText(userGroupIdText)) {
				_userGroupId = userGroupIdText;
			}
			final String userGroupId = _userGroupId;

			final String userEmail = useremailEdit.getText().toString().trim();
			final String userPhone = userphoneEdit.getText().toString().trim();
			
			registerBtn.setText(R.string.btn_text_regising);
			registerBtn.setEnabled(false);
			
			new Thread(new Runnable(){
				@Override
				public void run() {
					MyUser myUser = new MyUser("", userName, userPass, userNickName, "", userGroupId, "", "", userEmail, userPhone);
					String result = myUser.addUser();

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
	
	private class OnBackClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {			
			finish();
			
			MyHelper.hideInputMethod(RegisterActivity.this, view);
		}		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == 0) {
			InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(RegisterActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		
		return super.onTouchEvent(event);		
	}
}
