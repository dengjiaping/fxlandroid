package com.aalife.android;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AALifeActivity extends ActivityGroup {
	private GridView gridViewBar = null;
	private MenuImageAdapter menuAdapter = null;
	private LinearLayout layout = null;
	private int menuImg[] = new int[] { R.drawable.ic_day, R.drawable.ic_month, R.drawable.ic_rank, R.drawable.ic_tongji, R.drawable.ic_add };
	private int width = 0;
	private int height = 0;
	private Intent intent = null;
	private RelativeLayout mainLayout = null;
	private PopupWindow popWin = null;
	private Handler popHandler = new Handler();

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.aalifeactivity);
		
		this.mainLayout = (RelativeLayout) super.findViewById(R.id.mainlayout);
		int theme = UtilityHelper.getTheme(this);
		UtilityHelper.setTheme(this, this.mainLayout, theme);
		
		String nowDate = UtilityHelper.getDateNow("date");
		String sharedDate = UtilityHelper.getSharedDate(this);
		//每周的预警提示显示
		if(!nowDate.equals(sharedDate)) {
			UtilityHelper.setOpenFlag(this, true);
		}
		
		long appInstallTime = UtilityHelper.getAppInstallTime(this);
		long sharedInstallTime = UtilityHelper.getSharedInstallTime(this);
		if(appInstallTime != sharedInstallTime) {
			UtilityHelper.setWarnFlag(this, true);
			UtilityHelper.setRankFlag(this, true);
			UtilityHelper.setTongJiFlag(this, true);
						
			UtilityHelper.setSharedInstallTime(this, appInstallTime);
		} else {
			if(!UtilityHelper.getBackupFlag(this)) {
				if(DataBackupAndRestore.dbRestore(this)) {
					Toast.makeText(this, "恢复成功。", Toast.LENGTH_LONG).show();
				}
				UtilityHelper.setBackupFlag(this, true);
			}
		}

		SQLiteOpenHelper helper = new MyDatabaseHelper(this);
		helper.getWritableDatabase();
		helper.close();
		
		this.gridViewBar = (GridView) super.findViewById(R.id.gridviewbar);
		this.layout = (LinearLayout) super.findViewById(R.id.contentlayout);
		this.gridViewBar.setNumColumns(this.menuImg.length);
		this.gridViewBar.setSelector(new ColorDrawable(Color.TRANSPARENT));
		this.gridViewBar.setGravity(Gravity.CENTER);
		this.gridViewBar.setVerticalSpacing(0);
		this.width = super.getWindowManager().getDefaultDisplay().getWidth() / this.menuImg.length;
		this.menuAdapter = new MenuImageAdapter(this, this.menuImg, this.width, this.height, R.drawable.ic_day);
		this.gridViewBar.setAdapter(this.menuAdapter);		
		this.gridViewBar.setOnItemClickListener(new OnItemClickListenerImpl());

		String pass = UtilityHelper.getPassword(this);
		if(!pass.equals("")) {
			popHandler.postDelayed(popRunnable, 500);
		} else {
			this.switchActivity(0);
		}
	}

	// 菜单点击
	private class OnItemClickListenerImpl implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			AALifeActivity.this.switchActivity(position);
		}
	}

	// 切换菜单
	private void switchActivity(int id) {
		this.setFocus(id);
		// this.menu.setFocus(id);
		this.layout.removeAllViews();
		switch (id) {
		case 0:
			this.intent = new Intent(AALifeActivity.this, DayActivity.class);
			break;
		case 1:
			this.intent = new Intent(AALifeActivity.this, MonthTotalActivity.class);
			break;
		case 2:
			this.intent = new Intent(AALifeActivity.this, RankActivity.class);
			break;
		case 3:
			this.intent = new Intent(AALifeActivity.this, TongJiActivity.class);
			break;
		case 4:
			this.intent = new Intent(AALifeActivity.this, AddActivity.class);
			break;
		}
		this.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window subActivity = this.getLocalActivityManager().startActivity("subActivity", this.intent);
		this.layout.addView(subActivity.getDecorView(), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.layout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
	}

	// 退出  
	private void exitDialog() {
		Dialog dialog = new AlertDialog.Builder(AALifeActivity.this)
				.setIcon(R.drawable.ic_info_d)
				.setTitle("提示")
				.setMessage(R.string.txt_setting_exit)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						UtilityHelper.setMonthPrice(AALifeActivity.this);
						
						Intent intent = new Intent(AALifeActivity.this, AALifeService.class);
						AALifeActivity.this.startService(intent);
						
						DataBackupAndRestore.dbBackup(AALifeActivity.this);
						AALifeActivity.this.finish();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				}).create();
		dialog.show();
	}

	// 返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			String s = getCurrentActivity().getLocalClassName();
			if (s.equals("DayActivity")) {
				this.exitDialog();
				return true;
			} else {
				return getCurrentActivity().onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	// 创建菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.getMenuInflater().inflate(R.menu.aalifemenu, menu);
		return true;
	}

	// 点击菜单
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_password:
			final MyPassView passView = new MyPassView(AALifeActivity.this);
			
			final AlertDialog passDialog = new AlertDialog.Builder(AALifeActivity.this).create();
			passDialog.setIcon(R.drawable.ic_edit_d);
			passDialog.setTitle("设置密码");
			passDialog.setView(passView, 0, 0, 0, 0);
			passDialog.show();

			String pass = UtilityHelper.getPassword(AALifeActivity.this);
			passView.passText.setText(pass);
			
			passView.btnOk.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					String s = passView.passText.getText().toString();
					UtilityHelper.setPassword(AALifeActivity.this, s);
					Toast.makeText(AALifeActivity.this, "设置成功。", Toast.LENGTH_LONG).show();
					passDialog.dismiss();
				}
			});
			
			break;
		case R.id.menu_update:
			Uri uri = Uri.parse(getString(R.string.uri_update));
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			this.startActivity(intent);			
			break;
		case R.id.menu_theme1:
			UtilityHelper.setTheme(this, this.mainLayout, 1);
			
			break;
		case R.id.menu_theme2:
			UtilityHelper.setTheme(this, this.mainLayout, 2);			
			break;
		case R.id.menu_backup:
			DataBackupAndRestore.dbBackup(this);			
			Toast.makeText(this, "备份成功。", Toast.LENGTH_LONG).show();
			
			break;
		case R.id.menu_restore:			
			if(DataBackupAndRestore.dbRestore(this)) {
				Toast.makeText(this, "恢复成功。", Toast.LENGTH_LONG).show();
				this.switchActivity(0);
			} else {
				Toast.makeText(this, "恢复失败。", Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.menu_warn:
			LayoutInflater layout = LayoutInflater.from(this);
			View view = layout.inflate(R.layout.settinglayout, null);
			
			final EditText warnNumEdit = (EditText) view.findViewById(R.id.warntext);
			final EditText warnMonthEdit = (EditText) view.findViewById(R.id.warnmonthtext);
			warnNumEdit.setText(String.valueOf(UtilityHelper.getWarnNumber(this)));
			warnMonthEdit.setText(String.valueOf(UtilityHelper.getWarnMonthNumber(this)));
						
			final AlertDialog setDialog = new AlertDialog.Builder(AALifeActivity.this).create();
			setDialog.setIcon(R.drawable.ic_edit_d);
			setDialog.setTitle("设置");
			setDialog.setView(view, 0, 0, 0, 0);
			setDialog.setButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					UtilityHelper.setWarnNumber(AALifeActivity.this, Integer.parseInt(warnNumEdit.getText().toString()));
					UtilityHelper.setWarnMonthNumber(AALifeActivity.this, Integer.parseInt(warnMonthEdit.getText().toString()));
					AALifeActivity.this.switchActivity(0);
					setDialog.dismiss();
				}
			});
			setDialog.show(); 
			
			break;
		case R.id.menu_about:
			Dialog dialog = new AlertDialog.Builder(AALifeActivity.this)
					.setIcon(R.drawable.ic_info_d)
					.setTitle("关于")
					.setMessage(R.string.txt_setting_about)
					.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.dismiss();
						}
					}).create();
			dialog.show();
			
			break;
		case R.id.menu_exit:
			this.exitDialog();			
			break;
		}
		return false;
	}
	
	// 设置当前菜单
	public void setFocus(int id) {
		this.menuAdapter.setFocus(id);
	}
	
	private Runnable popRunnable = new Runnable(){
		@Override
		public void run() {
			final MyPassView popView = new MyPassView(AALifeActivity.this);
			popView.setFocusable(true);
			popView.setFocusableInTouchMode(true);
			popView.passText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			
			popWin = new PopupWindow(popView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
			popWin.showAtLocation(findViewById(R.id.mainlayout), Gravity.CENTER, 0, 0);
			popView.btnOk.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					String s = popView.passText.getText().toString();
					String pass = UtilityHelper.getPassword(AALifeActivity.this);
					if(s.equals(pass)) {
						popWin.dismiss();
						AALifeActivity.this.switchActivity(0);
					} else {
						Toast.makeText(AALifeActivity.this, "密码错误！", Toast.LENGTH_LONG).show();
						popView.passText.setText("");
					}
				}
			});
			popView.setOnKeyListener(new OnKeyListener(){
				@Override
				public boolean onKey(View view, int keyCode, KeyEvent event) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							popWin.dismiss();
							AALifeActivity.this.finish();
						}
					}
					return true;
				}
			});
		}		
	};

}
