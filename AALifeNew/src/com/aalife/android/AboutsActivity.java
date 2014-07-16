package com.aalife.android;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
	private final int EGG_CLICK = 8;

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
		Button btnSetSure = (Button) super.findViewById(R.id.btn_set_sure);
		btnSetSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!sharedHelper.getLogin()) {
					Toast.makeText(AboutsActivity.this, getString(R.string.txt_about_notlogin), Toast.LENGTH_SHORT).show();
					return;
				}
				
				final String text = etAboutText.getText().toString().trim();
				if(text.length() < 5) {
					Toast.makeText(AboutsActivity.this, getString(R.string.txt_about_empty), Toast.LENGTH_SHORT).show();
					return;
				}
				
				pbAboutSending.setVisibility(View.VISIBLE);
				
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
			AboutsActivity activity = myActivity.get();
			switch(msg.what) {
			case 1:
				activity.pbAboutSending.setVisibility(View.GONE);
				
				boolean result = msg.getData().getBoolean("result");
				if(result) {
					activity.sharedHelper.setIsSend(true);
					Toast.makeText(activity, activity.getString(R.string.txt_about_emailsuccess), Toast.LENGTH_SHORT).show();
					
					activity.close();
				} else {
					Toast.makeText(activity, activity.getString(R.string.txt_about_emailerror), Toast.LENGTH_SHORT).show();					
				}
				
				break;
			}
		}			
	};	
}
