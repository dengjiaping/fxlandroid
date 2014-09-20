package com.aalife.android;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UserEditActivity extends Activity {
	private EditText etUserName = null;
	private EditText etUserPass = null;
	private EditText etUserNickName = null;
	private EditText etUserEmail = null;
	private Button btnUserEdit = null;
	private SharedHelper sharedHelper = null;
	private MyHandler myHandler = new MyHandler(this);
	private String[] result = null;
	private ProgressBar pbUserLoading = null;
	private ImageView ivUserImage = null;
	private int userImageSize = 0;
	private static int RESULT_LOAD_IMAGE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_edit);
		
		//标题变粗
		TextPaint textPaint = null;
		TextView tvUserName = (TextView) super.findViewById(R.id.tv_user_name);
		textPaint = tvUserName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvUserPass = (TextView) super.findViewById(R.id.tv_user_pass);
		textPaint = tvUserPass.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvNickName = (TextView) super.findViewById(R.id.tv_user_nickname);
		textPaint = tvNickName.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvUserEmail = (TextView) super.findViewById(R.id.tv_user_email);
		textPaint = tvUserEmail.getPaint();
		textPaint.setFakeBoldText(true);
		
		//初始化
		sharedHelper = new SharedHelper(this);
		pbUserLoading = (ProgressBar) super.findViewById(R.id.pb_user_loading);
		etUserName = (EditText) super.findViewById(R.id.et_user_name);
		etUserPass = (EditText) super.findViewById(R.id.et_user_pass);
		etUserNickName = (EditText) super.findViewById(R.id.et_user_nickname);
		etUserEmail = (EditText) super.findViewById(R.id.et_user_email);
		userImageSize = this.getResources().getDimensionPixelSize(R.dimen.user_image_size);
		pbUserLoading.setVisibility(View.VISIBLE);
		
		//头像按钮
		ivUserImage = (ImageView) super.findViewById(R.id.iv_userimage);
		ivUserImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int userId = sharedHelper.getUserId();
				if(userId > 0) {
				    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				    startActivityForResult(intent, RESULT_LOAD_IMAGE);
				}
			}			
		});
		
		//返回按钮
		ImageButton btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				UserEditActivity.this.close();
			}			
		});
		
		//修改资料按钮
		btnUserEdit = (Button) super.findViewById(R.id.btn_user_edit);
		btnUserEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final String userName = etUserName.getText().toString().trim();
				if (userName.equals("")) {
					Toast.makeText(UserEditActivity.this, getString(R.string.txt_user_username) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
					return;
				}

				final String userPass = etUserPass.getText().toString().trim();
				if (userPass.equals("")) {
					Toast.makeText(UserEditActivity.this, getString(R.string.txt_user_userpass) + getString(R.string.txt_nonull), Toast.LENGTH_SHORT).show();
					return;
				}
				
				final String userNickName = etUserNickName.getText().toString().trim();
				
				final String userEmail = etUserEmail.getText().toString().trim();
				if(!userEmail.equals("") && !UtilityHelper.isEmailAddress(userEmail)) {
					Toast.makeText(UserEditActivity.this, getString(R.string.txt_user_emailerror), Toast.LENGTH_SHORT).show();
					return;
				}
				
				final String userImage = sharedHelper.getUserImage();
				final String userFrom = getString(R.string.app_version);
				
				pbUserLoading.setVisibility(View.VISIBLE);
				btnUserEdit.setEnabled(false);
				
				new Thread(new Runnable(){
					@Override
					public void run() {						
						int userId = sharedHelper.getUserId();
						int result = UtilityHelper.editUser(userId, userName, userPass, userNickName, userImage, userEmail, userFrom);
						
						Bundle bundle = new Bundle();
						bundle.putInt("result", result);
						Message message = new Message();
						message.what = 1;
						message.setData(bundle);
						myHandler.sendMessage(message);
					}
				}).start();
			}			
		});
		
		//获取用户资料
		new Thread(new Runnable(){
			@Override
			public void run() {							
				int userId = sharedHelper.getUserId();
				result = UtilityHelper.getUser(userId);
				
				if(!result[3].equals("")) {
					Boolean success = UtilityHelper.loadBitmap(UserEditActivity.this, result[3]);
					if(success) {
						sharedHelper.setUserImage(result[3]);
					}
				}
				
				Message message = new Message();
				message.what = 2;
				myHandler.sendMessage(message);
			}
		}).start();
		
		//显示头像
		String userImage = sharedHelper.getUserImage();
		if(!userImage.equals("")) {
			ivUserImage.setImageBitmap(UtilityHelper.getUserImage(this, userImage));
		}
		
	}

	//关闭this
	protected void close() {
		this.finish();
	}

	//多线程处理
	static class MyHandler extends Handler {
		WeakReference<UserEditActivity> myActivity = null;
		MyHandler(UserEditActivity activity) {
			myActivity = new WeakReference<UserEditActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			UserEditActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:
				activity.pbUserLoading.setVisibility(View.GONE);
				activity.btnUserEdit.setEnabled(true);
				
				int result = msg.getData().getInt("result");
				
				if(result == 2) {
					Toast.makeText(activity, activity.getString(R.string.txt_user_userrepeat), Toast.LENGTH_SHORT).show();
				} else if(result == 1) {					
					String userName = activity.etUserName.getText().toString().trim();
					activity.sharedHelper.setUserName(userName);	
					String userPass = activity.etUserPass.getText().toString().trim();
					activity.sharedHelper.setUserPass(userPass);				
					String userNickName = activity.etUserNickName.getText().toString().trim();
					activity.sharedHelper.setUserNickName(userNickName);
					String userEmail = activity.etUserEmail.getText().toString().trim();
					activity.sharedHelper.setUserEmail(userEmail);
					
					Toast.makeText(activity, activity.getString(R.string.txt_user_editsuccess), Toast.LENGTH_SHORT).show();
					activity.close();
				} else {
					Toast.makeText(activity, activity.getString(R.string.txt_user_editerror), Toast.LENGTH_SHORT).show();
				}
				
				break;
			case 2:
				activity.pbUserLoading.setVisibility(View.GONE);
				
				if(!activity.result[0].equals("")) {
					activity.etUserName.setText(activity.result[0]);
					activity.etUserPass.setText(activity.result[1]);
					activity.etUserNickName.setText(activity.result[2]);
					activity.etUserEmail.setText(activity.result[4]);
					if(!activity.result[3].equals("")) {
						activity.ivUserImage.setImageBitmap(UtilityHelper.getUserImage(activity, activity.sharedHelper.getUserImage()));
					}
				} else {
					activity.etUserName.setText(activity.sharedHelper.getUserName());
					activity.etUserPass.setText(activity.sharedHelper.getUserPass());
					activity.etUserNickName.setText(activity.sharedHelper.getUserNickName());
					activity.etUserEmail.setText(activity.sharedHelper.getUserEmail());
				}
				
				break;
			case 3:
				activity.pbUserLoading.setVisibility(View.GONE);
				
				boolean success = msg.getData().getBoolean("result");
				String newUserImage = msg.getData().getString("userimage");
				if(success) {
					activity.sharedHelper.setUserImage(newUserImage);
					Toast.makeText(activity, activity.getString(R.string.txt_user_imagesuccess), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(activity, activity.getString(R.string.txt_user_imageerror), Toast.LENGTH_SHORT).show();
				}
				
				break;
			}
		}			
	};
	
	//图片选择返回处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
			Uri originalUri = data.getData();
			ContentResolver resolver = getContentResolver();
			
			Cursor cursor = null;
			String imgExtName = "";
			try {
				cursor = resolver.query(originalUri, null, null, null, null); 
				cursor.moveToFirst(); 
				String imgName = cursor.getString(3);
				imgExtName = UtilityHelper.getFileExtName(imgName);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			} finally {
				cursor.close(); 
			}
					
			Bitmap bitmap = null;
			try {
				bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			bitmap = UtilityHelper.resizeBitmap(bitmap, userImageSize);			
			final String newUserImage = "tu_" + sharedHelper.getUserId() + imgExtName;
			
			boolean success = UtilityHelper.saveBitmap(this, bitmap, newUserImage);
			if(success) {
				pbUserLoading.setVisibility(View.VISIBLE);
				ivUserImage.setImageBitmap(UtilityHelper.getUserImage(this, newUserImage));
				
				new Thread(new Runnable(){
					@Override
					public void run() {		
						boolean result = UtilityHelper.postBitmap(UserEditActivity.this, newUserImage);
						
						Bundle bundle = new Bundle();
						bundle.putBoolean("result", result);
						bundle.putString("userimage", newUserImage);
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
