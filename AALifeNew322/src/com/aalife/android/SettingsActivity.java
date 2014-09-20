package com.aalife.android;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	private SharedHelper sharedHelper = null;
	private EditText etSetLock = null;
	private EditText etSetWelcome = null;
	private TextView tvSetClockText = null;
	private CheckBox cbSetSync = null;
	private CheckBox cbSetUpdate = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		//标题变粗
		TextPaint textPaint = null;
		TextView tvSetLock = (TextView) super.findViewById(R.id.tv_set_lock);
		textPaint = tvSetLock.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvSetWelcome = (TextView) super.findViewById(R.id.tv_set_welcome);
		textPaint = tvSetWelcome.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvSetClock = (TextView) super.findViewById(R.id.tv_set_clock);
		textPaint = tvSetClock.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvSetSync = (TextView) super.findViewById(R.id.tv_set_sync);
		textPaint = tvSetSync.getPaint();
		textPaint.setFakeBoldText(true);
		TextView tvSetUpdate = (TextView) super.findViewById(R.id.tv_set_update);
		textPaint = tvSetUpdate.getPaint();
		textPaint.setFakeBoldText(true);
				
		//初始化
		sharedHelper = new SharedHelper(this);
		etSetLock = (EditText) super.findViewById(R.id.et_set_lock);
		etSetWelcome = (EditText) super.findViewById(R.id.et_set_welcome);
		tvSetClockText = (TextView) super.findViewById(R.id.tv_set_clocktext);
		cbSetSync = (CheckBox) super.findViewById(R.id.cb_set_sync);
		cbSetUpdate = (CheckBox) super.findViewById(R.id.cb_set_update);

		String lock = sharedHelper.getLockText();
		etSetLock.setText(lock);
		String welcome = sharedHelper.getWelcomeText();
		etSetWelcome.setText(welcome);

		if(!sharedHelper.getFixSync()) {
			cbSetSync.setChecked(true);
		}
		
		if(sharedHelper.getUpdate()) {
			cbSetUpdate.setChecked(true);
		}
		
		//设置提醒闹钟
		tvSetClockText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
				boolean flag = false;

				String clockImpls[][] = {
				    { "Android 4.4 Alarm Clock", "com.android.deskclock", "com.android.deskclock.DeskClock" },
				    { "Standard Alarm Clock", "com.android.deskclock", "com.android.deskclock.AlarmClock" },
			        { "Froyo Nexus Alarm Clock", "com.google.android.deskclock", "com.android.deskclock.DeskClock" },
			        { "Android Alarm Clock", "com.android.alarmclock", "com.android.alarmclock.AlarmClock" },
			        { "Moto Blur Alarm Clock", "com.motorola.blur.alarmclock", "com.motorola.blur.alarmclock.AlarmClock" },
			        { "XiaoMi Alarm Clock", "com.android.deskclock", "com.android.deskclock.DeskClockTabActivity" },
			        { "Lenovo Alarm Clock", "com.lenovomobile.deskclock", "com.lenovomobile.deskclock.AlarmClock" },
			        { "HTC Alarm Clock", "com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl" },
			        { "Sony Alarm Clock", "com.sonyericsson.alarm", "com.sonyericsson.alarm.Alarm" },
			        { "Asus Alarm Clock", "com.asus.asusclock", "com.asus.asusclock.deskclock.DeskClock" },
			        { "Samsung Galaxy Clock", "com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage" }
				};

				for(int i=0; i<clockImpls.length; i++) {
				    String packageName = clockImpls[i][1];
				    String className = clockImpls[i][2];
				    ComponentName cn = new ComponentName(packageName, className);
				    intent.setComponent(cn);
				    try {
				        startActivity(intent);
				        flag = true;
				        break;
				    } catch (Exception e) {
				    	continue;
				    }
				}
				
				if(!flag) {
					Toast.makeText(SettingsActivity.this, getString(R.string.txt_set_clockerror), Toast.LENGTH_SHORT).show();
				}
			}			
		});
		
		//返回按钮
		ImageButton btnTitleBack = (ImageButton) super.findViewById(R.id.btn_title_back);
		btnTitleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				SettingsActivity.this.close();
			}			
		});
		
		//设置
		Button btnSetSure = (Button) super.findViewById(R.id.btn_set_sure);
		btnSetSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String lock = etSetLock.getText().toString().trim();
				sharedHelper.setLockText(lock);				
				String welcome = etSetWelcome.getText().toString().trim();
				sharedHelper.setWelcomeText(welcome);
				
				if(cbSetSync.isChecked()) {
					sharedHelper.setFixSync(false);
				} else {
					sharedHelper.setFixSync(true);
				}
				
				if(cbSetUpdate.isChecked()) {
					sharedHelper.setUpdate(true);
				} else {
					sharedHelper.setUpdate(false);
				}
				
				SettingsActivity.this.close();
			}			
		});
	}

	//关闭this
	protected void close() {
		this.finish();
	}
}
