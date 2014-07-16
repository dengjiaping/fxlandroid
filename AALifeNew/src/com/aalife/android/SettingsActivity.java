package com.aalife.android;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	private SharedHelper sharedHelper = null;
	private EditText etSetLock = null;
	private EditText etSetWelcome = null;
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
