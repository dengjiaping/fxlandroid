package com.aalife.android;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AboutsActivity extends Activity {
	private SharedHelper sharedHelper = null;
	private EditText etAboutText = null;
	private MyHandler myHandler = new MyHandler(this);
	private ProgressBar pbAboutSending = null;
	private TextView tvAboutVersion = null;
	private int eggCount = 0;
	private final int EGG_CLICK = 6;
	private Button btnSetSure = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abouts);
		
		//标题变粗
		TextPaint textPaint = null;
		TextView tvAboutEmail = (TextView) super.findViewById(R.id.tv_about_email);
		textPaint = tvAboutEmail.getPaint();
		textPaint.setFakeBoldText(true);
				
		//初始化
		sharedHelper = new SharedHelper(this);
		etAboutText= (EditText) super.findViewById(R.id.et_about_text);
		pbAboutSending = (ProgressBar) super.findViewById(R.id.pb_about_sending);
		tvAboutVersion = (TextView) super.findViewById(R.id.tv_about_version);
		
		//返回按钮
		ImageButton btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				AboutsActivity.this.close();
			}			
		});
		
		//发送邮件
		btnSetSure = (Button) super.findViewById(R.id.btn_set_sure);
		btnSetSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!sharedHelper.getLogin()) {
					Toast.makeText(AboutsActivity.this, getString(R.string.txt_about_notlogin), Toast.LENGTH_SHORT).show();
					return;
				}
				
				final String text = etAboutText.getText().toString().trim();
				if(text.length() < 10) {
					Toast.makeText(AboutsActivity.this, getString(R.string.txt_about_empty), Toast.LENGTH_SHORT).show();
					return;
				}
				
				pbAboutSending.setVisibility(View.VISIBLE);
				btnSetSure.setEnabled(false);
				
				new Thread(new Runnable(){
					@Override
					public void run() {			
						
						String userImage = sharedHelper.getUserImage();
						String userNickName = sharedHelper.getUserNickName();
						String userName = sharedHelper.getUserName();
						
						String name = userNickName.equals("") ? userName : userNickName;			
						boolean result = UtilityHelper.sendEmail(name, userImage, text);
						
						Bundle bundle = new Bundle();
						bundle.putBoolean("result", result);	
						Message message = new Message();
						message.what = 1;
						message.setData(bundle);
						myHandler.sendMessage(message);
					}
				}).start();
			}			
		});
		
		//如果已发送
		if(sharedHelper.getIsSend()) {
			etAboutText.setText(R.string.txt_about_emailsuccess);
			etAboutText.setEnabled(false);
			etAboutText.setGravity(Gravity.CENTER);
			
			btnSetSure.setEnabled(false);
		}
		
		//彩蛋
		tvAboutVersion.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				eggCount++;
				if(eggCount == EGG_CLICK) {
					tvAboutVersion.setClickable(false);
					Dialog dialog = new AlertDialog.Builder(AboutsActivity.this)
					    .setCancelable(false)
						.setTitle(R.string.txt_egg)
						.setMessage(R.string.txt_eggtext)
						.setNegativeButton(R.string.txt_sure, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.cancel();
								
								eggCount = 0;
								tvAboutVersion.setClickable(true);
							}
						}).create();
					dialog.show();
				}
				//检查新版本
				if(UtilityHelper.checkInternet(AboutsActivity.this, true)) {
					new Thread(new Runnable(){
						@Override
						public void run() {
							boolean result = false;
							try {
								result = UtilityHelper.checkNewVersion(AboutsActivity.this);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							Bundle bundle = new Bundle();
							bundle.putBoolean("result", result);	
							Message message = new Message();
							message.what = 2;
							message.setData(bundle);
							myHandler.sendMessage(message);
						}
					}).start();
				} else {
					Toast.makeText(AboutsActivity.this, getString(R.string.txt_home_neterror), Toast.LENGTH_SHORT).show();	
				}
			}			
		});
	}

	//关闭this
	protected void close() {
		this.finish();
	}
	
	//多线程处理
	static class MyHandler extends Handler {
		WeakReference<AboutsActivity> myActivity = null;
		MyHandler(AboutsActivity activity) {
			myActivity = new WeakReference<AboutsActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			boolean result = false;
			final AboutsActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:
				activity.pbAboutSending.setVisibility(View.GONE);
				activity.btnSetSure.setEnabled(true);
				
				result = msg.getData().getBoolean("result");
				if(result) {
					activity.sharedHelper.setIsSend(true);
					Toast.makeText(activity, activity.getString(R.string.txt_about_emailsuccess), Toast.LENGTH_SHORT).show();
					
					activity.close();
				} else {
					Toast.makeText(activity, activity.getString(R.string.txt_about_emailerror), Toast.LENGTH_SHORT).show();					
				}
				
				break;
			case 2:
				result = msg.getData().getBoolean("result");
				if(result) {					
					Dialog dialog = new AlertDialog.Builder(activity)
							.setTitle(R.string.txt_tips)
							.setMessage(R.string.txt_newversion)
							.setPositiveButton(R.string.txt_sure, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									activity.tvAboutVersion.setText(R.string.txt_newdowning);
									activity.downNewFile();
								}
							}).setNegativeButton(R.string.txt_cancel, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									dialog.cancel();
								}
							}).create();
					dialog.show();
				} else {
					Toast.makeText(activity, activity.getString(R.string.txt_isnewversion), Toast.LENGTH_SHORT).show();					
				}

				break;
			case 3:
				result = msg.getData().getBoolean("result");
				if(result) {
					activity.close();
				} else {
					activity.tvAboutVersion.setText(R.string.txt_updateerror);
				}

				break;
			}
		}			
	};	
	
	//升级
	protected void downNewFile() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				boolean result = false;
				try {
					Uri uri = Uri.fromFile(UtilityHelper.getInstallFile(AboutsActivity.this));
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
				message.what = 3;
				message.setData(bundle);
				myHandler.sendMessage(message);
			}
		}).start();
	}
}
