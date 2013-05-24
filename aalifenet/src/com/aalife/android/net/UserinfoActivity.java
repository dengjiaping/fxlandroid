package com.aalife.android.net;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class UserinfoActivity extends Activity {
	private EditText usernameEdit = null;
	private EditText userpassEdit = null;
	private EditText usernicknameEdit = null;
	private EditText usergroupidEdit = null;
	private CheckBox usergroupsaveBox = null;
	private EditText useremailEdit = null;
	private EditText userphoneEdit = null;
	private Button submitBtn = null;
	private ImageButton backBtn = null;
	private LinearLayout loginZone = null;

	private ArrayList<String> userList = new ArrayList<String>();
	private String userId = "";
	private String userImage = "";
	private String userGroupId = "";
	private String userGroupLive = "";
	private String userGroupSave = "";
	
	private MyHandler myHandler = new MyHandler(this);
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfoactivity);
		
		Intent intent = super.getIntent();
		userId = intent.getStringExtra("userid");
		
		usernameEdit = (EditText) super.findViewById(R.id.usernameedit);
		userpassEdit = (EditText) super.findViewById(R.id.userpassedit);
		usernicknameEdit = (EditText) super.findViewById(R.id.usernicknameedit);
		usergroupidEdit = (EditText) super.findViewById(R.id.usergroupidedit);
		usergroupsaveBox = (CheckBox) super.findViewById(R.id.usergroupsavebox);
		useremailEdit = (EditText) super.findViewById(R.id.useremailedit);
		userphoneEdit = (EditText) super.findViewById(R.id.userphoneedit);
		submitBtn = (Button) super.findViewById(R.id.submitbtn);
		backBtn = (ImageButton) super.findViewById(R.id.backbtn);
		loginZone = (LinearLayout) super.findViewById(R.id.loginitem);
		
		submitBtn.setText(R.string.btn_text_loading);
		submitBtn.setEnabled(false);
		
		loginZone.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(UserinfoActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		
		new Thread(new Runnable(){
			@Override
			public void run() {				
				MyUser myUser = new MyUser();
				myUser.getUser(userId);
				userList = myUser.getUserList();
				
				Message message = new Message();
				message.what = 2;
				myHandler.sendMessage(message);
			}
		}).start();
		
		submitBtn.setOnClickListener(new OnSubmitClickImpl());
		backBtn.setOnClickListener(new OnBackClickImpl());
	}
	
	static class MyHandler extends Handler{
		WeakReference<UserinfoActivity> myActivity = null;
		MyHandler(UserinfoActivity activity) {
			myActivity = new WeakReference<UserinfoActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			UserinfoActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:
				String result = msg.getData().getString("result");
				
				if(result.equals("groupnull")) {
					Toast.makeText(activity, R.string.toast_usergroupnull, Toast.LENGTH_SHORT).show();
				} else if(result.equals("error")) {
					Toast.makeText(activity, R.string.toast_editerror, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(activity, R.string.toast_editok, Toast.LENGTH_SHORT).show();
					Intent intent  = new Intent(activity, ItemListActivity.class);
					intent.putExtra("live", activity.userGroupLive);
					activity.setResult(Activity.RESULT_OK, intent);
					activity.finish();
				}
				
				activity.submitBtn.setText(R.string.btn_submituserinfo);
				activity.submitBtn.setEnabled(true);
				
				break;
			case 2:				
				activity.usernameEdit.setText(activity.userList.get(1));
				activity.usernameEdit.setEnabled(false);
				activity.userpassEdit.setText(activity.userList.get(2));
				activity.usernicknameEdit.setText(activity.userList.get(3));
				activity.userImage = activity.userList.get(4);
				activity.userGroupId = activity.userList.get(5);
				activity.usergroupidEdit.setText(activity.userList.get(5));
				activity.useremailEdit.setText(activity.userList.get(6));
				activity.userphoneEdit.setText(activity.userList.get(7));
				
				activity.submitBtn.setText(R.string.btn_submituserinfo);
				activity.submitBtn.setEnabled(true);
				
				break;
			}
		}			
	};
	
	private class OnSubmitClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			MyHelper.hideInputMethod(UserinfoActivity.this, view);
			
			final String userName = usernameEdit.getText().toString().trim();
			if(ValidateHelper.validateLength(userName, 3)) {
				Toast.makeText(UserinfoActivity.this, R.string.toast_contenterror, Toast.LENGTH_SHORT).show();
				return;
			}
			final String userNickName = usernicknameEdit.getText().toString().trim();
			final String userPass = userpassEdit.getText().toString().trim();
			if(ValidateHelper.validateLength(userPass, 3)) {
				Toast.makeText(UserinfoActivity.this, R.string.toast_contenterror, Toast.LENGTH_SHORT).show();
				return;
			}
			
			String newUserGroupId = usergroupidEdit.getText().toString().trim();
			if(ValidateHelper.validateText(newUserGroupId)) {
				Toast.makeText(UserinfoActivity.this, R.string.toast_contenterror, Toast.LENGTH_SHORT).show();
				return;
			}
			if(!newUserGroupId.equals(userGroupId)) {
				userGroupId = newUserGroupId;
				userGroupLive = "0";
			} else {
				userGroupLive = "1";
			}
			if(usergroupsaveBox.isChecked()) {
				userGroupSave = "save";
			} else {
				userGroupSave = "delete";
			}
			
			final String userEmail = useremailEdit.getText().toString().trim();
			final String userPhone = userphoneEdit.getText().toString().trim();
			
			submitBtn.setText(R.string.btn_text_editing);
			submitBtn.setEnabled(false);
			
			new Thread(new Runnable(){
				@Override
				public void run() {
					MyUser myUser = new MyUser(userId, userName, userPass, userNickName, userImage, userGroupId, userGroupLive, userGroupSave, userEmail, userPhone);
					String result = myUser.updateUser();

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
			
			MyHelper.hideInputMethod(UserinfoActivity.this, view);
		}		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == 0) {
			InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(UserinfoActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		
		return super.onTouchEvent(event);		
	}
}
