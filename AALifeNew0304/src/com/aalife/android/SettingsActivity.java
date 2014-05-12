package com.aalife.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class SettingsActivity extends Activity {
	private SharedHelper sharedHelper = null;
	private EditText etSetLock = null;
	private EditText etSetWelcome = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		//初始化
		sharedHelper = new SharedHelper(this);
		etSetLock = (EditText) super.findViewById(R.id.et_set_lock);
		etSetWelcome = (EditText) super.findViewById(R.id.et_set_welcome);

		String lock = sharedHelper.getLockText();
		etSetLock.setText(lock);
		String welcome = sharedHelper.getWelcomeText();
		etSetWelcome.setText(welcome);
		
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
				String lock = etSetLock.getText().toString();
				sharedHelper.setLockText(lock);				
				String welcome = etSetWelcome.getText().toString();
				sharedHelper.setWelcomeText(welcome);
				
				SettingsActivity.this.close();
			}			
		});
	}

	//关闭this
	protected void close() {
		this.finish();
	}
	
}
